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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class SelectiveIdentificationAuthorizationService implements IdentificationAuthorizationService {

	private Map<String, AuthorizationProvider> providers;

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
			repository.createOrUpdate(new ChargingStation(chargingStationId));
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
			return token;
		}

        ChargingStation chargingStation = repository.findOne(chargingStationId.getId());

		if (chargingStation == null) {
			return token;
		}

		List<String> chargingStationAuthorizationProviders = chargingStation.getAuthorizationProvidersAsList();

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

}
