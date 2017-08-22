/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.ocpi.service;

import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.motown.ocpi.AppConfig;
import io.motown.ocpi.dto.BusinessDetails;
import io.motown.ocpi.dto.Credentials;
import io.motown.ocpi.dto.SubscriptionUpdate;
import io.motown.ocpi.dto.Version;
import io.motown.ocpi.dto.VersionDetails;
import io.motown.ocpi.dto.Versions;
import io.motown.ocpi.persistence.entities.Endpoint;
import io.motown.ocpi.persistence.entities.Endpoint.ModuleIdentifier;
import io.motown.ocpi.persistence.entities.Subscription;
import io.motown.ocpi.response.CredentialsResponse;
import io.motown.ocpi.response.VersionDetailsResponse;
import io.motown.ocpi.response.VersionsResponse;

/**
 * Services regarding OCPI registration process
 * 
 * SubscriptionService
 * 
 * @author bartwolfs
 *
 */
@Component
public class SubscriptionService extends BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);

	/**
	 * findAllSubscriptions
	 * 
	 * @return
	 */
	public List<Subscription> findAllSubscriptions() {
		return ocpiRepository.findAllSubscriptions();
	}

	/**
	 * findSubscriptionByAuthorizationToken
	 * 
	 * @param authorizationToken
	 * @return
	 */
	public Subscription findSubscriptionByAuthorizationToken(String authorizationToken) {
		return ocpiRepository.findSubscriptionByAuthorizationToken(authorizationToken);
	}

	/**
	 * returns the highest mutual version between the offeredVersions of the
	 * partner and the supported versions on our side
	 *
	 * @param offeredVersions
	 * @return
	 */
	private Version findHighestMutualVersion(Versions offeredVersions) {
		Version highestSupportedVersion = null;

		for (String supportedVersion : Arrays.asList(AppConfig.SUPPORTED_VERSIONS)) {

			Version match = offeredVersions.find(supportedVersion);
			if (match != null) {
				highestSupportedVersion = match;
			}
		}
		return highestSupportedVersion;
	}

	/**
	 * getVersionDetails
	 * 
	 * @param versionURL
	 * @param authorizationToken
	 * @return VersionDetails
	 */
	public VersionDetails getVersionDetails(String versionURL, String authorizationToken) {

		LOG.info("getVersionDetails with token: " + authorizationToken);

		VersionDetailsResponse versionDetailsResponse = (VersionDetailsResponse) doRequest(new HttpGet(versionURL),
				authorizationToken, VersionDetailsResponse.class);
		return versionDetailsResponse.data;
	}

	/**
	 * getVersions
	 * 
	 * @param versionsURL
	 * @param authorizationToken
	 * @return Versions
	 */
	public Versions getVersions(String versionsURL, String authorizationToken) {

		LOG.info("getVersions with token: " + authorizationToken);

		VersionsResponse versionsResponse = (VersionsResponse) doRequest(new HttpGet(versionsURL), authorizationToken,
				VersionsResponse.class);
		return new Versions(versionsResponse.data);
	}

	/**
	 * Posts the credentials of the user with the EMSP credentials endpoint
	 *
	 * @param subscription
	 * @return the definitive partnerAuthorizationToken in the post response
	 */
	private Credentials postCredentials(Subscription subscription) {

		String credentialsUrl = subscription.getEndpoint(ModuleIdentifier.CREDENTIALS).getUrl();
		LOG.info("Posting credentials at " + credentialsUrl + " with authorizationToken: "
				+ subscription.getPartnerAuthorizationToken());

		Credentials credentials = new Credentials();
		credentials.url = HOST_URL + "/cpo/versions";
		credentials.token = subscription.getAuthorizationToken();
		credentials.party_id = PARTY_ID;
		credentials.country_code = COUNTRY_CODE;

		BusinessDetails businessDetails = new BusinessDetails();
		businessDetails.name = CLIENT_NAME;
		credentials.business_details = businessDetails;

		String json = toJson(credentials);

		LOG.info("Credentials POST: " + json);

		CredentialsResponse credentialsResponse = (CredentialsResponse) doRequest(new HttpPost(credentialsUrl),
				subscription.getPartnerAuthorizationToken(), CredentialsResponse.class);
		LOG.debug("credentialsResponse data: " + credentialsResponse);

		return credentialsResponse.data;
	}

	/**
	 * registers with the EMSP with passed as argument the endpoints of this
	 * EMSP are stored in the database, as well as the definitive token
	 *
	 * @param tokenProvider
	 */
	@Transactional
	public void register(Subscription subscription) {
		Endpoint versionsEndpoint = subscription.getEndpoint(ModuleIdentifier.VERSIONS);
		if (versionsEndpoint == null) {
			return;
		}

		LOG.info("Registering, get versions from endpoint " + versionsEndpoint.getUrl());
		Version version = findHighestMutualVersion(
				getVersions(versionsEndpoint.getUrl(), subscription.getPartnerAuthorizationToken()));

		LOG.info("Registering, get versiondetails at " + version.url);
		VersionDetails versionDetails = getVersionDetails(version.url, subscription.getPartnerAuthorizationToken());
		// store version and endpoints for this subscription
		subscription.setOcpiVersion(version.version);

		for (io.motown.ocpi.dto.Endpoint endpoint : versionDetails.endpoints) {

			subscription.addToEndpoints(new Endpoint(endpoint.identifier, endpoint.url.toString()));
			// because the endpoints in 'versionInformationResponse' are not
			// DTO's (yet) we must instantiate ModuleIdentifier from value
		}

		// if not present generate a new token
		if (subscription.getAuthorizationToken() == null) {
			subscription.generateNewAuthorizationToken();
		}
		ocpiRepository.insertOrUpdate(subscription);

		Credentials credentials = postCredentials(subscription);
		if (credentials.token != null) { // if no token update do not overwrite
			// existing partner token!
			LOG.debug("Updating partnerToken with: " + credentials.token);
			subscription.setPartnerAuthorizationToken(credentials.token);
			// at this point we can safely remove the versionsEndpoint
			LOG.info("REMOVING VERIONS-ENDPOINT");
			subscription.getEndpoints().remove(versionsEndpoint);

			ocpiRepository.insertOrUpdate(subscription);
		}
	}

	/**
	 * updateSubscription
	 * 
	 * @param subscriptionUpdate
	 * @return persisted Subscription
	 */
	public Subscription updateSubscription(SubscriptionUpdate subscriptionUpdate) {

		Subscription subscription = ocpiRepository
				.findSubscriptionByAuthorizationToken(subscriptionUpdate.authorizationToken);

		subscription.getEndpoints().clear();
		// store business details, version and endpoints for this subscription
		io.motown.ocpi.persistence.entities.BusinessDetails businessDetails = new io.motown.ocpi.persistence.entities.BusinessDetails();
		businessDetails.setName(subscriptionUpdate.credentials.business_details.name);
		businessDetails.setWebsite(subscriptionUpdate.credentials.business_details.website);

		subscription.setPartnerAuthorizationToken(subscriptionUpdate.credentials.token);

		subscription.setOcpiVersion(subscriptionUpdate.versionDetails.version);
		for (io.motown.ocpi.dto.Endpoint endpointDto : subscriptionUpdate.versionDetails.endpoints) {

			Endpoint endpoint = new Endpoint();
			endpoint.setIdentifier(endpointDto.identifier);
			endpoint.setUrl(endpointDto.url);
			// because the endpoints in 'versionInformationResponse' are not
			// DTO's (yet) we must instantiate ModuleIdentifier from value
			subscription.addToEndpoints(endpoint);
		}
		// generate new token which will invalidate the token that existed
		subscription.generateNewAuthorizationToken();

		return ocpiRepository.insertOrUpdate(subscription);
	}

}
