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
package io.motown.sample.simple.authentication;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.IdentifyingToken.AuthenticationStatus;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.identificationauthorization.pluginapi.AuthorizationProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Dummy implementation of {@code AuthorizationProvider}, this service will always return 'true'
 * when asked if a identification is valid.
 */
public class DummyIdentificationAuthorization implements AuthorizationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DummyIdentificationAuthorization.class);

    @Override
    public IdentifyingToken validate(IdentifyingToken token, @Nullable ChargingStationId chargingStationId) {
    	return new TextualToken(token.getToken(), AuthenticationStatus.ACCEPTED, "MSP", "7007");
    }

}
