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
import io.motown.ocpp.websocketjson.request.handler.*;
import io.motown.ocpp.websocketjson.response.handler.*;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.schema.generated.v15.*;
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
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.*;
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
        Bootnotification request = new Bootnotification();
        request.setChargePointVendor(CHARGING_STATION_VENDOR);
        request.setChargePointModel(CHARGING_STATION_MODEL);
        request.setChargePointSerialNumber(CHARGING_STATION_SERIAL_NUMBER);
        request.setChargeBoxSerialNumber(CHARGE_BOX_SERIAL_NUMBER);
        request.setFirmwareVersion(FIRMWARE_VERSION);
        request.setIccid(ICCID);
        request.setImsi(IMSI);
        request.setMeterType(METER_TYPE);
        request.setMeterSerialNumber(METER_SERIAL_NUMBER);

        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, BootNotificationRequestHandler.PROC_URI, request);
        BootNotificationRequestHandler handler = mock(BootNotificationRequestHandler.class);
        service.addRequestHandler(BootNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, wampMessage.getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidBootNotification() {
        Bootnotification request = new Bootnotification();
        request.setChargePointVendor(null);
        request.setChargePointModel(CHARGING_STATION_MODEL);
        request.setChargePointSerialNumber(CHARGING_STATION_SERIAL_NUMBER);
        request.setChargeBoxSerialNumber(CHARGE_BOX_SERIAL_NUMBER);
        request.setFirmwareVersion(FIRMWARE_VERSION);
        request.setIccid(ICCID);
        request.setImsi(IMSI);
        request.setMeterType(METER_TYPE);
        request.setMeterSerialNumber(METER_SERIAL_NUMBER);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), BootNotificationRequestHandler.PROC_URI, request);
        BootNotificationRequestHandler handler = mock(BootNotificationRequestHandler.class);
        service.addRequestHandler(BootNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleDataTransfer() {
        Datatransfer request = new Datatransfer();
        request.setVendorId(CHARGING_STATION_VENDOR);
        request.setMessageId("GetChargeInstruction");
        request.setData("");

        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, DataTransferRequestHandler.PROC_URI, request);
        DataTransferRequestHandler handler = mock(DataTransferRequestHandler.class);
        service.addRequestHandler(DataTransferRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, wampMessage.getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidDataTransfer() {
        Datatransfer request = new Datatransfer();
        request.setVendorId(null);
        request.setMessageId("GetChargeInstruction");
        request.setData("");

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), DataTransferRequestHandler.PROC_URI, request);
        DataTransferRequestHandler handler = mock(DataTransferRequestHandler.class);
        service.addRequestHandler(DataTransferRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleDiagnosticsStatusNotification() {
        String callId = UUID.randomUUID().toString();
        Diagnosticsstatusnotification request = new Diagnosticsstatusnotification();
        request.setStatus(Diagnosticsstatusnotification.Status.UPLOADED);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, DiagnosticsStatusNotificationRequestHandler.PROC_URI, request);
        DiagnosticsStatusNotificationRequestHandler handler = mock(DiagnosticsStatusNotificationRequestHandler.class);
        service.addRequestHandler(DiagnosticsStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, gson.toJson(wampMessage.getPayload()), mockWebSocket);
    }

    @Test
    public void handleInvalidDiagnosticsStatusNotification() {
        Diagnosticsstatusnotification request = new Diagnosticsstatusnotification();
        request.setStatus(null);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), DiagnosticsStatusNotificationRequestHandler.PROC_URI, request);
        DiagnosticsStatusNotificationRequestHandler handler = mock(DiagnosticsStatusNotificationRequestHandler.class);
        service.addRequestHandler(DiagnosticsStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleFirmwareStatusNotification() {
        String callId = UUID.randomUUID().toString();
        Firmwarestatusnotification request =  new Firmwarestatusnotification();
        request.setStatus(Firmwarestatusnotification.Status.DOWNLOADED);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, FirmwareStatusNotificationRequestHandler.PROC_URI, request);
        FirmwareStatusNotificationRequestHandler handler = mock(FirmwareStatusNotificationRequestHandler.class);
        service.addRequestHandler(FirmwareStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, gson.toJson(wampMessage.getPayload()), mockWebSocket);
    }

    @Test
    public void handleInvalidFirmwareStatusNotification() {
        Firmwarestatusnotification request =  new Firmwarestatusnotification();
        request.setStatus(null);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), FirmwareStatusNotificationRequestHandler.PROC_URI, request);
        FirmwareStatusNotificationRequestHandler handler = mock(FirmwareStatusNotificationRequestHandler.class);
        service.addRequestHandler(FirmwareStatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleHeartbeat() {
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, HeartbeatRequestHandler.PROC_URI, new Heartbeat());
        HeartbeatRequestHandler handler = mock(HeartbeatRequestHandler.class);
        service.addRequestHandler(HeartbeatRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, gson.toJson(wampMessage.getPayload()), mockWebSocket);
    }

    @Test
    public void handleMeterValues() {
        String callId = UUID.randomUUID().toString();

        Value_ value = new Value_();
        value.setValue("100");
        value.setContext("Transaction.Begin");
        value.setFormat("SignedData");
        value.setMeasurand("Power.Active.Export");
        value.setLocation("Outlet");
        value.setUnit("kWh");

        List<Value_> values = new ArrayList<>();
        values.add(value);

        Value meterValue = new Value();
        meterValue.setTimestamp(new Date());
        meterValue.setValues(values);
        List<Value> meterValues = new ArrayList<>();
        meterValues.add(meterValue);

        Metervalues request = new Metervalues();
        request.setConnectorId((double) EVSE_ID.getNumberedId());
        request.setTransactionId((double) TRANSACTION_NUMBER);
        request.setValues(meterValues);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, MeterValuesRequestHandler.PROC_URI, request);
        MeterValuesRequestHandler handler = mock(MeterValuesRequestHandler.class);
        service.addRequestHandler(MeterValuesRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidMeterValues() {
        Metervalues request = new Metervalues();
        request.setConnectorId((double) EVSE_ID.getNumberedId());
        request.setTransactionId((double) TRANSACTION_NUMBER);
        request.setValues(new ArrayList<Value>());

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), MeterValuesRequestHandler.PROC_URI, request);
        MeterValuesRequestHandler handler = mock(MeterValuesRequestHandler.class);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleStartTransaction() {
        String callId = UUID.randomUUID().toString();
        Starttransaction request = new Starttransaction();
        request.setConnectorId((double) EVSE_ID.getNumberedId());
        request.setIdTag(IDENTIFYING_TOKEN.getToken());
        request.setTimestamp(FIVE_MINUTES_AGO);
        request.setMeterStart((double) METER_START);
        request.setReservationId((double) RESERVATION_ID.getNumber());

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, StartTransactionRequestHandler.PROC_URI, request);
        StartTransactionRequestHandler handler = mock(StartTransactionRequestHandler.class);
        service.addRequestHandler(StartTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidStartTransaction() {
        Starttransaction request = new Starttransaction();
        request.setConnectorId((double) EVSE_ID.getNumberedId());
        request.setIdTag(IDENTIFYING_TOKEN.getToken());
        request.setTimestamp(null);
        request.setMeterStart((double) METER_START);
        request.setReservationId((double) RESERVATION_ID.getNumber());

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), StartTransactionRequestHandler.PROC_URI, request);
        StartTransactionRequestHandler handler = mock(StartTransactionRequestHandler.class);
        service.addRequestHandler(StartTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleStatusNotification() {
        String callId = UUID.randomUUID().toString();
        Statusnotification request = new Statusnotification();
        request.setConnectorId((double) EVSE_ID.getNumberedId());
        request.setStatus(Statusnotification.Status.AVAILABLE);
        request.setErrorCode(Statusnotification.ErrorCode.NO_ERROR);
        request.setInfo(null);
        request.setTimestamp(FIVE_MINUTES_AGO);
        request.setVendorId(CHARGING_STATION_VENDOR);
        request.setVendorErrorCode(null);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, StatusNotificationRequestHandler.PROC_URI, request);
        StatusNotificationRequestHandler handler = mock(StatusNotificationRequestHandler.class);
        service.addRequestHandler(StatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidStatusNotification() {
        Statusnotification request = new Statusnotification();
        request.setConnectorId((double) EVSE_ID.getNumberedId());
        request.setStatus(null);
        request.setErrorCode(Statusnotification.ErrorCode.NO_ERROR);
        request.setInfo(null);
        request.setTimestamp(FIVE_MINUTES_AGO);
        request.setVendorId(CHARGING_STATION_VENDOR);
        request.setVendorErrorCode(null);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), StatusNotificationRequestHandler.PROC_URI, request);
        StatusNotificationRequestHandler handler = mock(StatusNotificationRequestHandler.class);
        service.addRequestHandler(StatusNotificationRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler, never()).handleRequest(any(ChargingStationId.class), anyString(), anyString(), any(WebSocket.class));
    }

    @Test
    public void handleStopTransaction() {
        String callId = UUID.randomUUID().toString();
        Stoptransaction request = new Stoptransaction();
        request.setTransactionId((double) TRANSACTION_NUMBER);
        request.setIdTag(IDENTIFYING_TOKEN.getToken());
        request.setTimestamp(FIVE_MINUTES_AGO);
        request.setMeterStop((double) METER_STOP);
        request.setTransactionData(null);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, StopTransactionRequestHandler.PROC_URI, request);
        StopTransactionRequestHandler handler = mock(StopTransactionRequestHandler.class);
        service.addRequestHandler(StopTransactionRequestHandler.PROC_URI, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, new WampMessageParser(gson).parseMessage(new StringReader(wampMessage.toJson(gson))).getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidStopTransaction() {
        Stoptransaction request = new Stoptransaction();
        request.setTransactionId((double) TRANSACTION_NUMBER);
        request.setIdTag(IDENTIFYING_TOKEN.getToken());
        request.setTimestamp(null);
        request.setMeterStop((double) METER_STOP);
        request.setTransactionData(null);

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
        String request = createAcceptedCallResult(callId);
        ResponseHandler responseHandler = mock(UnlockConnectorResponseHandler.class);
        service.addResponseHandler(callId, responseHandler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        verify(responseHandler).handle(CHARGING_STATION_ID, new WampMessageParser(gson).parseMessage(new StringReader(request)), gson, domainService);
    }

    @Test
    public void handleUnlockEvseResponseNoHandler() {
        String callId = UUID.randomUUID().toString();
        String request = createAcceptedCallResult(callId);

        // no exceptions
        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));
    }

    @Test
    public void performStartTransaction() throws IOException{
        service.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void handleStartTransactionResponse() {
        String callId = UUID.randomUUID().toString();
        String request = createAcceptedCallResult(callId);
        ResponseHandler responseHandler = mock(RemoteStartTransactionResponseHandler.class);
        service.addResponseHandler(callId, responseHandler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        verify(responseHandler).handle(CHARGING_STATION_ID, new WampMessageParser(gson).parseMessage(new StringReader(request)), gson, domainService);
    }

    @Test
    public void performStopTransaction() throws IOException{
        service.stopTransaction(CHARGING_STATION_ID, TRANSACTION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void handleStopTransactionResponse() {
        String callId = UUID.randomUUID().toString();
        String request = createAcceptedCallResult(callId);
        ResponseHandler responseHandler = mock(RemoteStopTransactionResponseHandler.class);
        service.addResponseHandler(callId, responseHandler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        verify(responseHandler).handle(CHARGING_STATION_ID, new WampMessageParser(gson).parseMessage(new StringReader(request)), gson, domainService);
    }

    @Test
    public void performSoftReset() throws IOException{
        service.softReset(CHARGING_STATION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void performHardReset() throws IOException{
        service.hardReset(CHARGING_STATION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void handleResetResponse() {
        String callId = UUID.randomUUID().toString();
        String request = createAcceptedCallResult(callId);
        ResponseHandler responseHandler = mock(ResetResponseHandler.class);
        service.addResponseHandler(callId, responseHandler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        verify(responseHandler).handle(CHARGING_STATION_ID, new WampMessageParser(gson).parseMessage(new StringReader(request)), gson, domainService);
    }

}
