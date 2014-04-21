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

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChargingStationSentMeterValuesEvent} is the event which is published when a charging station has sent one or
 * more metervalues.
 */
public final class ChargingStationSentMeterValuesEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final TransactionId transactionId;

    private final EvseId evseId;

    private final List<MeterValue> meterValueList;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ChargingStationSentMeterValuesEvent} with an charging station identifier, transaction id, evse id,
     * meter values and identity context.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param transactionId     the identifier of the transaction.
     * @param evseId            the identifier of the evse.
     * @param meterValueList    list of meter values.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code evseId}, {@code meterValuesList} or
     *                              {@code identityContext} is {@code null}.
     */
    public ChargingStationSentMeterValuesEvent(ChargingStationId chargingStationId, @Nullable TransactionId transactionId, EvseId evseId, List<MeterValue> meterValueList, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = transactionId;
        this.evseId = checkNotNull(evseId);
        this.meterValueList = checkNotNull(meterValueList);
        this.identityContext = checkNotNull(identityContext);
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
     * @return the evse identifier.
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * @return the list of metervalues.
     */
    public List<MeterValue> getMeterValueList() {
        return meterValueList;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }
}
