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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.handler.DataTransferRequestHandler;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.schema.generated.v15.Datatransfer;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.atmosphere.websocket.WebSocket;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
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
    public void handleIncomingRequest() {
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
    public void handleInvalidIncomingRequest() {
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
    public void noResponseHandler() {
        String callId = UUID.randomUUID().toString();
        String request = createAcceptedCallResult(callId);

        // no exceptions
        service.handleMessage(CHARGING_STATION_ID, new StringReader(request));
    }

    @Test
    public void noSocket() throws IOException {
        // no exception
        service.unlockEvse(CHARGING_STATION_ID, EVSE_ID, new CorrelationToken());
    }

    @Test
    public void inCaseOfSocketIoExceptionNoExceptionThrown() throws IOException {
        WebSocket webSocket = getMockWebSocket();
        when(webSocket.write(anyString())).thenThrow(new IOException());
        service.addWebSocket(CHARGING_STATION_ID.getId(), webSocket);

        // no exception
        service.unlockEvse(CHARGING_STATION_ID, EVSE_ID, new CorrelationToken());
    }

    @Test
    public void unlockEvseRequest() throws IOException {
        WebSocket webSocket = getMockWebSocket();
        service.addWebSocket(CHARGING_STATION_ID.getId(), webSocket);
        service.unlockEvse(CHARGING_STATION_ID, EVSE_ID, new CorrelationToken());

        verify(webSocket).write(anyString());
    }

    @Test
    public void remoteStartTransactionRequest() throws IOException{
        service.remoteStartTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void changeConfigurationRequest() throws IOException{
        service.changeConfiguration(CHARGING_STATION_ID, "KVCBX_LANG", "NL", CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void getConfigurationRequest() throws IOException{
        service.getConfiguration(CHARGING_STATION_ID);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void getDiagnosticsRequest() throws IOException{
        int numRetries = 3;
        int retryInterval = 1000;
        Date startTime = new Date();
        Date stopTime = new Date(DateTimeUtils.currentTimeMillis() - FIVE_MINUTES);
        service.getDiagnostics(CHARGING_STATION_ID, numRetries, retryInterval, startTime, stopTime, FTP_LOCATION, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void remoteStopTransactionRequest() throws IOException{
        service.remoteStopTransaction(CHARGING_STATION_ID, TRANSACTION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void softResetRequest() throws IOException{
        service.softReset(CHARGING_STATION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void hardResetRequest() throws IOException{
        service.hardReset(CHARGING_STATION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void updateFirmwareRequest() throws IOException{
        service.updateFirmware(CHARGING_STATION_ID, new Date(), Maps.<String, String>newHashMap(), FTP_LOCATION);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void sendLocalListRequest() throws IOException{
        List<IdentifyingToken> list = Lists.newArrayList();
        String idTag = "044943121F1D80";
        TextualToken id = new TextualToken(idTag, IdentifyingToken.AuthenticationStatus.ACCEPTED);
        list.add(id);

        int listVersion = 1;
        String listHash = "";
        service.sendLocalList(CHARGING_STATION_ID, AuthorizationListUpdateType.FULL, list, listVersion, listHash, CORRELATION_TOKEN);

        String expectedMessage = String.format("[%d,\"%s\",\"%s\",{" +
                "  \"updateType\": \"Full\"," +
                "  \"listVersion\": 1.0," +
                "  \"localAuthorisationList\": [" +
                "    {" +
                "      \"idTag\": \"%s\"," +
                "      \"idTagInfo\": {" +
                "        \"status\": \"Accepted\"" +
                "      }" +
                "    }" +
                "  ]," +
                "  \"hash\": \"\"" +
                "}]", WampMessage.CALL, CORRELATION_TOKEN.getToken(), "SendLocalList", idTag)
                .replaceAll("\\s+", "");

        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void getLocalListVersionRequest() throws IOException{
        service.getLocalListVersion(CHARGING_STATION_ID, CORRELATION_TOKEN);

        String expectedMessage = String.format("[%d,\"%s\",\"%s\",{}]", WampMessage.CALL, CORRELATION_TOKEN.getToken(), "GetLocalListVersion").replaceAll("\\s+", "");

        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void clearCacheRequest() throws IOException{
        service.clearCache(CHARGING_STATION_ID, CORRELATION_TOKEN);

        String expectedMessage = String.format("[%d,\"%s\",\"%s\",{}]", WampMessage.CALL, CORRELATION_TOKEN.getToken(), "ClearCache").replaceAll("\\s+", "");

        verify(mockWebSocket).write(expectedMessage);
    }
}
