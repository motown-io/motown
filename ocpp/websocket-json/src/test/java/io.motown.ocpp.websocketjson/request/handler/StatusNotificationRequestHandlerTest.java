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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.StatusNotification;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.Statusnotification;
import org.atmosphere.websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StatusNotificationRequestHandlerTest {

    private Gson gson;

    private DomainService domainService;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);
    }

    @Test
    public void handleValidRequest() throws IOException {
        Statusnotification requestPayload = new Statusnotification();
        requestPayload.setConnectorId(EVSE_ID.getNumberedId());
        requestPayload.setStatus(Statusnotification.Status.AVAILABLE);
        requestPayload.setErrorCode(Statusnotification.ErrorCode.NO_ERROR);
        requestPayload.setInfo("");
        requestPayload.setTimestamp(new Date());
        requestPayload.setVendorId("");
        requestPayload.setVendorErrorCode("");

        String response = handleRequest(requestPayload);
        verify(domainService).statusNotification(notNull(ChargingStationId.class), any(StatusNotification.class), any(AddOnIdentity.class));
        assertNotNull(response);
    }

    @Test
    public void handleMissingTimestampRequest() throws IOException {
        Statusnotification requestPayload = new Statusnotification();
        requestPayload.setConnectorId(2);
        requestPayload.setStatus(Statusnotification.Status.AVAILABLE);
        requestPayload.setErrorCode(Statusnotification.ErrorCode.NO_ERROR);
        requestPayload.setInfo("");
        requestPayload.setTimestamp(null);
        requestPayload.setVendorId("");
        requestPayload.setVendorErrorCode("");

        String response = handleRequest(requestPayload);
        //In case the timestamp is missing it has to be created ('time of receipt')
        verify(domainService).statusNotification(notNull(ChargingStationId.class), any(StatusNotification.class), any(AddOnIdentity.class));
        assertNotNull(response);
    }

    private String handleRequest(Statusnotification requestPayload) throws IOException {
        String token = UUID.randomUUID().toString();
        StatusNotificationRequestHandler handler = new StatusNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY);

        WebSocket webSocket = getMockWebSocket();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocket);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(webSocket).write(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

}
