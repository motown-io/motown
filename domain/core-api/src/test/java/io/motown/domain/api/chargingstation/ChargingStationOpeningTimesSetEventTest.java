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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class ChargingStationOpeningTimesSetEventTest {

    @Test(expected = NullPointerException.class)
    public void testChargingStationOpeningTimesSetEventWithNullChargingStationId() {
        new ChargingStationOpeningTimesSetEvent(null, OPENING_TIMES, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testChargingStationOpeningTimesSetEventWithNullOpeningTimes() {
        new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, null, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testChargingStationOpeningTimesSetEventWithNullIdentityContext() {
        new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, OPENING_TIMES, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChargingStationOpeningTimesSetEventFinalOpeningTimes() {
        new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, OPENING_TIMES, IDENTITY_CONTEXT).getOpeningTimes().add(OPENING_TIME);
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(ChargingStationOpeningTimesSetEvent.class).usingGetClass().verify();
    }
}
