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
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ProcessMeterValueCommand} is the command which is published when a charging station has sent a metervalue.
 */
public final class ProcessMeterValueCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private TransactionId transactionId;
    private EvseId evseId;

    private List<MeterValue> meterValueList;

    /**
     * Creates a {@code ProcessMeterValueCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @throws NullPointerException     if {@code chargingStationId} is {@code null}.
     * @throws IllegalArgumentException if {@code evseId} is negative.
     */
    public ProcessMeterValueCommand(ChargingStationId chargingStationId, @Nullable TransactionId transactionId, EvseId evseId, List<MeterValue> meterValueList) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = transactionId;
        this.evseId = checkNotNull(evseId);
        this.meterValueList = checkNotNull(meterValueList);
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
     * Gets the optional transaction identifier.
     *
     * @return the transaction identifier
     */
    @Nullable
    public TransactionId getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the listing of meter values
     *
     * @return
     */
    public List<MeterValue> getMeterValueList() {
        return meterValueList;
    }

    /**
     * Gets the evse identifier.
     *
     * @return the evse identifier
     */
    public EvseId getEvseId() {
        return evseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, transactionId, evseId, meterValueList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ProcessMeterValueCommand other = (ProcessMeterValueCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.transactionId, other.transactionId) && Objects.equals(this.evseId, other.evseId) && Objects.equals(this.meterValueList, other.meterValueList);
    }
}
