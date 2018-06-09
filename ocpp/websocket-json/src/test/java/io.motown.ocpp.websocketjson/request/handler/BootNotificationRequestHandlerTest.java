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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.utils.AttributeMap;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.WebSocketWrapper;
import io.motown.ocpp.websocketjson.schema.generated.v15.Bootnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.BootnotificationResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.atmosphere.websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ADD_ON_IDENTITY;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocketWrapper;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BootNotificationRequestHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private static final int HEARTBEAT_INTERVAL = 1200;
    private static final Date NOW = new Date();

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);
    }

    @Test
    public void handleValidRequest() throws IOException {
        when(domainService.bootChargingStation(any(ChargingStationId.class), anyString(), any(AttributeMap.class), any(AddOnIdentity.class)))
                .thenReturn(new BootChargingStationResult(true, HEARTBEAT_INTERVAL, NOW));

        WampMessage response = handleRequest();
        assertNotNull(response);
        assertTrue(response.toJson(gson).contains(BootnotificationResponse.Status.ACCEPTED.toString()));
    }

    @Test
    public void handleInvalidRequest() throws IOException {
        when(domainService.bootChargingStation(any(ChargingStationId.class), anyString(), any(AttributeMap.class), any(AddOnIdentity.class)))
                .thenReturn(new BootChargingStationResult(false, HEARTBEAT_INTERVAL, NOW));

        WampMessage response = handleRequest();

        assertNotNull(response);
        assertTrue(response.toJson(gson).contains(BootnotificationResponse.Status.REJECTED.toString()));
    }

    private WampMessage handleRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        BootNotificationRequestHandler handler = new BootNotificationRequestHandler(gson, domainService, ADD_ON_IDENTITY, null);

        Bootnotification requestPayload = new Bootnotification();
        requestPayload.setChargePointVendor("DBT");
        requestPayload.setChargePointModel("NQC-ACDC");
        requestPayload.setChargePointSerialNumber("gir.vat.mx.000e48");
        requestPayload.setChargeBoxSerialNumber("gir.vat.mx.000e48");
        requestPayload.setFirmwareVersion("1.0.49");
        requestPayload.setIccid("");
        requestPayload.setImsi("");
        requestPayload.setMeterType("DBT NQC-ACDC");
        requestPayload.setMeterSerialNumber("gir.vat.mx.000e48");

        WebSocketWrapper webSocketWrapper = getMockWebSocketWrapper();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocketWrapper);

        ArgumentCaptor<WampMessage> argumentCaptor = ArgumentCaptor.forClass(WampMessage.class);
        verify(webSocketWrapper).sendResultMessage(argumentCaptor.capture());
        return argumentCaptor.getValue();

    }

}
