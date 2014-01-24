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
package io.motown.vas.viewmodel.configuration;

import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.UnconfiguredChargingStationBootedEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.motown.vas.viewmodel.domain.TestUtils.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConfigurationHandlerTest {

//    private ConfigurationHandler handler;
//
//    private ConfigurationCommandGateway gateway;
//
//    @Before
//    public void setUp() {
//        handler = new ConfigurationHandler();
//
//        gateway = mock(ConfigurationCommandGateway.class);
//        handler.setCommandGateway(gateway);
//    }
//
//    @Test
//    public void testHandleUnconfiguredChargingStationBootedEvent() {
//        Map<String, String> attributes = new HashMap<>();
//        attributes.put("vendor", getVendor());
//        attributes.put("model", getModel());
//
//        handler.handle(new UnconfiguredChargingStationBootedEvent(getChargingStationId(), getProtocol(), attributes));
//
//        verify(gateway).send(new ConfigureChargingStationCommand(getChargingStationId(), handler.getConnectors(getVendor(), getModel())));
//    }

}
