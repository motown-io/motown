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

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.security.IdentityContext;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code TransactionStartedEvent} is the event which is published when a transaction has started.
 */
public final class TransactionStartedEvent {

    private final ChargingStationId chargingStationId;

    private final TransactionId transactionId;

    private final EvseId evseId;

    private final IdentifyingToken identifyingToken;

    private final int meterStart;

    private final Date timestamp;

    private final Map<String, String> attributes;

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
     * @param evseId            the evse's identifier or position.
     * @param identifyingToken  the token which started the transaction.
     * @param meterStart        meter value in Wh for the evse when the transaction started.
     * @param timestamp         the time at which the transaction started.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information provided by
     *                          the charging station when it started the transaction but which are not required by Motown. Because
     *                          {@link java.util.Map} implementations are potentially mutable a defensive copy is made.
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code transactionId}, {@code evseId}, {@code identifyingToken},
     * {@code timestamp}, {@code attributes} or {@code identityContext} is {@code null}.
     * @throws IllegalArgumentException if {@code evseId} is negative.
     */
    public TransactionStartedEvent(ChargingStationId chargingStationId, TransactionId transactionId, EvseId evseId, IdentifyingToken identifyingToken,
                                   int meterStart, Date timestamp, Map<String, String> attributes, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.meterStart = meterStart;
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
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
     * Gets the evse's identifier or position.
     *
     * @return the evse's identifier or position.
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * Gets the token which started the transaction.
     *
     * @return the token.
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    /**
     * Gets the meter value when the transaction started.
     *
     * @return the meter value when the transaction started.
     */
    public int getMeterStart() {
        return meterStart;
    }

    /**
     * Gets the time at which the transaction started.
     *
     * @return the time at which the transaction started.
     */
    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    /**
     * Gets the attributes associated with the start of the transaction.
     * <p/>
     * These attributes are additional information provided by the charging station when it started the transaction
     * but which are not required by Motown.
     *
     * @return an immutable {@link java.util.Map} of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
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
