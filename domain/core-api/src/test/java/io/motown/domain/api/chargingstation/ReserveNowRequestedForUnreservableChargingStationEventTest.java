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

import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getChargingStationId;
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getTextualToken;
import static org.junit.Assert.assertEquals;

public class ReserveNowRequestedForUnreservableChargingStationEventTest {

    private static final EvseId EVSE_ID = new EvseId(1);

    private static final Date EXPIRY_DATE = new Date();

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(null, EVSE_ID, getTextualToken(), EXPIRY_DATE, getTextualToken());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithEvseIdNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(getChargingStationId(), null, getTextualToken(), EXPIRY_DATE, getTextualToken());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithTokenNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(getChargingStationId(), EVSE_ID, null, EXPIRY_DATE, getTextualToken());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithExpiryDateNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(getChargingStationId(), EVSE_ID, getTextualToken(), null, getTextualToken());
    }

    @Test
    public void noNullPointerExceptionThrownWhenCreatingEventWithParentTokenNull() {
        new ReserveNowRequestedForUnreservableChargingStationEvent(getChargingStationId(), EVSE_ID, getTextualToken(), EXPIRY_DATE, null);
    }

    @Test
    public void testAllGetters() {
        ReserveNowRequestedForUnreservableChargingStationEvent event = new ReserveNowRequestedForUnreservableChargingStationEvent(getChargingStationId(), EVSE_ID, getTextualToken(), EXPIRY_DATE, getTextualToken());

        assertEquals(getChargingStationId(), event.getChargingStationId());
        assertEquals(EXPIRY_DATE, event.getExpiryDate());
        assertEquals(EVSE_ID, event.getEvseId());
        assertEquals(getTextualToken(), event.getIdentifyingToken());
        assertEquals(getTextualToken(), event.getParentIdentifyingToken());
    }

}
