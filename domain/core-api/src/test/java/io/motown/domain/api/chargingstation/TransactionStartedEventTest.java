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

import java.util.Collections;
import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class TransactionStartedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new TransactionStartedEvent(null, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new TransactionStartedEvent(CHARGING_STATION_ID, null, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, null, 1, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTimestamp() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullAttributes() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), null);
    }
}
