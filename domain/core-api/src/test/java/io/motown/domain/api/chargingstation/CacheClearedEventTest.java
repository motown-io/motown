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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTITY_CONTEXT;
import static org.junit.Assert.assertEquals;

public class CacheClearedEventTest {

    @Test
    public void testCacheClearedEvent() {
        CacheClearedEvent event = new CacheClearedEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT);
        assertEquals(CHARGING_STATION_ID, event.getChargingStationId());
        assertEquals(IDENTITY_CONTEXT, event.getIdentityContext());
    }

    @Test(expected = NullPointerException.class)
    public void testCacheClearedEventWithNullChargingStationId() {
        new CacheClearedEvent(null, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testCacheClearedEventWithNullIdentityContext() {
        new CacheClearedEvent(CHARGING_STATION_ID, null);
    }

    @Test
    public void testEqualsAndHashCodeImplementedAccordingContract() {
        EqualsVerifier.forClass(CacheClearedEvent.class).verify();
    }
}
