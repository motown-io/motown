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
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code IncomingDataTransferResponseCommand} is the command which communicates the processed response to an initial
 * datatransfer request that was made from the charging station.
 */
public final class IncomingDataTransferResponseCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private String responseData;

    private IncomingDataTransferResultStatus status;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code IncomingDataTransferResponseCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param responseData              the responseData to transfer back to the charging station (empty if no value).
     * @param status            the status of the processing of the datatransfer responseData from the charging station.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code vendorId}, {@code messageId}, {@code responseData} or is
     *                             {@code identityContext} is {@code null}.
     * @throws IllegalArgumentException if {@code vendorId} is empty.
     */
    public IncomingDataTransferResponseCommand(ChargingStationId chargingStationId, String responseData, IncomingDataTransferResultStatus status, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.status = checkNotNull(status);
        this.responseData = checkNotNull(responseData);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public IncomingDataTransferResultStatus getStatus() {
        return status;
    }

    /**
     * @return the responseData.
     */
    public String getResponseData() {
        return responseData;
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
        return Objects.hash(chargingStationId, responseData, status, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final IncomingDataTransferResponseCommand other = (IncomingDataTransferResponseCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.responseData, other.responseData) && Objects.equals(this.status, other.status) && Objects.equals(this.identityContext, other.identityContext);
    }
}
