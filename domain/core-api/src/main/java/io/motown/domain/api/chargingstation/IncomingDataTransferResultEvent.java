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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code AuthorizationResultEvent} is the event which is published as a result of a request for authorization.
 */
public class IncomingDataTransferResultEvent {

    private final ChargingStationId chargingStationId;

    private final String data;

    private final IncomingDataTransferResultStatus status;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code AuthorizationResultEvent} with an identifier, identifier and result status.
     *
     * @param chargingStationId         the identifier of the charging station.
     * @param data                      the data to return to the initiator of the data transfer.
     * @param status                    the status of the acceptance of the sent data.
     * @param identityContext           identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code idTag}, {@code authorizationResultStatus} or {@code identityContext} is {@code null}.
     */
    public IncomingDataTransferResultEvent(ChargingStationId chargingStationId, String data, IncomingDataTransferResultStatus status, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.data = checkNotNull(data);
        this.status = checkNotNull(status);
        this.identityContext = checkNotNull(identityContext);
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
     * Gets the data to return to the initiator of the data transfer.
     * @return the data string
     */
    public String getData() {
        return data;
    }

    /**
     * Gets the status of the acceptance of the sent data.
     * @return the result status
     */
    public IncomingDataTransferResultStatus getStatus() {
        return status;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, data, status, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final IncomingDataTransferResultEvent other = (IncomingDataTransferResultEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.data, other.data) && Objects.equals(this.status, other.status) && Objects.equals(this.identityContext, other.identityContext);
    }
}
