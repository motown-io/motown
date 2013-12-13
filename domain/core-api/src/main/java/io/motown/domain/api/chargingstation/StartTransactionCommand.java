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
 * {@code StartTransactionCommand} is the command which is published when a charging station has started a transaction.
 */
public final class StartTransactionCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String transactionId;

    private final int connectorId;

    private final String idTag;

    private final int meterStart;

    private final Date timestamp;

    /**
     * Creates a {@code StartTransactionCommand}.
     *
     * @param chargingStationId identifier of the charging station.
     * @param transactionId     identifier of the transaction.
     * @param connectorId       identifier of the connector on which the transaction is started.
     * @param idTag             identifier of the person that started the transaction.
     * @param meterStart        meter value in Wh for the connector at the start of the transaction.
     * @param timestamp         date and time the transaction has been started.
     * @throws NullPointerException if {@code chargingStationId} and/or {@code transactionId} is {@code null}.
     */
    public StartTransactionCommand(ChargingStationId chargingStationId, String transactionId, int connectorId, String idTag, int meterStart, Date timestamp) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.connectorId = connectorId;
        this.idTag = idTag;
        this.meterStart = meterStart;
        this.timestamp = timestamp;
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
     * Gets the transaction identifier.
     *
     * @return the transaction identifier
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the connector identifier.
     *
     * @return the connector identifier.
     */
    public int getConnectorId() {
        return connectorId;
    }

    /**
     * Gets the id tag.
     *
     * @return the id tag.
     */
    public String getIdTag() {
        return idTag;
    }

    /**
     * Gets the meter start value.
     *
     * @return the meter start value.
     */
    public int getMeterStart() {
        return meterStart;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartTransactionCommand that = (StartTransactionCommand) o;

        if (connectorId != that.connectorId) return false;
        if (meterStart != that.meterStart) return false;
        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (idTag != null ? !idTag.equals(that.idTag) : that.idTag != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (!transactionId.equals(that.transactionId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + transactionId.hashCode();
        result = 31 * result + connectorId;
        result = 31 * result + (idTag != null ? idTag.hashCode() : 0);
        result = 31 * result + meterStart;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
