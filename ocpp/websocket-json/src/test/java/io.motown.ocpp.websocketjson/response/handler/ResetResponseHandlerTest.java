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
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.ResetResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ADD_ON_IDENTITY;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ResetResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private String token;
    private CorrelationToken correlationToken;
    private ResetResponseHandler handler;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);

        correlationToken = new CorrelationToken();
        token = correlationToken.getToken();
        handler = new ResetResponseHandler(correlationToken);
    }

    @Test
    public void handleAcceptedResponse() {
        ResetResponse payload = new ResetResponse();
        payload.setStatus(ResetResponse.Status.ACCEPTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyZeroInteractions(domainService);
    }

    @Test
    public void handleRejectedResponse() {
        ResetResponse payload = new ResetResponse();
        payload.setStatus(ResetResponse.Status.REJECTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyZeroInteractions(domainService);
    }

}
