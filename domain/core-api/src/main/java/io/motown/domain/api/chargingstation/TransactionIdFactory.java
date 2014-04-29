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

import com.google.common.primitives.Ints;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory for the creation of {@code TransactionId} derivatives
 */
public class TransactionIdFactory {

    private TransactionIdFactory() {
    }

    /**
     * Creates {@code NumberedTransactionId} or {@code UUIDTransactionId} by inspecting the provided transactionId String.
     * @param transactionIdString   the String to create into a TransactionId
     * @param chargingStationId     the chargingstation id
     * @param protocol              the protocol identifier
     * @return a {@code TransactionId}
     * @throws NullPointerException in case transactionIdString is null.
     * @throws IllegalArgumentException in case no {@code TransactionId} could be created.
     */
    public static TransactionId createTransactionId(String transactionIdString, ChargingStationId chargingStationId, String protocol) {
        return Ints.tryParse(checkNotNull(transactionIdString)) != null
                ? new NumberedTransactionId(chargingStationId, protocol, Ints.tryParse(transactionIdString))
                : new UuidTransactionId(UUID.fromString(transactionIdString));
    }
}
