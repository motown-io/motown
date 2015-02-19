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

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class StartTransactionInfo {

    private final EvseId evseId;

    private final int meterStart;

    private final Date timestamp;

    private final IdentifyingToken identifyingToken;

    private final Map<String, String> attributes;

    /**
     * Information about a starting transaction.
     *
     * @param evseId            the evse's identifier or position.
     * @param identifyingToken  the token which started the transaction.
     * @param meterStart        meter value in Wh for the evse when the transaction started.
     * @param timestamp         the time at which the transaction started.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information provided by
     *                          the charging station when it booted but which are not required by Motown. Because
     *                          {@link java.util.Map} implementations are potentially mutable a defensive copy is made.
     */
    public StartTransactionInfo(EvseId evseId, int meterStart, Date timestamp, IdentifyingToken identifyingToken, Map<String, String> attributes) {
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.meterStart = meterStart;
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
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

}
