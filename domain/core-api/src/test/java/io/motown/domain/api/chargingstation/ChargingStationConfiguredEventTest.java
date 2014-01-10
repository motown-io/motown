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

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNullAndConnectors() {
        new ChargingStationConfiguredEvent(null, Collections.<Connector>emptySet(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithConnectorsNull() {
        new ChargingStationConfiguredEvent(new ChargingStationId("CS-001"), null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithConfigurationItemsNull() {
        new ChargingStationConfiguredEvent(new ChargingStationId("CS-001"), Collections.<Connector>emptySet(), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingConnectors() {
        Set<Connector> connectors = new HashSet<>();

        ChargingStationConfiguredEvent command = new ChargingStationConfiguredEvent(new ChargingStationId("CS-001"), connectors, Collections.<String, String>emptyMap());

        command.getConnectors().add(new Connector(new ConnectorId(1), "Type1", 32));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingConfigurationItems() {
        Map<String, String> configurationItems = new HashMap<>();

        ChargingStationConfiguredEvent command = new ChargingStationConfiguredEvent(new ChargingStationId("CS-001"), Collections.<Connector>emptySet(), configurationItems);

        command.getConfigurationItems().put("configItem", "configValue");
    }
}
