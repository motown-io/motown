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
package io.motown.identificationauthorization.app;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;

import javax.annotation.Nullable;
import java.util.Set;

public class SequentialIdentificationAuthorizationService implements IdentificationAuthorizationService {

	private Set<AuthorizationProvider> providers;

	public void setProviders(Set<AuthorizationProvider> providers) {
		this.providers = providers;
	}

	/**
	 * Validates the token with the configured authentication providers, as soon
	 * as one provider provides valid authorization this will be the result.
	 *
	 * @param token identifying token for which authorization is required
     * @param chargingStationId optional charging station id for this authorization
	 * @return validated IdentifyingToken
	 */
	@Override
	public IdentifyingToken validate(IdentifyingToken token, @Nullable ChargingStationId chargingStationId) {
		for (AuthorizationProvider provider : providers) {
			IdentifyingToken validatedToken = provider.validate(token, chargingStationId);
			if (validatedToken.isValid()) {
				return validatedToken;
			}
		}
		return token;
	}

}
