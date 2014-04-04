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

import java.util.ArrayList;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertEquals;

public class ChargingStationSentMeterValuesEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new ChargingStationSentMeterValuesEvent(null, TRANSACTION_ID, EVSE_ID, new ArrayList<MeterValue>(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithNullEvseId() {
        new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, null, new ArrayList<MeterValue>(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithMeterValuesNull() {
        new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithIdentityContextNull() {
        new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, new ArrayList<MeterValue>(), null);
    }

    @Test
    public void constructorSetsFields() {
        ChargingStationSentMeterValuesEvent event = new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, new ArrayList<MeterValue>(), NULL_USER_IDENTITY_CONTEXT);

        assertEquals(CHARGING_STATION_ID, event.getChargingStationId());
        assertEquals(TRANSACTION_ID, event.getTransactionId());
        assertEquals(EVSE_ID, event.getEvseId());
        assertEquals(new ArrayList<MeterValue>(), event.getMeterValueList());
        assertEquals(NULL_USER_IDENTITY_CONTEXT, event.getIdentityContext());
    }
}
