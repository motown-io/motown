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
import io.motown.domain.api.chargingstation.AuthorizationListUpdateType;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.DiagnosticsUploadSettings;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.handler.DataTransferRequestHandler;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.schema.generated.v15.Cancelreservation;
import io.motown.ocpp.websocketjson.schema.generated.v15.Changeavailability;
import io.motown.ocpp.websocketjson.schema.generated.v15.Clearcache;
import io.motown.ocpp.websocketjson.schema.generated.v15.Datatransfer;
import io.motown.ocpp.websocketjson.schema.generated.v15.Getlocallistversion;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo_;
import io.motown.ocpp.websocketjson.schema.generated.v15.LocalAuthorisationList;
import io.motown.ocpp.websocketjson.schema.generated.v15.Reservenow;
import io.motown.ocpp.websocketjson.schema.generated.v15.Reset;
import io.motown.ocpp.websocketjson.schema.generated.v15.Sendlocallist;
import io.motown.ocpp.websocketjson.schema.generated.v15.Updatefirmware;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageHandler;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class WebSocketWrapperTest {

    private Gson gson = new Gson();

    @Test
    public void isOpenShouldReturnWebSocketStatus() {
        WebSocket mockWebSocket = getMockWebSocket();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, mock(WampMessageHandler.class), gson);

        when(mockWebSocket.isOpen()).thenReturn(false);
        assertFalse(wrapper.isOpen());

        when(mockWebSocket.isOpen()).thenReturn(true);
        assertTrue(wrapper.isOpen());

        // null websocket == not open
        wrapper = new WebSocketWrapper(CHARGING_STATION_ID, null, mock(WampMessageHandler.class), gson);
        assertFalse(wrapper.isOpen());
    }

    @Test
    public void closeShouldCallWebSocketCloseIfSocketIsOpen() {
        WebSocket mockWebSocket = getMockWebSocket();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, mock(WampMessageHandler.class), gson);
        when(mockWebSocket.isOpen()).thenReturn(true);

        wrapper.close();

        verify(mockWebSocket).close();

        // no call to close if not open
        reset(mockWebSocket);
        when(mockWebSocket.isOpen()).thenReturn(false);
        wrapper.close();
        verify(mockWebSocket, times(0)).close();
    }

    @Test
    public void closeShouldNotFailOnNullSocket() {
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, null, mock(WampMessageHandler.class), gson);

        wrapper.close();

        // no exception thrown
    }

    @Test
    public void sendMessageShouldWriteToSocket() throws ChargePointCommunicationException, IOException {
        WebSocket mockWebSocket = getMockWebSocket();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, mock(WampMessageHandler.class), gson);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, "1", MessageProcUri.CANCEL_RESERVATION, "dummy payload");

        wrapper.sendMessage(wampMessage);

        verify(mockWebSocket).write(wampMessage.toJson(gson));
    }

    @Test
    public void sendMessageShouldCallMessageHandler() throws ChargePointCommunicationException {
        WebSocket mockWebSocket = getMockWebSocket();
        WampMessageHandler messageHandler = mock(WampMessageHandler.class);
        String callId = UUID.randomUUID().toString();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, messageHandler, gson);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, MessageProcUri.CANCEL_RESERVATION, "dummy payload");

        wrapper.sendMessage(wampMessage);

        verify(messageHandler).handleWampCall(CHARGING_STATION_ID.getId(), wampMessage.toJson(gson), callId);
    }

    @Test(expected = ChargePointCommunicationException.class)
    public void ioExceptionShouldResultInCommunicationExceptionOnSendMessage() throws IOException, ChargePointCommunicationException {
        WebSocket mockWebSocket = getMockWebSocket();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, mock(WampMessageHandler.class), gson);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, "1", MessageProcUri.CANCEL_RESERVATION, "dummy payload");
        when(mockWebSocket.write(anyString())).thenThrow(new IOException("dummy exception"));

        wrapper.sendMessage(wampMessage);
    }

    @Test
    public void sendResultMessageShouldWriteToSocket() throws IOException {
        WebSocket mockWebSocket = getMockWebSocket();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, mock(WampMessageHandler.class), gson);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL_RESULT, "1", MessageProcUri.AUTHORIZE, "dummy payload");

        wrapper.sendResultMessage(wampMessage);

        verify(mockWebSocket).write(wampMessage.toJson(gson));
    }

    @Test
    public void sendResultMessageShouldCallMessageHandler() {
        WebSocket mockWebSocket = getMockWebSocket();
        WampMessageHandler messageHandler = mock(WampMessageHandler.class);
        String callId = UUID.randomUUID().toString();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, messageHandler, gson);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL_RESULT, callId, MessageProcUri.START_TRANSACTION, "dummy payload");

        wrapper.sendResultMessage(wampMessage);

        verify(messageHandler).handleWampCallResult(CHARGING_STATION_ID.getId(), wampMessage.getProcUri().toString(), wampMessage.toJson(gson));
    }

    @Test
    public void ioExceptionShouldNotResultInCommunicationExceptionOnSendResultMessage() throws IOException {
        WebSocket mockWebSocket = getMockWebSocket();
        WebSocketWrapper wrapper = new WebSocketWrapper(CHARGING_STATION_ID, mockWebSocket, mock(WampMessageHandler.class), gson);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL_RESULT, "1", MessageProcUri.START_TRANSACTION, "dummy payload");
        when(mockWebSocket.write(anyString())).thenThrow(new IOException("dummy exception"));

        wrapper.sendResultMessage(wampMessage);

        // no exception
    }

}
