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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class StopTransactionRequestedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new StopTransactionRequestedEvent(null, PROTOCOL, TRANSACTION_ID);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullProtocol() {
        new StopTransactionRequestedEvent(CHARGING_STATION_ID, null, TRANSACTION_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingWithEmptyProtocol() {
        new StopTransactionRequestedEvent(CHARGING_STATION_ID, "", TRANSACTION_ID);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new StopTransactionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, null);
    }
}
