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
package io.motown.domain.api.chargingstation;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code AuthorizationResultEvent} is the event which is published as a result of a request for authorization.
 */
public class AuthorizationResultEvent {

    private final ChargingStationId chargingStationId;

    private final IdentifyingToken identifyingToken;

    private final AuthorizationResultStatus authorizationResultStatus;

    /**
     * Creates a {@code AuthorizationResultEvent} with an identifier, identifier and result status.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param identifyingToken  the identification that has been authorized.
     * @param authorizationResultStatus the status of authorization result.
     * @throws NullPointerException if {@code chargingStationId}, {@code idTag} or {@code authorizationResultStatus} is {@code null}.
     */
    public AuthorizationResultEvent(ChargingStationId chargingStationId, IdentifyingToken identifyingToken, AuthorizationResultStatus authorizationResultStatus) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.authorizationResultStatus = checkNotNull(authorizationResultStatus);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the identification.
     *
     * @return the identification.
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    /**
     * Gets the authentication status.
     *
     * @return the authentication status.
     */
    public AuthorizationResultStatus getAuthenticationStatus() {
        return authorizationResultStatus;
    }

}
