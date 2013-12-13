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
 * {@code StopTransactionCommand} is the command which is published when a transaction is stopped.
 */
public final class StopTransactionCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String transactionId;

    private final String idTag;

    private final int meterStop;

    private final Date timeStamp;

    /**
     * Creates a {@code StopTransactionCommand} to indicate a transaction has been stopped
     *
     * @param chargingStationId the identifier of the charging station.
     * @param transactionId
     * @param idTag
     * @param meterStop
     * @param timeStamp
     *
     * @throws NullPointerException if {@code chargingStationId} or {@code transactionId} is {@code null}.
     */
    public StopTransactionCommand(ChargingStationId chargingStationId, String transactionId, String idTag, int meterStop, Date timeStamp) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.idTag = idTag;
        this.meterStop = meterStop;
        this.timeStamp = timeStamp;
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
     * Gets the charging transaction identifier.
     *
     * @return the transaction identifier.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the identifier tag.
     *
     * @return the identifier tag.
     */
    public String getIdTag() {
        return idTag;
    }

    /**
     * Gets the meter stop value.
     *
     * @return the meter stop value.
     */
    public int getMeterStop() {
        return meterStop;
    }

    /**
     * Gets the timestamp the transaction has been stopped.
     *
     * @return the timestamp.
     */
    public Date getTimeStamp() {
        return timeStamp;
    }
}
