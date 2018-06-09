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
import io.motown.ocpp.websocketjson.WebSocketWrapper;
import io.motown.ocpp.websocketjson.schema.generated.v15.Diagnosticsstatusnotification;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
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
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocketWrapper;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiagnosticsStatusNotificationRequestHandlerTest {

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
        DiagnosticsStatusNotificationRequestHandler handler = new DiagnosticsStatusNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY, null);

        Diagnosticsstatusnotification requestPayload = new Diagnosticsstatusnotification();
        requestPayload.setStatus(Diagnosticsstatusnotification.Status.UPLOADED);

        WebSocketWrapper webSocketWrapper = getMockWebSocketWrapper();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocketWrapper);

        ArgumentCaptor<WampMessage> argumentCaptor = ArgumentCaptor.forClass(WampMessage.class);
        verify(webSocketWrapper).sendResultMessage(argumentCaptor.capture());
        WampMessage response = argumentCaptor.getValue();
        assertNotNull(response);
    }

}
