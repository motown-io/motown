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

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code TransactionStartedEvent} is the event which is published when a transaction has started.
 */
public class TransactionStartedEvent {

    private final ChargingStationId chargingStationId;

    private final String idTag;

    private final int meterStart;

    private final String transactionId;

    private final int connectorId;

    private final Date timestamp;

    /**
     * Creates a {@code TransactionStartedEvent}.
     *
     *
     * @param chargingStationId identifier of the charging station.
     * @param transactionId     identifier of the transaction.
     * @param connectorId       identifier of the connector on which the transaction is started.
     * @param idTag             identifier of the person that started the transaction.
     * @param meterStart        meter value in Wh for the connector at the start of the transaction.
     * @param timestamp         date and time the transaction has been started.
     * @throws NullPointerException if {@code chargingStationId} and/or {@code transactionId} is {@code null}.
     */
    public TransactionStartedEvent(ChargingStationId chargingStationId, String transactionId, int connectorId, String idTag, int meterStart, Date timestamp) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.connectorId = connectorId;
        this.idTag = idTag;
        this.timestamp = timestamp;
        this.meterStart = meterStart;
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
     * Gets the id tag.
     *
     * @return the id tag.
     */
    public String getIdTag() {
        return idTag;
    }

    /**
     * Gets the transaction id.
     *
     * @return the transaction id.
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
}
