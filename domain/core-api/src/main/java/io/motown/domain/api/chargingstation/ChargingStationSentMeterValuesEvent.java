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

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChargingStationSentMeterValuesEvent} is the event which is published when a charging station has sent one or
 * more metervalues.
 */
public final class ChargingStationSentMeterValuesEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private TransactionId transactionId;
    private ConnectorId connectorId;

    private List<MeterValue> meterValueList;

    /**
     * Creates a {@code ChargingStationSentMeterValuesEvent} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public ChargingStationSentMeterValuesEvent(ChargingStationId chargingStationId, @Nullable TransactionId transactionId, ConnectorId connectorId, List<MeterValue> meterValueList) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = transactionId;
        this.connectorId = checkNotNull(connectorId);
        this.meterValueList = checkNotNull(meterValueList);
    }

    /**
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the transaction identifier.
     */
    @Nullable
    public TransactionId getTransactionId() {
        return transactionId;
    }

    /**
     * @return the connector identifier.
     */
    public ConnectorId getConnectorId() {
        return connectorId;
    }

    /**
     * @return the list of metervalues.
     */
    public List<MeterValue> getMeterValueList() {
        return meterValueList;
    }
}
