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

import io.motown.domain.api.security.IdentityContext;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DataTransferRequestedEvent} is the event which is published when a datatransfer to a charging station is requested.
 */
public final class DataTransferRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final DataTransferMessage dataTransferMessage;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DataTransferRequestedEvent} with an identifier, vendor identifier, message identifier, and free format data.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol identifier.
     * @param dataTransferMessage the data transfer message.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code dataTransferMessage} or
     *                              {@code identityContext} is {@code null}.
     * @throws IllegalArgumentException if {@code protocol} is empty.
     */
    public DataTransferRequestedEvent(ChargingStationId chargingStationId, String protocol, DataTransferMessage dataTransferMessage, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.dataTransferMessage = checkNotNull(dataTransferMessage);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the protocol identifier
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Gets the data transfer message.
     *
     * @return the data transfer message.
     */
    public DataTransferMessage getDataTransferMessage() {
        return dataTransferMessage;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, protocol, dataTransferMessage, identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DataTransferRequestedEvent other = (DataTransferRequestedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.dataTransferMessage, other.dataTransferMessage) && Objects.equals(this.identityContext, other.identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("protocol", protocol)
                .add("dataTransferMessage", dataTransferMessage)
                .add("identityContext", identityContext)
                .toString();
    }
}
