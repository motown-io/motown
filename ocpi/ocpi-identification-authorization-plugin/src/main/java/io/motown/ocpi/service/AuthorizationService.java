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

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AuthorizationService extends BaseService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationService.class);

	/**
	 * Retrieves tokens from the enpoint passed as argument, and stores these in
	 * the database
	 *
	 * @param tokenEndPoint endpoint to use for retrieving tokens
	 */
	public void synchronizeTokens(Endpoint tokenEndPoint) {

	    Integer subscriptionId = tokenEndPoint.getSubscriptionId();
	    String partnerAuthorizationToken = tokenEndPoint.getSubscription().getPartnerAuthorizationToken();
		String lastSyncDate = getLastSyncDate(subscriptionId);

		int totalCount = 1;
		int numberRetrieved = 0;
		Date startOfSync = new Date();

		while (totalCount > numberRetrieved) {
			String tokenUrl = tokenEndPoint.getUrl();
			tokenUrl += "?offset=" + numberRetrieved + "&limit=" + PAGE_SIZE;

			if (lastSyncDate != null) {
				tokenUrl += "&date_from=" + lastSyncDate;
			}
			LOG.info("Get tokens at endpoint: " + tokenUrl);

			TokenResponse tokenResponse = (TokenResponse) doRequest(new HttpGet(tokenUrl), partnerAuthorizationToken, TokenResponse.class);

			if (tokenResponse.totalCount == null) {
				break;
			}
			totalCount = tokenResponse.totalCount;

			numberRetrieved += tokenResponse.data.size();
			LOG.info("Inserting " + tokenResponse.data.size() + " tokens");

			for (io.motown.ocpi.dto.Token token : tokenResponse.data) {
				insertOrUpdateToken(token, subscriptionId);
			}
		}

		updateLastSynchronizationDate(startOfSync, subscriptionId);
		LOG.info("Number of tokens retrieved: " + numberRetrieved);
	}

    /**
     * Returns the formatted datetime the tokens where last synchronized if it exists, otherwise null.
     *
     * @return last sync date if it exists, null otherwise
     */
    private String getLastSyncDate(Integer subscriptionId) {
        String lastSyncDate = null;

        TokenSyncDate tokenSyncDate = ocpiRepository.getTokenSyncDate(subscriptionId);
        if (tokenSyncDate != null) {
            lastSyncDate = AppConfig.DATE_FORMAT.format(tokenSyncDate.getSyncDate());
        }

        return lastSyncDate;
    }

    /**
     * Updates the last sync date for the passed subscription.
     *
     * @param syncDate       date of the last sync
     * @param subscriptionId the subscription that should be updated
     */
    private void updateLastSynchronizationDate(Date syncDate, Integer subscriptionId) {
        TokenSyncDate tokenSyncDate = ocpiRepository.getTokenSyncDate(subscriptionId);

        if (tokenSyncDate == null) {
            tokenSyncDate = new TokenSyncDate();
            tokenSyncDate.setSubscriptionId(subscriptionId);
        }
        tokenSyncDate.setSyncDate(syncDate);

        ocpiRepository.insertOrUpdate(tokenSyncDate);
    }

    /**
     * Inserts a token or updates it if an existing token is found with the same
     * uid and issuing-company
     *
     * @param tokenUpdate
     */
    private void insertOrUpdateToken(io.motown.ocpi.dto.Token tokenUpdate, Integer subscriptionId) {
        Token token = ocpiRepository.findTokenByUidAndIssuingCompany(tokenUpdate.uid, tokenUpdate.issuer);
        if (token == null) {
            token = new Token();
            token.setUid(tokenUpdate.uid);
            token.setSubscriptionId(subscriptionId);
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

}
