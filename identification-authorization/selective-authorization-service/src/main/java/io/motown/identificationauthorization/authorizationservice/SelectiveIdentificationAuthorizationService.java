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
package io.motown.identificationauthorization.authorizationservice;

import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.identificationauthorization.app.IdentificationAuthorizationService;
import io.motown.identificationauthorization.authorizationservice.persistence.entities.ChargingStation;
import io.motown.identificationauthorization.authorizationservice.persistence.repositories.ChargingStationRepository;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class SelectiveIdentificationAuthorizationService implements IdentificationAuthorizationService {

    private static final Logger LOG = LoggerFactory.getLogger(SelectiveIdentificationAuthorizationService.class);

    private Map<String, AuthorizationProvider> providers;

    private String defaultAuthorizationProviders;

	private ChargingStationRepository repository;

	public void setProviders(Map<String, AuthorizationProvider> providers) {
		this.providers = providers;
	}

	public void setRepository(ChargingStationRepository repository) {
		this.repository = repository;
	}

	@EventHandler
	void onEvent(ChargingStationCreatedEvent event) {
		String chargingStationId = event.getChargingStationId().getId();
		ChargingStation chargingStation = repository.findOne(chargingStationId);

		if (chargingStation == null) {
			repository.createOrUpdate(new ChargingStation(chargingStationId, defaultAuthorizationProviders));
		}
	}

	/**
	 * Validates the token with the configured authentication providers for the charging station, as soon
	 * as one provider provides valid authorization this will be the result.
	 *
	 * @param token identifying token for which authorization is required
	 * @return validated IdentifyingToken
	 */
	@Override
	public IdentifyingToken validate(IdentifyingToken token, @Nullable ChargingStationId chargingStationId) {
		if (chargingStationId == null) {
            LOG.warn("No charging station id passed to validation request for token {}", token.getToken());
			return token;
		}

        ChargingStation chargingStation = repository.findOne(chargingStationId.getId());

		if (chargingStation == null) {
		    LOG.warn("Charging station not found in repository: {}", chargingStationId);
			return token;
		}

		List<String> chargingStationAuthorizationProviders = chargingStation.getAuthorizationProvidersAsList();

		if (chargingStationAuthorizationProviders.isEmpty()) {
		    LOG.warn("No authorization providers configured for charging station: {}", chargingStationId);
		    return token;
        }

        for (String authorizationProvider:chargingStationAuthorizationProviders) {
            AuthorizationProvider provider = providers.get(authorizationProvider);

            if (provider != null) {
                IdentifyingToken validatedToken = provider.validate(token, chargingStationId);
                if (validatedToken.isValid()) {
                    return validatedToken;
                }
            }
        }

		return token;
	}

    /**
     * The value (comma-separated) for authorization providers that should be used for new charging stations.
     *
     * @param defaultAuthorizationProviders comma-separated list of authorization providers for new charging stations.
     */
	public void setDefaultAuthorizationProviders(String defaultAuthorizationProviders) {
		this.defaultAuthorizationProviders = defaultAuthorizationProviders;
	}

}
