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

import io.motown.ocpp.websocketjson.schema.MessageProcUri;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WampMessageParserTest {

    private static final String CALL_ID = "1234";

    WampMessageParser parser;

    @Before
    public void setup() {
        parser = new WampMessageParser();
    }

    @Test
    public void processRequest() throws IOException {
        String payload = "{\"connectorId\":2,\"transactionId\":32201,\"values\":[{\"timestamp\":\"2013-03-07T16:52:16Z\",\"values\":[{\"value\":\"0\",\"unit\":\"Wh\",\"measurand\":\"Energy.Active.Import.Register\"},{\"value\":\"0\",\"unit\":\"varh\",\"measurand\":\"Energy.Reactive.Import.Register\"}]},{\"timestamp\":\"2013-03-07T19:52:16Z\",\"values\":[{\"value\":\"20\",\"unit\":\"Wh\",\"measurand\":\"Energy.Active.Import.Register\"},{\"value\":\"20\",\"unit\":\"varh\",\"measurand\":\"Energy.Reactive.Import.Register\"}]}]}";
        Reader reader = new StringReader(String.format("[%d,\"%s\",\"%s\",%s]", WampMessage.CALL, CALL_ID, MessageProcUri.METERVALUES.toString(), payload));

        WampMessage wampMessage = parser.parseMessage(reader);

        assertEquals(WampMessage.CALL, wampMessage.getMessageType());
        assertEquals(CALL_ID, wampMessage.getCallId());
        assertEquals(MessageProcUri.METERVALUES, wampMessage.getProcUri());
        assertNotNull(wampMessage.getPayload());
    }

    @Test
    public void processRequestWithSpacesInWeirdPlaces() throws IOException {
        String payload = "{\"connectorId\":2,\"transactionId\":32201,\"values\":[{\"timestamp\":\"2013-03-07T16:52:16Z\",\"values\":[{\"value\":\"0\",\"unit\":\"Wh\",\"measurand\":\"Energy.Active.Import.Register\"},{\"value\":\"0\",\"unit\":\"varh\",\"measurand\":\"Energy.Reactive.Import.Register\"}]},{\"timestamp\":\"2013-03-07T19:52:16Z\",\"values\":[{\"value\":\"20\",\"unit\":\"Wh\",\"measurand\":\"Energy.Active.Import.Register\"},{\"value\":\"20\",\"unit\":\"varh\",\"measurand\":\"Energy.Reactive.Import.Register\"}]}]}";
        Reader reader = new StringReader(String.format(" [ %d,\" %s\",\"%s\",%s]", WampMessage.CALL, CALL_ID, MessageProcUri.METERVALUES.toString(), payload));

        WampMessage wampMessage = parser.parseMessage(reader);

        assertEquals(WampMessage.CALL, wampMessage.getMessageType());
        assertEquals(CALL_ID, wampMessage.getCallId());
        assertEquals(MessageProcUri.METERVALUES, wampMessage.getProcUri());
        assertNotNull(wampMessage.getPayload());
    }

    @Test
    public void processResponse() throws IOException {
        Reader reader = new StringReader(String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, CALL_ID, "Accepted"));

        WampMessage wampMessage = parser.parseMessage(reader);

        assertEquals(WampMessage.CALL_RESULT, wampMessage.getMessageType());
        assertEquals(CALL_ID, wampMessage.getCallId());
        assertNotNull(wampMessage.getPayload());
    }

    @Test
    public void processErrorResponse() throws IOException {
        String errorCode = "404";
        String errorDescription = "Something went wrong";
        String errorDetails = "No details available";
        Reader reader = new StringReader(String.format("[%d,\"%s\",\"%s\",\"%s\",\"%s\"]", WampMessage.CALL_ERROR, CALL_ID, errorCode, errorDescription, errorDetails));

        WampMessage wampMessage = parser.parseMessage(reader);

        assertEquals(WampMessage.CALL_ERROR, wampMessage.getMessageType());
        assertEquals(CALL_ID, wampMessage.getCallId());
        assertEquals(errorCode, wampMessage.getErrorCode());
        assertEquals(errorDescription, wampMessage.getErrorDescription());
        assertEquals(errorDetails, wampMessage.getErrorDetails());
    }

}
