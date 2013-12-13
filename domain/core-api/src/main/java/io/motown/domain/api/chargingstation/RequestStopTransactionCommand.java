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
 * {@code RequestStopTransactionCommand} is the command which is published when a transaction has to be
 * stopped remotely.
 */
public final class RequestStopTransactionCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String transactionId;

    /**
     * Creates a {@code RequestStopTransactionCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param transactionId the unique transaction identifier.
     * @throws NullPointerException if {@code chargingStationId} or {@code transactionId} is {@code null}.
     */
    public RequestStopTransactionCommand(ChargingStationId chargingStationId, String transactionId) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * Gets the transaction id.
     *
     * @return the transaction id.
     */
    public String getTransactionId() {
        return transactionId;
    }
}
