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
package io.motown.ocpi.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.motown.ocpi.service.AuthorizationService;
import io.motown.ocpi.service.SubscriptionService;
import io.motown.ocpi.persistence.entities.Endpoint;
import io.motown.ocpi.persistence.entities.Endpoint.ModuleIdentifier;
import io.motown.ocpi.persistence.entities.Subscription;

/**
 * Batch job for synchronizing the token table against the OCPI providers token
 * database
 * 
 * AuthorizationOcpiUpdateJob
 * 
 * @author bartwolfs
 *
 */
@Component
public class AuthorizationTokenUpdateJob {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationTokenUpdateJob.class);

	private static boolean jobRunning = false;
	
	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private SubscriptionService subscriptionService;

	@Value("${io.motown.ocpi.hosturl}")
	protected String HOST_URL;

	@Scheduled(cron = "${io.motown.ocpi.token.sync.cron}")
	public void synchronizeTokens() {

		LOG.info("synchronizeTokens");
		
		if (!jobRunning){
			LOG.info("Executing SyncJob");

			jobRunning = true;
			try {
				for (Subscription subscription : subscriptionService.findAllSubscriptions()) {
	
					if (!subscription.isRegistered()) {
						subscriptionService.register(subscription);
					}
					Endpoint tokenEndpoint = subscription.getEndpoint(ModuleIdentifier.TOKENS);
					if (tokenEndpoint != null) {
						authorizationService.synchronizeTokens(tokenEndpoint);
					}
				}
			} catch (Exception ex) {
				LOG.error("Exception synchronizing tokens: ", ex);
				throw ex; // to make sure we get a rollback
			} finally {
				jobRunning = false;
			}
		} else {
			LOG.info("SyncJob is already running");
		}
	}
}
