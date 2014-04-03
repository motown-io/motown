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
package io.motown.ocpp.websocketjson.request.handler;

import com.google.gson.Gson;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.OcppWebSocketRequestHandler;
import org.atmosphere.websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ADD_ON_IDENTITY;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StartTransactionRequestHandlerTest {

    private Gson gson;

    private DomainService domainService;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);
    }

    @Test
    public void handleValidRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        StartTransactionRequestHandler handler = new StartTransactionRequestHandler(gson, domainService, OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER, ADD_ON_IDENTITY);

        String requestPayload = "{\n" +
                "  \"connectorId\": 2,\n" +
                "  \"idTag\": \"B4F62CEF\",\n" +
                "  \"timestamp\": \"2013-02-01T15:09:18Z\",\n" +
                "  \"meterStart\": 0,\n" +
                "  \"reservationId\": 0\n" +
                "}";

        WebSocket webSocket = getMockWebSocket();
        handler.handleRequest(CHARGING_STATION_ID, token, requestPayload, webSocket);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(webSocket).write(argumentCaptor.capture());
        String response = argumentCaptor.getValue();
        assertNotNull(response);
    }

}
