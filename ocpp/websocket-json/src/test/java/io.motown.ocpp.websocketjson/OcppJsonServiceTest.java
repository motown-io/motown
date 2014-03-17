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

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.BootNotificationRequest;
import io.motown.ocpp.websocketjson.request.chargingstation.DataTransferRequest;
import io.motown.ocpp.websocketjson.request.chargingstation.DiagnosticsStatus;
import io.motown.ocpp.websocketjson.request.chargingstation.FirmwareStatus;
import io.motown.ocpp.websocketjson.response.centralsystem.DataTransferStatus;
import io.motown.ocpp.websocketjson.response.centralsystem.RegistrationStatus;
import io.motown.ocpp.websocketjson.response.chargingstation.UnlockStatus;
import io.motown.ocpp.websocketjson.response.handler.ResponseHandler;
import io.motown.ocpp.websocketjson.response.handler.UnlockConnectorResponseHandler;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.atmosphere.websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.DATE_FORMAT;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OcppJsonServiceTest {

    private OcppJsonService service;

    private SchemaValidator schemaValidator;

    private DomainService domainService;

    private Gson gson;

    @Before
    public void setup() {
        schemaValidator = mock(SchemaValidator.class);
        // for this tests all requests are valid
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(true);

        gson = getGson();

        domainService = mock(DomainService.class);

        service = new OcppJsonService();
        service.setDomainService(domainService);
        service.setGson(gson);
        service.setSchemaValidator(schemaValidator);
        service.setWampMessageParser(new WampMessageParser(gson));
    }

    @Test
    public void handleUnknownProcUri() {
        String request = String.format("[%d,%s,%s,%s]", WampMessage.CALL, UUID.randomUUID().toString(), "UnknownProcUri", "{\"request\":\"invalid\"}");

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertNull(response);
    }

    @Test
    public void handleBootNotification() {
        BootNotificationRequest request = new BootNotificationRequest(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER);
        Date now = new Date();
        int heartbeatInterval = 900;
        BootChargingStationResult bootResult = new BootChargingStationResult(true, heartbeatInterval, now);
        when(domainService.bootChargingStation(CHARGING_STATION_ID, null, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, OcppJsonService.PROTOCOL_IDENTIFIER, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER))
                .thenReturn(bootResult);
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, "BootNotification", request);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertEquals(String.format("[%d,\"%s\",{\"status\":\"%s\",\"currentTime\":\"%s\",\"heartbeatInterval\":%d}]", WampMessage.CALL_RESULT, callId, RegistrationStatus.ACCEPTED.value(), new SimpleDateFormat(DATE_FORMAT).format(now), heartbeatInterval), response);
    }

    @Test
    public void handleInvalidBootNotification() {
        BootNotificationRequest request = new BootNotificationRequest(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), "BootNotification", request);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertNull(response);
    }

    @Test
    public void handleDataTransfer() {
        String messageId = "GetChargeInstruction";
        String data = "";
        DataTransferRequest request = new DataTransferRequest(CHARGING_STATION_VENDOR, messageId, data);
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, "DataTransfer", request);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertEquals(String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, callId, DataTransferStatus.ACCEPTED.value()), response);
    }

    @Test
    public void handleInvalidDataTransfer() {
        DataTransferRequest request = new DataTransferRequest(CHARGING_STATION_VENDOR, "GetChargeInstruction", "");
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), "DataTransfer", request);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertNull(response);
    }

    @Test
    public void handleDiagnosticsStatusNotification() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"DiagnosticsStatusNotification\",{\"status\":\"%s\"}]", WampMessage.CALL, callId, DiagnosticsStatus.UPLOADED.value());

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertEquals(String.format("[%d,\"%s\",{}]", WampMessage.CALL_RESULT, callId), response);
    }

    @Test
    public void handleInvalidDiagnosticsStatusNotification() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"DiagnosticsStatusNotification\",{\"status\":\"%s\"}]", WampMessage.CALL, callId, DiagnosticsStatus.UPLOADED);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertNull(response);
    }

    @Test
    public void handleFirmwareStatusNotification() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"FirmwareStatusNotification\",{\"status\":\"%s\"}]", WampMessage.CALL, callId, FirmwareStatus.DOWNLOADED.value());

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertEquals(String.format("[%d,\"%s\",{}]", WampMessage.CALL_RESULT, callId), response);
    }

    @Test
    public void handleInvalidFirmwareStatusNotification() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"FirmwareStatusNotification\",{\"status\":\"%s\"}]", WampMessage.CALL, callId, FirmwareStatus.DOWNLOADED);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertNull(response);
    }

    @Test
    public void handleHeartbeat() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"Heartbeat\",{}]", WampMessage.CALL, callId);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

//        assertNotNull(gson.fromJson(new WampMessageParser(gson).parseMessage(new StringReader(response)).getPayloadAsString(), HeartbeatResponse.class).getCurrentTime());
        assertNotNull(response);
    }

    @Test
    public void handleInvalidHeartbeat() {
        String request = String.format("[%d,\"%s\",\"Heartbeat\",[]]", WampMessage.CALL, UUID.randomUUID().toString());
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertNull(response);
    }

    @Test
    public void unlockEvseRequestVerifySocketWrite() throws IOException {
        WebSocket webSocket = getMockWebSocket();
        service.addWebSocket(CHARGING_STATION_ID.getId(), webSocket);
        service.unlockEvse(CHARGING_STATION_ID, EVSE_ID, new CorrelationToken());

        verify(webSocket).write(anyString());
    }

    @Test
    public void unlockEvseRequestNoSocket() throws IOException {
        // no exception
        service.unlockEvse(CHARGING_STATION_ID, EVSE_ID, new CorrelationToken());
    }

    @Test
    public void unlockEvseRequestSocketIoExceptionNoExceptionThrown() throws IOException {
        WebSocket webSocket = getMockWebSocket();
        when(webSocket.write(anyString())).thenThrow(new IOException());
        service.addWebSocket(CHARGING_STATION_ID.getId(), webSocket);

        // no exception
        service.unlockEvse(CHARGING_STATION_ID, EVSE_ID, new CorrelationToken());
    }

    @Test
    public void handleUnlockEvseResponse() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, callId, UnlockStatus.ACCEPTED.value());
        ResponseHandler responseHandler = mock(UnlockConnectorResponseHandler.class);
        service.addResponseHandler(callId, responseHandler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        verify(responseHandler).handle(CHARGING_STATION_ID, new WampMessageParser(gson).parseMessage(new StringReader(request)), gson, domainService);
    }

    @Test
    public void handleUnlockEvseResponseNoHandler() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, callId, UnlockStatus.ACCEPTED.value());

        // no exceptions
        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));
    }

}
