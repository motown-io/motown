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

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestReserveNowCommand} is the command which is published when a connector should be
 * reserved.
 */
public final class RequestReserveNowCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final int connectorId;
    private final IdentifyingToken identifyingToken;
    private final Date expiryDate;
    private final IdentifyingToken parentIdentifyingToken;

    /**
     * Creates a {@code RequestReserveNowCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param expiryDate
     * @param parentIdentifyingToken
     * @throws NullPointerException if {@code chargingStationId}, {@code identifyingToken} or {@code expiryDate} is {@code null}.
     */
    public RequestReserveNowCommand(ChargingStationId chargingStationId, int connectorId, IdentifyingToken identifyingToken, Date expiryDate, IdentifyingToken parentIdentifyingToken) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.connectorId = connectorId;
        this.identifyingToken = checkNotNull(identifyingToken);
        this.expiryDate = checkNotNull(expiryDate);
        this.parentIdentifyingToken = parentIdentifyingToken;
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public IdentifyingToken getParentIdentifyingToken() {
        return parentIdentifyingToken;
    }

}
