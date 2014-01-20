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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code AuthorizationListVersionReceivedCommand} is the command which is published when the version of the charging
 * stations local authorization list is received.
 */
public final class AuthorizationListVersionReceivedCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final int version;

    /**
     * Creates a {@code AuthorizationListVersionReceivedCommand} with an identifier and the current authorization
     * list version.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param version           the current version of the authorization list on the charging station
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public AuthorizationListVersionReceivedCommand(ChargingStationId chargingStationId, int version) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.version = version;
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
     * @return the current version of the authorization list on the charging station
     */
    public int getVersion() {
        return version;
    }
}
