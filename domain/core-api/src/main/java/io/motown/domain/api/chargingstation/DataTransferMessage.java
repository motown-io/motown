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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DataTransferMessage} is the message which is sent to a charging station to communicate in a vendor-specific
 * way.
 */
public class DataTransferMessage {

    private final String vendorId;

    private final String messageId;

    private final String data;

    /**
     * Creates a {@code DataTransferMessage} with a vendor identifier, message identifier, and free format data.
     *
     * @param vendorId          the vendor identifier.
     * @param messageId         the optional additional message identifier.
     * @param data              the free format data to send to the charging station.
     * @throws NullPointerException if {@code vendorId}, {@code messageId}, or {@code data} is {@code null}.
     * @throws IllegalArgumentException if {@code vendorId} is empty.
     */
    public DataTransferMessage(String vendorId, String messageId, String data) {
        checkNotNull(vendorId);
        checkArgument(!vendorId.isEmpty());
        this.vendorId = vendorId;
        this.messageId = checkNotNull(messageId);
        this.data = checkNotNull(data);
    }

    /**
     * Gets the vendor identifier.
     *
     * The vendor identifier specifies the vendor which defined this vendor specific message.
     *
     * @return the vendor identifier.
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * Gets the message identifier.
     *
     * The message identifier specifies the message type of this vendor specific message (in combination with the vendor
     * identifier).
     *
     * @return the message identifier.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Gets the free format data.
     *
     * @return the free format data.
     */
    public String getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(vendorId, messageId, data);
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
        final DataTransferMessage other = (DataTransferMessage) obj;
        return Objects.equals(this.vendorId, other.vendorId) && Objects.equals(this.messageId, other.messageId) && Objects.equals(this.data, other.data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("vendorId", vendorId)
                .add("messageId", messageId)
                .add("data", data)
                .toString();
    }
}
