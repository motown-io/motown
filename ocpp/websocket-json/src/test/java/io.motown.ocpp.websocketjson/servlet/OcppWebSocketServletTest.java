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
package io.motown.ocpp.websocketjson.servlet;

import io.motown.ocpp.websocketjson.OcppJsonService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OcppWebSocketServletTest {

    private OcppJsonService ocppJsonService;

    private OcppWebSocketServlet ocppWebSocketServlet;

    @Before
    public void setup() {
        ocppJsonService = mock(OcppJsonService.class);
        ocppWebSocketServlet = new OcppWebSocketServlet(ocppJsonService);
        ocppWebSocketServlet.setOcppJsonService(ocppJsonService);
    }

    @Test
    public void onOpenWebSocket() throws IOException {
        ocppWebSocketServlet.onOpen(getMockWebSocket());
    }

    @Test
    public void onTextStreamValidateServiceCall() throws IOException {
        Reader reader = new StringReader("content");

        ocppWebSocketServlet.onTextStream(getMockWebSocket(), reader);

        verify(ocppJsonService).handleMessage(CHARGING_STATION_ID, reader);
    }

}
