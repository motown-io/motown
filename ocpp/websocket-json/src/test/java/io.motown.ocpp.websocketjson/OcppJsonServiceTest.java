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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.*;
import io.motown.ocpp.websocketjson.request.handler.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class OcppJsonServiceTest {

    private OcppJsonService service;

    private DomainService domainService;

    private Gson gson;

    private WebSocket mockWebSocket;

    @Before
    public void setup() {
        gson = getGson();

        domainService = mock(DomainService.class);

        mockWebSocket = getMockWebSocket();

        service = new OcppJsonService();
        service.setDomainService(domainService);
        service.setGson(gson);
        service.setSchemaValidator(new SchemaValidator());
        service.setWampMessageParser(new WampMessageParser(gson));
        service.addWebSocket(CHARGING_STATION_ID.getId(), mockWebSocket);
    }

    @Test
    public void handleUnknownProcUri() {
        String request = String.format("[%d,%s,%s,%s]", WampMessage.CALL, UUID.randomUUID().toString(), "UnknownProcUri", "{\"request\":\"invalid\"}");

        // no exceptions
        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));
    }

    @Test
    public void handleBootNotification() throws IOException {
        BootNotificationRequest request = new BootNotificationRequest(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER);
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, BootNotificationRequestHandler.PROC_URI, request);
        BootNotificationRequestHandler handler = mock(BootNotificationRequestHandler.class);
        service.addRequestHandler(BootNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, wampMessage.getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidBootNotification() {
        BootNotificationRequest request = new BootNotificationRequest(null, CHARGING_STATION_MODEL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), BootNotificationRequestHandler.PROC_URI, request);
        BootNotificationRequestHandler handler = mock(BootNotificationRequestHandler.class);
        service.addRequestHandler(BootNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleDataTransfer() {
        DataTransferRequest request = new DataTransferRequest(CHARGING_STATION_VENDOR, "GetChargeInstruction", "");
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, DataTransferRequestHandler.PROC_URI, request);
        DataTransferRequestHandler handler = mock(DataTransferRequestHandler.class);
        service.addRequestHandler(DataTransferRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, wampMessage.getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidDataTransfer() {
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), DataTransferRequestHandler.PROC_URI, new DataTransferRequest(null, "GetChargeInstruction", ""));
        DataTransferRequestHandler handler = mock(DataTransferRequestHandler.class);
        service.addRequestHandler(DataTransferRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleDiagnosticsStatusNotification() {
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, DiagnosticsStatusNotificationRequestHandler.PROC_URI, new DiagnosticsStatusNotificationRequest(DiagnosticsStatus.UPLOADED));
        DiagnosticsStatusNotificationRequestHandler handler = mock(DiagnosticsStatusNotificationRequestHandler.class);
        service.addRequestHandler(DiagnosticsStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, gson.toJson(wampMessage.getPayload()), mockWebSocket);
    }

    @Test
    public void handleInvalidDiagnosticsStatusNotification() {
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), DiagnosticsStatusNotificationRequestHandler.PROC_URI, new DiagnosticsStatusNotificationRequest(null));
        DiagnosticsStatusNotificationRequestHandler handler = mock(DiagnosticsStatusNotificationRequestHandler.class);
        service.addRequestHandler(DiagnosticsStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleFirmwareStatusNotification() {
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, FirmwareStatusNotificationRequestHandler.PROC_URI, new FirmwareStatusNotificationRequest(FirmwareStatus.DOWNLOADED));
        FirmwareStatusNotificationRequestHandler handler = mock(FirmwareStatusNotificationRequestHandler.class);
        service.addRequestHandler(FirmwareStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, gson.toJson(wampMessage.getPayload()), mockWebSocket);
    }

    @Test
    public void handleInvalidFirmwareStatusNotification() {
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), FirmwareStatusNotificationRequestHandler.PROC_URI, new FirmwareStatusNotificationRequest(null));
        FirmwareStatusNotificationRequestHandler handler = mock(FirmwareStatusNotificationRequestHandler.class);
        service.addRequestHandler(FirmwareStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleHeartbeat() {
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, HeartbeatRequestHandler.PROC_URI, new HeartbeatRequest());
        HeartbeatRequestHandler handler = mock(HeartbeatRequestHandler.class);
        service.addRequestHandler(HeartbeatRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, gson.toJson(wampMessage.getPayload()), mockWebSocket);
    }

    @Test
    public void handleMeterValues() {
        String callId = UUID.randomUUID().toString();
        List<MeterValue.Value> values = new ArrayList<>();
        values.add(new MeterValue.Value("100", "Transaction.Begin", "SignedData", "Power.Active.Export", "Outlet", "kWh"));
        List<MeterValue> meterValues = new ArrayList<>();
        meterValues.add(new MeterValue(new Date(), values));
        MeterValuesRequest request = new MeterValuesRequest(EVSE_ID.getNumberedId(), TRANSACTION_NUMBER, meterValues);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, MeterValuesRequestHandler.PROC_URI, request);
        MeterValuesRequestHandler handler = mock(MeterValuesRequestHandler.class);
        service.addRequestHandler(MeterValuesRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidMeterValues() {
        MeterValuesRequest request = new MeterValuesRequest(EVSE_ID.getNumberedId(), TRANSACTION_NUMBER, new ArrayList<MeterValue>());
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), MeterValuesRequestHandler.PROC_URI, request);
        MeterValuesRequestHandler handler = mock(MeterValuesRequestHandler.class);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleStartTransaction() {
        String callId = UUID.randomUUID().toString();
        StartTransactionRequest request = new StartTransactionRequest(EVSE_ID.getNumberedId(), IDENTIFYING_TOKEN.getToken(), FIVE_MINUTES_AGO, METER_START, RESERVATION_ID.getNumber());
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, StartTransactionRequestHandler.PROC_URI, request);
        StartTransactionRequestHandler handler = mock(StartTransactionRequestHandler.class);
        service.addRequestHandler(StartTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidStartTransaction() {
        StartTransactionRequest request = new StartTransactionRequest(EVSE_ID.getNumberedId(), IDENTIFYING_TOKEN.getToken(), null, METER_START, RESERVATION_ID.getNumber());
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), StartTransactionRequestHandler.PROC_URI, request);
        StartTransactionRequestHandler handler = mock(StartTransactionRequestHandler.class);
        service.addRequestHandler(StartTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleStatusNotification() {
        String callId = UUID.randomUUID().toString();
        StatusNotificationRequest request = new StatusNotificationRequest(EVSE_ID.getNumberedId(), ChargePointStatus.AVAILABLE, ChargePointErrorCode.NO_ERROR, null, FIVE_MINUTES_AGO, CHARGING_STATION_VENDOR, null);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, StatusNotificationRequestHandler.PROC_URI, request);
        StatusNotificationRequestHandler handler = mock(StatusNotificationRequestHandler.class);
        service.addRequestHandler(StatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidStatusNotification() {
        StatusNotificationRequest request = new StatusNotificationRequest(EVSE_ID.getNumberedId(), null, ChargePointErrorCode.NO_ERROR, null, FIVE_MINUTES_AGO, CHARGING_STATION_VENDOR, null);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), StatusNotificationRequestHandler.PROC_URI, request);
        StatusNotificationRequestHandler handler = mock(StatusNotificationRequestHandler.class);
        service.addRequestHandler(StatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleStopTransaction() {
        String callId = UUID.randomUUID().toString();
        StopTransactionRequest request = new StopTransactionRequest(TRANSACTION_NUMBER, IDENTIFYING_TOKEN.getToken(), FIVE_MINUTES_AGO, METER_STOP, null);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, StopTransactionRequestHandler.PROC_URI, request);
        StopTransactionRequestHandler handler = mock(StopTransactionRequestHandler.class);
        service.addRequestHandler(StopTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidStopTransaction() {
        StopTransactionRequest request = new StopTransactionRequest(TRANSACTION_NUMBER, IDENTIFYING_TOKEN.getToken(), null, METER_STOP, null);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), StopTransactionRequestHandler.PROC_URI, request);
        StopTransactionRequestHandler handler = mock(StopTransactionRequestHandler.class);
        service.addRequestHandler(StopTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
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
