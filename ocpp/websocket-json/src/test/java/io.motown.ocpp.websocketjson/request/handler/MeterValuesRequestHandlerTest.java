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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MeterValuesRequestHandlerTest {

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
        MeterValuesRequestHandler handler = new MeterValuesRequestHandler(gson, domainService, OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER);

        String requestPayload = "{\n" +
                "  \"connectorId\": 2,\n" +
                "  \"transactionId\": 0,\n" +
                "  \"values\": [\n" +
                "    {\n" +
                "      \"timestamp\": \"2013-03-07T16:52:16Z\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"value\": \"0\",\n" +
                "          \"unit\": \"Wh\",\n" +
                "          \"measurand\": \"Energy.Active.Import.Register\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"value\": \"0\",\n" +
                "          \"unit\": \"varh\",\n" +
                "          \"measurand\": \"Energy.Reactive.Import.Register\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"timestamp\": \"2013-03-07T19:52:16Z\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"value\": \"20\",\n" +
                "          \"unit\": \"Wh\",\n" +
                "          \"measurand\": \"Energy.Active.Import.Register\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"value\": \"20\",\n" +
                "          \"unit\": \"varh\",\n" +
                "          \"measurand\": \"Energy.Reactive.Import.Register\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        WebSocket webSocket = getMockWebSocket();
        handler.handleRequest(CHARGING_STATION_ID, token, requestPayload, webSocket);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(webSocket).write(argumentCaptor.capture());
        String response = argumentCaptor.getValue();
        assertNotNull(response);
    }

}
