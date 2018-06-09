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
import io.motown.domain.api.chargingstation.FirmwareStatus;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.WebSocketWrapper;
import io.motown.ocpp.websocketjson.schema.generated.v15.Firmwarestatusnotification;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.atmosphere.websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ADD_ON_IDENTITY;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocketWrapper;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FirmwareStatusNotificationRequestHandlerTest {

    private Gson gson;

    private DomainService domainService;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);
    }

    @Test
    public void handleDownloadedRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        FirmwareStatusNotificationRequestHandler handler = new FirmwareStatusNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY, null);

        Firmwarestatusnotification requestPayload = new Firmwarestatusnotification();
        requestPayload.setStatus(Firmwarestatusnotification.Status.DOWNLOADED);

        WebSocketWrapper webSocketWrapper = getMockWebSocketWrapper();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocketWrapper);

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, FirmwareStatus.DOWNLOADED, ADD_ON_IDENTITY);
        verify(webSocketWrapper).sendResultMessage(any(WampMessage.class));
    }

    @Test
    public void handleDownloadFailedRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        FirmwareStatusNotificationRequestHandler handler = new FirmwareStatusNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY, null);

        Firmwarestatusnotification requestPayload = new Firmwarestatusnotification();
        requestPayload.setStatus(Firmwarestatusnotification.Status.DOWNLOAD_FAILED);

        WebSocketWrapper webSocketWrapper = getMockWebSocketWrapper();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocketWrapper);

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, FirmwareStatus.DOWNLOAD_FAILED, ADD_ON_IDENTITY);
        verify(webSocketWrapper).sendResultMessage(any(WampMessage.class));
    }

    @Test
    public void handleInstallationFailedRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        FirmwareStatusNotificationRequestHandler handler = new FirmwareStatusNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY, null);

        Firmwarestatusnotification requestPayload = new Firmwarestatusnotification();
        requestPayload.setStatus(Firmwarestatusnotification.Status.INSTALLATION_FAILED);

        WebSocketWrapper webSocketWrapper = getMockWebSocketWrapper();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocketWrapper);

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, FirmwareStatus.INSTALLATION_FAILED, ADD_ON_IDENTITY);
        verify(webSocketWrapper).sendResultMessage(any(WampMessage.class));
    }

    @Test
    public void handleInstalledRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        FirmwareStatusNotificationRequestHandler handler = new FirmwareStatusNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY, null);

        Firmwarestatusnotification requestPayload = new Firmwarestatusnotification();
        requestPayload.setStatus(Firmwarestatusnotification.Status.INSTALLED);

        WebSocketWrapper webSocketWrapper = getMockWebSocketWrapper();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocketWrapper);

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, FirmwareStatus.INSTALLED, ADD_ON_IDENTITY);
        verify(webSocketWrapper).sendResultMessage(any(WampMessage.class));
    }

}
