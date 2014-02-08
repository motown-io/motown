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

public class ConfigureChargingStationCommandTest {

    public static final int MAX_AMP_32 = 32;
    public static final int PHASE_3 = 3;
    public static final int VOLTAGE_230 = 230;

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNullAndEvses() {
        new ConfigureChargingStationCommand(null, Collections.<Evse>emptySet());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithEvsesNull() {
        new ConfigureChargingStationCommand(new ChargingStationId("CS-001"), null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithConfigurationItemsNull() {
        new ConfigureChargingStationCommand(new ChargingStationId("CS-001"), Collections.<Evse>emptySet(), null);
    }

    //TODO refactor
    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingEvses() {
        Set<Evse> evses = new HashSet<>();

        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(new ChargingStationId("CS-001"), evses);

        List<Connector> connectors = new ArrayList<>();
        connectors.add(new Connector(MAX_AMP_32, PHASE_3, VOLTAGE_230, ChargingProtocol.MODE3, Current.AC, ConnectorType.C_TYPE_2));
        command.getEvses().add(new Evse(new EvseId(1), connectors));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingConfigurationItems() {
        Map<String, String> configurationItems = new HashMap<>();

        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(new ChargingStationId("CS-001"), configurationItems);

        command.getSettings().put("configItem", "configValue");
    }
}
