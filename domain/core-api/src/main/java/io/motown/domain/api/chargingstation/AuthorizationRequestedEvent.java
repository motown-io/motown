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
 * {@code AuthorizationRequestedEvent} is the event which is published when a request has been made to
 * authorize a identification.
 */
public final class AuthorizationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final IdentifyingToken identifyingToken;

    /**
     * Creates a {@code AuthorizationRequestedEvent} with an identifier and a identifyingToken.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param identifyingToken  identification which should be authorized.
     * @throws NullPointerException if {@code chargingStationId} or {@code identifyingToken} is {@code null}.
     */
    public AuthorizationRequestedEvent(ChargingStationId chargingStationId, IdentifyingToken identifyingToken) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.identifyingToken = checkNotNull(identifyingToken);
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
     * @return the identification
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }
}
