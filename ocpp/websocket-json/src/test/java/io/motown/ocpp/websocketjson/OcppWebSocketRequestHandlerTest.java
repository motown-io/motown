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
package io.motown.ocpp.websocketjson;

import io.motown.domain.api.chargingstation.ChangeConfigurationItemRequestedEvent;
import io.motown.domain.api.chargingstation.CorrelationToken;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OcppWebSocketRequestHandlerTest {

    private OcppJsonService service;

    private OcppWebSocketRequestHandler handler;

    @Before
    public void setUp() throws Exception {
        this.handler = new OcppWebSocketRequestHandler();
        this.service = mock(OcppJsonService.class);
        this.handler.setOcppJsonService(this.service);
    }

    @Test
    public void testChangeConfigurationItemRequestedEvent() {
        CorrelationToken correlationToken = new CorrelationToken();

        handler.handle(new ChangeConfigurationItemRequestedEvent(CHARGING_STATION_ID, PROTOCOL, CONFIGURATION_ITEM, NULL_USER_IDENTITY_CONTEXT), correlationToken);

        verify(service).changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM, correlationToken);
    }
}
