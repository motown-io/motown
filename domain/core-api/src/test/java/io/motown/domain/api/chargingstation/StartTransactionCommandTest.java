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

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;

public class StartTransactionCommandTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new StartTransactionCommand(null, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new StartTransactionCommand(CHARGING_STATION_ID, null, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, null, 1, new Date());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTimestamp() {
        new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        StartTransactionCommand command = new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, TRANSACTION_NUMBER, now);
        command.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, command.getTimestamp());
    }
}
