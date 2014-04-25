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

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertEquals;

public class ReserveNowRequestedForUnreservableChargingStationEventTest {

    private static final Date EXPIRY_DATE = new Date();

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(null, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithEvseIdNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, null, IDENTIFYING_TOKEN, EXPIRY_DATE, IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithTokenNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, null, EXPIRY_DATE, IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithExpiryDateNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, null, IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void noNullPointerExceptionThrownWhenCreatingEventWithParentTokenNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, null, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void noNullPointerExceptionThrownWhenCreatingEventWithIdentityContextNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, IDENTIFYING_TOKEN, null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        ReserveNowRequestedForUnreservableChargingStationEvent event =
                new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, now, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT);
        event.getExpiryDate().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, event.getExpiryDate());
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(ReserveNowRequestedForUnreservableChargingStationEvent.class).usingGetClass().verify();
    }
}
