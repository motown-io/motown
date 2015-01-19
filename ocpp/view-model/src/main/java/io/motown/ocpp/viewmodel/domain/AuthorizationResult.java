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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.AuthorizationResultStatus;

/**
 * Contains values which reflect the result of an 'authorize' command.
 */
public class AuthorizationResult {
    /**
     * The identifier that has been authorized
     */
    private String idToken;

    /**
     * The status of the authorization
     */
    private AuthorizationResultStatus status;

    /**
     * @param idToken       identifier that has been authorized
     * @param status        the status of the authorization
     */
    public AuthorizationResult(String idToken, AuthorizationResultStatus status) {
        this.idToken = idToken;
        this.status = status;
    }

    public String getIdToken() {
        return idToken;
    }

    public AuthorizationResultStatus getStatus() {
        return status;
    }
}
