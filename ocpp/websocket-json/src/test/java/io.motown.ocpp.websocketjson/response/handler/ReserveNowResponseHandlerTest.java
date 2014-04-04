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
package io.motown.ocpp.websocketjson.response.handler;

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReserveNowResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);
    }

    @Test
    public void handleValidResponse() {
        String token = UUID.randomUUID().toString();
        CorrelationToken correlationToken = new CorrelationToken(token);
        ReserveNowResponseHandler handler = new ReserveNowResponseHandler(correlationToken);

        String responseMessage = "[%d,\"%s\",{\n" +
                "  \"status\": \"Accepted\"\n" +
                "}]";

        WampMessage message = new WampMessageParser(gson).parseMessage(new StringReader(String.format(responseMessage, WampMessage.CALL_RESULT, token)));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, null);

        verify(domainService).informRequestResult(CHARGING_STATION_ID, RequestResult.SUCCESS, correlationToken, "");
    }

}
