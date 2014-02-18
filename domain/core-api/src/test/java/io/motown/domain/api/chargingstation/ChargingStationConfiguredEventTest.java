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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ChargingStationConfiguredEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNullAndEvses() {
        new ChargingStationConfiguredEvent(null, Collections.<Evse>emptySet(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithEvsesNull() {
        new ChargingStationConfiguredEvent(CHARGING_STATION_ID, null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithConfigurationItemsNull() {
        new ChargingStationConfiguredEvent(CHARGING_STATION_ID, Collections.<Evse>emptySet(), null);
    }

    //TODO refactor
    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingEvses() {
        ChargingStationConfiguredEvent command = new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, Collections.<String, String>emptyMap());

        command.getEvses().add(new Evse(EVSE_ID, CONNECTORS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingConfigurationItems() {
        ChargingStationConfiguredEvent command = new ChargingStationConfiguredEvent(CHARGING_STATION_ID, Collections.<Evse>emptySet(), CONFIGURATION_ITEMS);

        command.getConfigurationItems().put("configItem", "configValue");
    }

    @Test
    public void constructorSetsFields() {
        ChargingStationConfiguredEvent event = new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS);

        assertEquals(CHARGING_STATION_ID, event.getChargingStationId());
        assertEquals(EVSES, event.getEvses());
        assertEquals(CONFIGURATION_ITEMS, event.getConfigurationItems());
        assertNotNull(event.toString());
    }
}
