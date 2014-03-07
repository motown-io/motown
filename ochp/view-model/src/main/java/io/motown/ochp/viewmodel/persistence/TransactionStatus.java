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
package io.motown.ochp.viewmodel.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

public enum TransactionStatus {
    STARTED("Started"),
    STOPPED("Stopped");

    private final String value;

    private TransactionStatus(String value) {
        this.value = value;
    }

    public static TransactionStatus fromValue(String value) {
        for (TransactionStatus transactionStatus : TransactionStatus.values()) {
            if (transactionStatus.value.equalsIgnoreCase(checkNotNull(value))) {
                return transactionStatus;
            }
        }
        throw new IllegalArgumentException(value);
    }


    @Override
    public String toString() {
        return value;
    }
}
