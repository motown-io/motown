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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code TransactionStartedEvent} is the event which is published when a transaction has started.
 */
public final class TransactionStartedEvent {

    private final ChargingStationId chargingStationId;

    private final TransactionId transactionId;

    private final StartTransactionInfo startTransactionInfo;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code TransactionStartedEvent}.
     *
     * In contrast to most of the other classes and methods in the Core API the {@code transactionId} and
     * {@code identifyingToken} are possibly mutable. Some default, immutable implementations of these interfaces are
     * provided but the mutability of these parameters can't be guaranteed.
     *
     * @param chargingStationId the charging station's identifier.
     * @param transactionId     the transaction's identifier.
     * @param startTransactionInfo information about the start transaction.
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code transactionId}, {@code evseId}, {@code identifyingToken},
     *                          {@code timestamp}, {@code attributes} or {@code identityContext} is {@code null}.
     * @throws IllegalArgumentException if {@code evseId} is negative.
     */
    public TransactionStartedEvent(ChargingStationId chargingStationId, TransactionId transactionId, StartTransactionInfo startTransactionInfo, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.startTransactionInfo = checkNotNull(startTransactionInfo);
        this.identityContext = checkNotNull(identityContext);
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
     * Gets the start transaction information.
     *
     * @return start transaction information.
     */
    public StartTransactionInfo getStartTransactionInfo() {
        return startTransactionInfo;
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
