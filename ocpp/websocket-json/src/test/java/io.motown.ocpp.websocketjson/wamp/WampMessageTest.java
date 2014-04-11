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
package io.motown.ocpp.websocketjson.wamp;

import com.google.gson.Gson;
import io.motown.ocpp.websocketjson.MessageProcUri;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static org.junit.Assert.assertEquals;

public class WampMessageTest {

    private Gson gson;

    @Before
    public void setup() {
        gson = getGson();
    }

    @Test
    public void constructCall() {
        String callId = UUID.randomUUID().toString();
        MessageProcUri procUri = MessageProcUri.BOOT_NOTIFICATION;
        String payload = "";
        List<Object> params = new LinkedList<>();
        params.add((double) WampMessage.CALL);
        params.add(callId);
        params.add(procUri);
        params.add(payload);

        WampMessage wampMessage = new WampMessage(params);

        assertEquals(WampMessage.CALL, wampMessage.getMessageType());
        assertEquals(callId, wampMessage.getCallId());
        assertEquals(procUri, wampMessage.getProcUri());
        assertEquals(payload, wampMessage.getPayload());
    }

    @Test
    public void constructCallResult() {
        String callId = UUID.randomUUID().toString();
        String payload = "";
        List<Object> params = new LinkedList<>();
        params.add((double) WampMessage.CALL_RESULT);
        params.add(callId);
        params.add(payload);

        WampMessage wampMessage = new WampMessage(params);

        assertEquals(WampMessage.CALL_RESULT, wampMessage.getMessageType());
        assertEquals(callId, wampMessage.getCallId());
        assertEquals(payload, wampMessage.getPayload());
    }

    @Test
    public void constructCallError() {
        String callId = UUID.randomUUID().toString();
        String errorCode = "1";
        String errorDescription = "desc";
        String errorDetails = "details";
        List<Object> params = new LinkedList<>();
        params.add((double) WampMessage.CALL_ERROR);
        params.add(callId);
        params.add(errorCode);
        params.add(errorDescription);
        params.add(errorDetails);

        WampMessage wampMessage = new WampMessage(params);

        assertEquals(WampMessage.CALL_ERROR, wampMessage.getMessageType());
        assertEquals(callId, wampMessage.getCallId());
        assertEquals(errorCode, wampMessage.getErrorCode());
        assertEquals(errorDescription, wampMessage.getErrorDescription());
        assertEquals(errorDetails, wampMessage.getErrorDetails());
    }

    @Test
    public void callToJson() {
        String callId = UUID.randomUUID().toString();
        MessageProcUri procUri = MessageProcUri.BOOT_NOTIFICATION;
        String payload = "ThisIsThePayload";
        WampMessage message = new WampMessage(WampMessage.CALL, callId, procUri, payload);

        String jsonMessage = message.toJson(gson);

        assertEquals(String.format("[%d,\"%s\",\"%s\",\"%s\"]", WampMessage.CALL, callId, procUri.toString(), payload), jsonMessage);
    }

    @Test
    public void callResultToJson() {
        String callId = UUID.randomUUID().toString();
        String payload = "ThisIsThePayload";
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, callId, payload);

        String jsonMessage = message.toJson(gson);

        assertEquals(String.format("[%d,\"%s\",\"%s\"]", WampMessage.CALL_RESULT, callId, payload), jsonMessage);
    }

    @Test
    public void callErrorToJson() {
        String callId = UUID.randomUUID().toString();
        String errorCode = "ErrorCode";
        String errorDescription = "ErrorDescription";
        String errorDetails = "ErrorDetails";
        WampMessage message = new WampMessage(WampMessage.CALL_ERROR, callId, errorCode, errorDescription, errorDetails);

        String jsonMessage = message.toJson(gson);

        assertEquals(String.format("[%d,\"%s\",\"%s\",\"%s\",\"%s\"]", WampMessage.CALL_ERROR, callId, errorCode, errorDescription, errorDetails), jsonMessage);
    }

}
