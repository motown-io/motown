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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestStopTransactionCommand} is the command which is published when a transaction should be stopped.
 */
public final class RequestStopTransactionCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final TransactionId transactionId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestStopTransactionCommand} with an identifier, transaction identifier and identity context.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param transactionId     the unique transaction identifier.
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code transactionId} or {@code identityContext} is {@code null}.
     */
    public RequestStopTransactionCommand(ChargingStationId chargingStationId, TransactionId transactionId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.identityContext = checkNotNull(identityContext);
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
     * Gets the transaction id.
     *
     * @return the transaction id.
     */
    public TransactionId getTransactionId() {
        return transactionId;
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
