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

import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.identificationauthorization.pluginapi.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class IdentificationAuthorizationService {

    private Set<AuthenticationProvider> providers;

    @Autowired
    public void setProviders(Set<AuthenticationProvider> providers) {
        this.providers = providers;
    }

    /**
     * Validates the token with the configured authentication providers, as soon as one provider indicates the
     * identification is valid this will be the result.
     *
     * @param token identifying token to be validated.
     * @return true if one of the authentication providers indicates the identification is valid, otherwise false.
     */
    public boolean isValid(IdentifyingToken token) {
        boolean valid = false;

        for(AuthenticationProvider provider : providers) {
            if(provider.isValid(token)) {
                valid = true;
                break;
            }
        }

        return valid;
    }

}
