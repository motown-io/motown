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
package io.motown.ocpi.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.motown.ocpi.AppConfig;
import io.motown.ocpi.dto.Credentials;
import io.motown.ocpi.dto.SubscriptionUpdate;
import io.motown.ocpi.dto.Version;
import io.motown.ocpi.dto.VersionDetails;
import io.motown.ocpi.dto.Versions;
import io.motown.ocpi.persistence.entities.Endpoint.ModuleIdentifier;
import io.motown.ocpi.persistence.entities.Subscription;
import io.motown.ocpi.response.CredentialsResponse;
import io.motown.ocpi.response.Response;
import io.motown.ocpi.response.Response.StatusCode;
import io.motown.ocpi.response.VersionDetailsResponse;
import io.motown.ocpi.response.VersionsResponse;
import io.motown.ocpi.service.SubscriptionService;

/**
 * Implementation for CPO OCPI rest services
 * 
 * CpoService
 * @author bartwolfs
 *
 */
@Service
@Path("/cpo")
@Produces(MediaType.APPLICATION_JSON)
public class CpoService {

	private static final Logger LOG = LoggerFactory.getLogger(CpoService.class);

	@Value( "${io.motown.ocpi.hosturl}" )
    protected String HOST_URL;

	@Autowired
    private SubscriptionService subscriptionService;

    /**
	 * Finds Subscription object based on request.
	 *
	 * @param request
	 *            request that should contain the authorization value
	 * @return subscription object if it can be found for the token value
	 *         extracted from the request.
	 */
	public Subscription getSubscriptionFromRequest(HttpServletRequest request) {
		String tokenHeader = request.getHeader("Authorization");

		if (tokenHeader == null || tokenHeader.indexOf("Token ") != 0) {
			LOG.info("Empty authorizationheader, or header does not start with 'Token ': " + tokenHeader);
			return null;
		}

		String tokenValue = tokenHeader.substring(tokenHeader.indexOf(" ")).trim();
		LOG.info("Token value: " + tokenValue);

		if (tokenValue != null){
			return subscriptionService.findSubscriptionByLukasAuthorizationToken(tokenValue);
		}
		return null;
	}

	@GET
    @Path("/{ocpiVersion}")
    public Response getVersionDetails(@PathParam("ocpiVersion") String ocpiVersion, @Context HttpServletRequest request, @Context SecurityContext securityContext) {

		LOG.info("GGG getVersionDetails " + ocpiVersion);

        if (getSubscriptionFromRequest(request) == null){
        	return new Response(StatusCode.CLIENT_ERROR, "Forbidden");
        }
        
        VersionDetails versionDetails = new VersionDetails();
        versionDetails.version = ocpiVersion;
        io.motown.ocpi.dto.Endpoint endpoint = new io.motown.ocpi.dto.Endpoint();
        endpoint.identifier = ModuleIdentifier.CREDENTIALS;
        endpoint.url = HOST_URL + "/cpo/" + ocpiVersion + "/credentials/";
        versionDetails.endpoints.add(endpoint);
        
        return new VersionDetailsResponse(versionDetails);
    }

	@GET
    @Path("/versions")
    public Response getVersions(@Context HttpServletRequest request) {

		LOG.info("GGG getVersions");
        if (getSubscriptionFromRequest(request) == null){
        	return new Response(StatusCode.CLIENT_ERROR, "Forbidden");
        }
        
        List<Version> versions = new ArrayList<Version>();

        for (String supportedVersion : Arrays.asList(AppConfig.SUPPORTED_VERSIONS)){
        	Version version = new Version();
        	version.version = supportedVersion;
        	version.url = HOST_URL + "/cpo/" + supportedVersion;
            versions.add(version);
        }
        return new VersionsResponse(versions);
    }

    @PUT
    @Path("/{ocpiVersion}/credentials")
    public Response putCredentials(Credentials credentials, @PathParam("ocpiVersion") String ocpiVersion, @Context HttpServletRequest request, @Context SecurityContext securityContext) {
        LOG.info("putCredentials()");

        Subscription subscription = getSubscriptionFromRequest(request);
        if (subscription == null){
        	return new Response(StatusCode.CLIENT_ERROR, "Forbidden");
        }

        Versions versions = subscriptionService.getVersions(credentials.url, credentials.token);
        if (versions == null) {
        	LOG.error("Could not retrieve versions from endpoint ${credentialsDto.url?.url}.");
            return new Response(StatusCode.UNABLE_TO_USE_CLIENT_API);
        }

        Version version = versions.find(ocpiVersion);
        if (version == null) {
        	LOG.error("Client does not support the OCPI version it contacted on Lukas, this is not allowed. Versions URL: ${credentialsDto.url?.url}");
            return new Response(StatusCode.UNSUPPORTED_VERSION);
        }

        VersionDetails versionDetails = subscriptionService.getVersionDetails(version.url, credentials.token);
        if (versionDetails == null) {
        	LOG.error("Unable to retrieve version details from endpoint " + version.url);
            return new Response(StatusCode.UNABLE_TO_USE_CLIENT_API);
        }
        SubscriptionUpdate update = new SubscriptionUpdate();
        update.credentials = credentials;
        update.lukasAuthorizationToken = subscription.getLukasAuthorizationToken();
        update.versionDetails = versionDetails;

        subscription = subscriptionService.updateSubscription(update);

        credentials.token = subscription.getLukasAuthorizationToken();
        
        return new CredentialsResponse(credentials);
    }
}
