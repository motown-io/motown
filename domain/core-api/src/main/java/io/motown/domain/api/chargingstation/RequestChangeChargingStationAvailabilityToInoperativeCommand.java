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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestChangeChargingStationAvailabilityToInoperativeCommand} is the command which is published when a change availability to inoperative
 * of a charging station is requested.
 */
public final class RequestChangeChargingStationAvailabilityToInoperativeCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final int connectorId;

    /**
     * Creates a {@code RequestChangeChargingStationAvailabilityToInoperativeCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param connectorId       the identifier of the connector.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public RequestChangeChargingStationAvailabilityToInoperativeCommand(ChargingStationId chargingStationId, int connectorId) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkArgument(connectorId > 0);
        this.connectorId = connectorId;
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
     * Gets the connector id.
     *
     * @return the connector id.
     */
    public int getConnectorId() {
        return connectorId;
    }
}
