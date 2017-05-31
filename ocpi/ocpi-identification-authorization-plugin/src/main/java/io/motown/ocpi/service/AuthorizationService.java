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

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.client.ClientResponse;

import io.motown.ocpi.AppConfig;
import io.motown.ocpi.persistence.entities.Endpoint;
import io.motown.ocpi.persistence.entities.Token;
import io.motown.ocpi.persistence.entities.TokenSyncDate;
import io.motown.ocpi.response.TokenResponse;

/**
 * Service for Authorization Token handling
 * 
 * AuthorizationService
 * 
 * @author bartwolfs
 *
 */
@Component
public class AuthorizationService extends BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationService.class);

	/**
	 * returns the formatted datetime the tokens where last synchronized
	 *
	 * @return
	 */
	private String getLastSyncDate() {
		String lastSyncDate = null;
		TokenSyncDate tokenSyncDate = ocpiRepository.getTokenSyncDate();
		if (tokenSyncDate != null) {
			lastSyncDate = AppConfig.DATE_FORMAT.format(tokenSyncDate.getSyncDate());
		} else {
			tokenSyncDate = new TokenSyncDate();
		}
		tokenSyncDate.sync();
		ocpiRepository.insertOrUpdate(tokenSyncDate);
		return lastSyncDate;
	}

	/**
	 * Inserts a token or updates it if an existing token is found with the same
	 * uid and issuing-company
	 *
	 * @param tokenUpdate
	 */
	private void insertOrUpdateToken(io.motown.ocpi.dto.Token tokenUpdate) {

		Token token = ocpiRepository.findTokenByUidAndIssuingCompany(tokenUpdate.uid, tokenUpdate.issuer);
		if (token == null) {
			token = new Token();
			token.setUid(tokenUpdate.uid);
			token.setDateCreated(new Date());
		}
		token.setTokenType(tokenUpdate.type);
		token.setAuthId(tokenUpdate.auth_id);
		token.setVisualNumber(tokenUpdate.visual_number);
		token.setIssuingCompany(tokenUpdate.issuer);
		token.setValid(tokenUpdate.valid);
		token.setWhitelist(tokenUpdate.whitelist);
		token.setLanguageCode(tokenUpdate.languageCode);

		try {
			token.setLastUpdated(AppConfig.DATE_FORMAT.parse(tokenUpdate.last_updated));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		ocpiRepository.insertOrUpdate(token);
	}

	/**
	 * Retrieves tokens from the enpoint passed as argument, and stores these in
	 * the database
	 *
	 * @param tokenEndPoint
	 */
	@Transactional
	public void synchronizeTokens(Endpoint tokenEndPoint) {

		String lastSyncDate = getLastSyncDate();

		int totalCount = 1;
		int numberRetrieved = 0;

		while (totalCount > numberRetrieved) {
			String tokenUrl = tokenEndPoint.getUrl();
			tokenUrl += "?offset=" + numberRetrieved + "&limit=" + PAGE_SIZE;

			if (lastSyncDate != null) {
				tokenUrl += "&date_from=" + lastSyncDate;
			}
			LOG.info("get tokens at endpoint: " + tokenUrl);

			ClientResponse clientResponse = getWebResource(tokenUrl,
					tokenEndPoint.getSubscription().getPartnerAuthorizationToken()).get(ClientResponse.class);
			TokenResponse tokenResponse = (TokenResponse) process(clientResponse, TokenResponse.class);

			if (tokenResponse.totalCount == null) {
				break;
			}
			totalCount = tokenResponse.totalCount;

			numberRetrieved += tokenResponse.data.size();
			LOG.info("inserting " + tokenResponse.data.size() + " tokens");

			for (io.motown.ocpi.dto.Token token : tokenResponse.data) {
				insertOrUpdateToken(token);
			}
		}
		LOG.debug("Number of tokens retrieved: " + numberRetrieved);
	}

}
