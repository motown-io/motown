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

/**
 *   Event that signals that the transaction has stopped.
 */
public class TransactionStoppedEvent {
    private final ChargingStationId chargingStationId;
    private final String transactionId;
    private final String idTag;
    private final int meterValueStop;
    private final Date timestamp;

    /**
     * Creates a {@code TransactionStoppedEvent}
     *
     * @param chargingStationId
     * @param transactionId
     * @param idTag
     * @param meterValueStop
     * @param timestamp
     */
    public TransactionStoppedEvent(ChargingStationId chargingStationId, String transactionId, String idTag, int meterValueStop, Date timestamp) {
        this.chargingStationId = chargingStationId;
        this.transactionId = transactionId;
        this.idTag = idTag;
        this.meterValueStop = meterValueStop;
        this.timestamp = timestamp;
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getIdTag() {
        return idTag;
    }

    public int getMeterValueStop() {
        return meterValueStop;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
