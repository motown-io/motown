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
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.handler.DataTransferRequestHandler;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.schema.generated.v15.*;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.atmosphere.websocket.WebSocket;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class OcppJsonServiceTest {

    private OcppJsonService service;

    private Gson gson;

    private WebSocket mockWebSocket;

    private String createExpectedMessageCall(MessageProcUri messageProcUri, Object payload) {
        return String.format("[%d,\"%s\",\"%s\",%s]", WampMessage.CALL, CORRELATION_TOKEN.getToken(), messageProcUri.toString(), gson.toJson(payload)).replaceAll("\\s+", "");
    }

    @Before
    public void setup() {
        gson = getGson();

        DomainService domainService = mock(DomainService.class);
        when(domainService.generateReservationIdentifier(any(ChargingStationId.class), anyString())).thenReturn(RESERVATION_ID);

        mockWebSocket = getMockWebSocket();

        service = new OcppJsonService();
        service.setDomainService(domainService);
        service.setGson(gson);
        service.setSchemaValidator(new SchemaValidator());
        service.setWampMessageParser(new WampMessageParser());
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
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, MessageProcUri.DATA_TRANSFER, request);
        DataTransferRequestHandler handler = mock(DataTransferRequestHandler.class);
        service.addRequestHandler(MessageProcUri.DATA_TRANSFER, handler);

        service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        verify(handler).handleRequest(CHARGING_STATION_ID, callId, wampMessage.getPayloadAsString(), mockWebSocket);
    }

    @Test
    public void handleInvalidIncomingRequest() {
        Datatransfer request = new Datatransfer();
        request.setVendorId(null);
        request.setMessageId("GetChargeInstruction");
        request.setData("");

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), MessageProcUri.DATA_TRANSFER, request);
        DataTransferRequestHandler handler = mock(DataTransferRequestHandler.class);
        service.addRequestHandler(MessageProcUri.DATA_TRANSFER, handler);

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
    public void closeWebSocketWhenNewSocketOverwritesExisting() {
        WebSocket webSocket = getMockWebSocket();
        service.addWebSocket(CHARGING_STATION_ID.getId(), webSocket);
        WebSocket newWebSocket = getMockWebSocket();
        service.addWebSocket(CHARGING_STATION_ID.getId(), newWebSocket);

        verify(webSocket).close();
    }

    @Test
    public void removeWebSocketFromServiceVerifyNoCalls() throws IOException {
        WebSocket webSocket = getMockWebSocket();
        service.addWebSocket(CHARGING_STATION_ID.getId(), webSocket);
        service.removeWebSocket(CHARGING_STATION_ID.getId());

        // using 'cancelReservation' to trigger a write on the socket.. which should not occur
        service.cancelReservation(CHARGING_STATION_ID, RESERVATION_ID, CORRELATION_TOKEN);
        Cancelreservation requestPayload = new Cancelreservation();
        requestPayload.setReservationId(RESERVATION_ID.getNumber());
        String expectedMessage = createExpectedMessageCall(MessageProcUri.CANCEL_RESERVATION, requestPayload);

        verify(webSocket, never()).write(expectedMessage);
    }

    @Test
    public void remoteStartTransactionRequest() throws IOException {
        service.remoteStartTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void changeConfigurationRequest() throws IOException {
        service.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void getConfigurationRequest() throws IOException {
        service.getConfiguration(CHARGING_STATION_ID, CONFIGURATION_SPECIFIC_KEYS);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void getDiagnosticsRequest() throws IOException {
        int numRetries = 3;
        int retryInterval = 1000;
        Date startTime = new Date();
        Date stopTime = new Date(DateTimeUtils.currentTimeMillis() - FIVE_MINUTES);
        service.getDiagnostics(CHARGING_STATION_ID, new DiagnosticsUploadSettings(FTP_LOCATION, numRetries, retryInterval, startTime, stopTime), CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void remoteStopTransactionRequest() throws IOException {
        service.remoteStopTransaction(CHARGING_STATION_ID, TRANSACTION_ID, CORRELATION_TOKEN);
        verify(mockWebSocket).write(anyString());
    }

    @Test
    public void softResetRequest() throws IOException {
        service.softReset(CHARGING_STATION_ID, CORRELATION_TOKEN);

        Reset requestPayload = new Reset();
        requestPayload.setType(Reset.Type.SOFT);

        String expectedMessage = createExpectedMessageCall(MessageProcUri.RESET, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }


    @Test
    public void hardResetRequest() throws IOException {
        service.hardReset(CHARGING_STATION_ID, CORRELATION_TOKEN);

        Reset requestPayload = new Reset();
        requestPayload.setType(Reset.Type.HARD);

        String expectedMessage = createExpectedMessageCall(MessageProcUri.RESET, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void updateFirmwareRequest() throws IOException, URISyntaxException {
        Date timestamp = new Date();
        Map attributes = Maps.<String, String>newHashMap();
        service.updateFirmware(CHARGING_STATION_ID, timestamp, attributes, FTP_LOCATION);

        Updatefirmware requestPayload = new Updatefirmware();
        requestPayload.setRetries(null);
        requestPayload.setRetryInterval(null);
        requestPayload.setLocation(new URI(FTP_LOCATION));
        requestPayload.setRetrieveDate(timestamp);

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(mockWebSocket).write(message.capture());
        assertTrue(message.getValue().contains(gson.toJson(requestPayload)));
    }

    @Test
    public void sendLocalListRequest() throws IOException {
        Set<IdentifyingToken> list = Sets.newHashSet();
        String idTag = "044943121F1D80";
        TextualToken id = new TextualToken(idTag, IdentifyingToken.AuthenticationStatus.ACCEPTED, "MSP", "7007");
        list.add(id);

        int listVersion = 1;
        String listHash = "";
        service.sendLocalList(CHARGING_STATION_ID, AuthorizationListUpdateType.FULL, list, listVersion, listHash, CORRELATION_TOKEN);

        //Validate result
        Sendlocallist requestPayload = new Sendlocallist();
        requestPayload.setUpdateType(Sendlocallist.UpdateType.FULL);
        List<LocalAuthorisationList> localAuthorizationList = Lists.newArrayList();
        LocalAuthorisationList entry = new LocalAuthorisationList();
        entry.setIdTag(idTag);
        IdTagInfo_ info = new IdTagInfo_();
        info.setStatus(IdTagInfo_.Status.ACCEPTED);
        entry.setIdTagInfo(info);
        localAuthorizationList.add(entry);
        requestPayload.setLocalAuthorisationList(localAuthorizationList);
        requestPayload.setListVersion(listVersion);
        requestPayload.setHash(listHash);

        String expectedMessage = createExpectedMessageCall(MessageProcUri.SEND_LOCALLIST, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void getLocalListVersionRequest() throws IOException {
        service.getLocalListVersion(CHARGING_STATION_ID, CORRELATION_TOKEN);

        String expectedMessage = createExpectedMessageCall(MessageProcUri.GET_LOCALLIST_VERSION, new Getlocallistversion());
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void clearCacheRequest() throws IOException {
        service.clearCache(CHARGING_STATION_ID, CORRELATION_TOKEN);

        String expectedMessage = createExpectedMessageCall(MessageProcUri.CLEAR_CACHE, new Clearcache());
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void changeAvailabilityRequest() throws IOException {
        service.changeAvailability(CHARGING_STATION_ID, EVSE_ID, Changeavailability.Type.INOPERATIVE, CORRELATION_TOKEN);

        Changeavailability requestPayload = new Changeavailability();
        requestPayload.setConnectorId(EVSE_ID.getNumberedId());
        requestPayload.setType(Changeavailability.Type.INOPERATIVE);

        String expectedMessage = createExpectedMessageCall(MessageProcUri.CHANGE_AVAILABILITY, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void dataTransferRequest() throws IOException {
        service.dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_MESSAGE, CORRELATION_TOKEN);

        Datatransfer requestPayload = new Datatransfer();
        requestPayload.setVendorId(DATA_TRANSFER_MESSAGE.getVendorId());
        requestPayload.setMessageId(DATA_TRANSFER_MESSAGE.getMessageId());
        requestPayload.setData(DATA_TRANSFER_MESSAGE.getData());

        String expectedMessage = createExpectedMessageCall(MessageProcUri.DATA_TRANSFER, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void reserveNowRequest() throws IOException {
        Date expiryDate = new Date(DateTimeUtils.currentTimeMillis() + FIVE_MINUTES);

        service.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, null, expiryDate, CORRELATION_TOKEN);

        Reservenow requestPayload = new Reservenow();
        requestPayload.setReservationId(RESERVATION_ID.getNumber());
        requestPayload.setIdTag(IDENTIFYING_TOKEN.getToken());
        requestPayload.setExpiryDate(expiryDate);
        requestPayload.setConnectorId(EVSE_ID.getNumberedId());

        String expectedMessage = createExpectedMessageCall(MessageProcUri.RESERVE_NOW, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }

    @Test
    public void cancelReservation() throws IOException {
        service.cancelReservation(CHARGING_STATION_ID, RESERVATION_ID, CORRELATION_TOKEN);

        Cancelreservation requestPayload = new Cancelreservation();
        requestPayload.setReservationId(RESERVATION_ID.getNumber());

        String expectedMessage = createExpectedMessageCall(MessageProcUri.CANCEL_RESERVATION, requestPayload);
        verify(mockWebSocket).write(expectedMessage);
    }

}
