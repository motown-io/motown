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

import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.PROTOCOL;
import static org.junit.Assert.assertTrue;

public class TransactionIdFactoryTest {

    @Test
    public void creationOfNumberedTransactionId() {
        TransactionId transactionId =  TransactionIdFactory.createTransactionId("1234", CHARGING_STATION_ID, PROTOCOL);

        assertTrue(transactionId instanceof NumberedTransactionId);
    }

    @Test
    public void creationOfUUIDTransactionId() {
        TransactionId transactionId =  TransactionIdFactory.createTransactionId("123-456-789-000-000", CHARGING_STATION_ID, PROTOCOL);

        assertTrue(transactionId instanceof UuidTransactionId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedTransactionId() {
        TransactionIdFactory.createTransactionId("%UnsupportedTransactionId%", CHARGING_STATION_ID, PROTOCOL);
    }

    @Test(expected = NullPointerException.class)
    public void nullTransactionId() {
        TransactionIdFactory.createTransactionId(null, CHARGING_STATION_ID, PROTOCOL);
    }
}
