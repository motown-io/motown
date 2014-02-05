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
 * {@code ChangeChargingStationAvailabilityToInoperativeRequestedEvent} is the event which is published when a request has been made to
 * change the availability of a charging station to inoperative.
 */
public final class ChangeChargingStationAvailabilityToInoperativeRequestedEvent implements ChangeAvailabilityChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final ConnectorId connectorId;

    /**
     * Creates a {@code SoftResetChargingStationRequestedEvent} with an identifier, a protocol and connector identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @param connectorId       the identifier of the connector.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol} is {@code null}.
     */
    public ChangeChargingStationAvailabilityToInoperativeRequestedEvent(ChargingStationId chargingStationId, String protocol, ConnectorId connectorId) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
        this.connectorId = checkNotNull(connectorId);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the protocol identifier.
     *
     * @return the protocol identifier.
     */
    @Override
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the connector id.
     *
     * @return the connector id.
     */
    @Override
    public ConnectorId getConnectorId() {
        return connectorId;
    }
}
