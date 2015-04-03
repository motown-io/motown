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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A number based charging transaction identifier.
 * <p/>
 * The number on which this transaction identifier is based should be unique within a Motown protocol add-on.
 */
public final class NumberedTransactionId implements TransactionId {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final int number;

    /**
     * Creates a {@code NumberedTransactionId}.
     * <p/>
     * To improve the uniqueness of this transaction identifier it is required to provide identifiers of the charging
     * station and protocol. These will be used to form the textual representation of the identifier.
     *
     * @param chargingStationId the charging station's identifier.
     * @param protocol          the protocol identifier.
     * @param number            the number uniquely identifying the transaction.
     * @throws NullPointerException     if {@code chargingStationId} or {@code protocol} is {@code null}.
     * @throws IllegalArgumentException if {@code protocol} is empty or {@code number} is a negative number.
     */
    public NumberedTransactionId(ChargingStationId chargingStationId, String protocol, int number) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        checkArgument(number >= 0);
        this.number = number;
    }

    /**
     * Creates a  {@code NumberedTransactionId}.
     * <p/>
     * This constructor should be used if a {@code NumberedTransactionId} is needed and the transaction number is not
     * available, but the transactionId string is.
     *
     * @param chargingStationId the charging station's identifier.
     * @param protocol          the protocol identifier.
     * @param transactionId     the transaction id string (containing the number).
     * @throws NumberFormatException if the number cannot be extracted from {@code transactionId}.
     */
    public NumberedTransactionId(ChargingStationId chargingStationId, String protocol, String transactionId) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        checkNotNull(transactionId);
        checkArgument(!transactionId.isEmpty());
        this.number = numberFromTransactionIdString(chargingStationId, protocol, transactionId);
    }

    /**
     * Gets the number on which this transaction identifier is based.
     *
     * @return the number on which this transaction identifier is based.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the textual representation of the transaction identifier.
     * <p/>
     * The textual representation is formed by concatenating the charging station identifier, protocol identifier, and
     * the transaction number.
     *
     * @return the textual representation of the transaction identifier.
     */
    @Override
    public String getId() {
        return String.format("%s_%s_%s", chargingStationId.getId(), protocol, number);
    }

    /**
     * Retrieves the number from a transaction id string. ChargingStationId and protocol are passed to make a better
     * guess at the number.
     *
     * @param chargingStationId    the charging station's identifier.
     * @param protocol             the protocol identifier.
     * @param transactionId        the transaction id containing the number.
     * @return the transaction number
     * @throws NumberFormatException if the number cannot be extracted from {@code transactionId}.
     */
    private int numberFromTransactionIdString(ChargingStationId chargingStationId, String protocol, String transactionId) {
        String transactionIdPartBeforeNumber = String.format("%s_%s_", chargingStationId.getId(), protocol);
        try {
            return Integer.parseInt(transactionId.substring(transactionIdPartBeforeNumber.length()));
        } catch (NumberFormatException e) {
            throw new NumberFormatException(String.format("Cannot retrieve transaction number from string [%s]", transactionId));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, protocol, number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NumberedTransactionId other = (NumberedTransactionId) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.number, other.number);
    }
}
