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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StopTransactionRequestedEvent} is the event which is published when a request has been made to stop the
 * transaction.
 */
public final class StopTransactionRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final TransactionId transactionId;

    /**
     * Creates a {@code StopTransactionRequestedEvent} with an identifier and a transaction identifier.
     *
     * @param chargingStationId the charging station's identifier.
     * @param protocol          the protocol identifier.
     * @param transactionId     the transaction's identifier.
     * @throws NullPointerException     if {@code chargingStationId} or {@code protocol} or {@code transactionId} is {@code null}.
     * @throws IllegalArgumentException if {@code protocol} is empty.
     */
    public StopTransactionRequestedEvent(ChargingStationId chargingStationId, String protocol, TransactionId transactionId) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.transactionId = checkNotNull(transactionId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Gets the transaction's identifier.
     *
     * @return the transaction's identifier
     */
    public TransactionId getTransactionId() {
        return transactionId;
    }
}
