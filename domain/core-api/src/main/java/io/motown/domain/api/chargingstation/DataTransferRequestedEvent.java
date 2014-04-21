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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DataTransferRequestedEvent} is the event which is published when a datatransfer to a charging station is requested.
 */
public final class DataTransferRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final String vendorId;

    private final String messageId;

    private final String data;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DataTransferRequestedEvent} with an identifier, vendor identifier, message identifier, and free format data.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol identifier.
     * @param vendorId          the vendor identifier.
     * @param messageId         the optional additional message identifier.
     * @param data              the free format data to send to the charging station.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol}, {@code vendorId} or
     *                              {@code identityContext} is {@code null}.
     */
    public DataTransferRequestedEvent(ChargingStationId chargingStationId, String protocol, String vendorId, String messageId, String data, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        checkNotNull(vendorId);
        checkArgument(!vendorId.isEmpty());
        this.vendorId = vendorId;
        this.messageId = checkNotNull(messageId);
        this.data = checkNotNull(data);
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
     * @return the vendor identifier
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * @return the optional additional message identifier
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @return the free format data
     */
    public String getData() {
        return data;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }
}
