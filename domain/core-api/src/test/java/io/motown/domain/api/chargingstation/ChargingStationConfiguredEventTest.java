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

import java.util.*;

public class ChargingStationConfiguredEventTest {

    public static final int VOLTAGE_230 = 230;
    public static final int PHASE_3 = 3;
    public static final int MAX_AMP_32 = 32;
    public static final ChargingStationId CHARGING_STATION_ID = new ChargingStationId("CS-001");

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
        Set<Evse> evses = new HashSet<>();

        ChargingStationConfiguredEvent command = new ChargingStationConfiguredEvent(CHARGING_STATION_ID, evses, Collections.<String, String>emptyMap());

        List<Connector> connectors = new ArrayList<>();
        connectors.add(new Connector(MAX_AMP_32, PHASE_3, VOLTAGE_230, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_2));
        command.getEvses().add(new Evse(new EvseId(1), connectors));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingConfigurationItems() {
        Map<String, String> configurationItems = new HashMap<>();

        ChargingStationConfiguredEvent command = new ChargingStationConfiguredEvent(CHARGING_STATION_ID, Collections.<Evse>emptySet(), configurationItems);

        command.getConfigurationItems().put("configItem", "configValue");
    }
}
