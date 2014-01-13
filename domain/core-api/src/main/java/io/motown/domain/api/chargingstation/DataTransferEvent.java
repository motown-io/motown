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

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DataTransferEvent} is the event which is published when a datatransfer to a charging station is requested.
 */
public final class DataTransferEvent implements CommunicationWithChargingStationRequestedEvent  {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private String vendorId;

    private String messageId;

    private String data;

    /**
     * Creates a {@code DataTransferEvent} with an identifier, vendor identifier, message identifier, and free format data.
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol identifier.
     * @param vendorId          the vendor identifier.
     * @param messageId         the optional additional message identifier.
     * @param data              the free format data to send to the charging station.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol} or {@code vendorId} is {@code null}.
     */
    public DataTransferEvent(ChargingStationId chargingStationId, String protocol, String vendorId, String messageId, String data) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        checkNotNull(vendorId);
        checkArgument(!vendorId.isEmpty());
        this.vendorId = vendorId;
        this.messageId = checkNotNull(messageId);
        this.data = checkNotNull(data);
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
}
