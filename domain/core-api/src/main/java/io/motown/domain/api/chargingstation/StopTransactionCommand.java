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
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StopTransactionCommand} is the command which is published when a charging station has stopped a transaction.
 */
public final class StopTransactionCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final TransactionId transactionId;

    private final IdentifyingToken identifyingToken;

    private final int meterStop;

    private final Date timestamp;

    /**
     * Creates a {@code StopTransactionCommand}.
     * <p/>
     * In contrast to most of the other classes and methods in the Core API the {@code transactionId} and
     * {@code identifyingToken} are possibly mutable. Some default, immutable implementations of these interfaces are
     * provided but the mutability of these parameters can't be guaranteed.
     *
     * @param chargingStationId the charging station's identifier.
     * @param transactionId     the transaction's identifier.
     * @param identifyingToken  the token which stopped the transaction.
     * @param meterStop         meter value in Wh for the evse when the transaction stopped.
     * @param timestamp         the time at which the transaction stopped.
     * @throws NullPointerException if {@code chargingStationId}, {@code transactionId}, {@code identifyingToken} or
     *                              {@code timestamp} is {@code null}.
     */
    public StopTransactionCommand(ChargingStationId chargingStationId, TransactionId transactionId, IdentifyingToken identifyingToken, int meterStop, Date timestamp) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.meterStop = meterStop;
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
    }

    /**
     * Gets the charging station's identifier.
     *
     * @return the charging station's identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the transaction's identifier.
     *
     * @return the transaction's identifier
     */
    public TransactionId getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the token which started the transaction.
     *
     * @return the token.
     */
    public IdentifyingToken getIdTag() {
        return identifyingToken;
    }

    /**
     * Gets the meter value when the transaction stopped.
     *
     * @return the meter value when the transaction stopped.
     */
    public int getMeterStop() {
        return meterStop;
    }

    /**
     * Gets the time at which the transaction started.
     *
     * @return the time at which the transaction started.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, transactionId, identifyingToken, meterStop, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StopTransactionCommand other = (StopTransactionCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.transactionId, other.transactionId) && Objects.equals(this.identifyingToken, other.identifyingToken) && Objects.equals(this.meterStop, other.meterStop) && Objects.equals(this.timestamp, other.timestamp);
    }
}
