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
import io.motown.ocpp.websocketjson.schema.generated.v15.ReservenowResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReserveNowResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private String token;
    private CorrelationToken correlationToken;
    private ReserveNowResponseHandler handler;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);

        token = UUID.randomUUID().toString();
        correlationToken = new CorrelationToken(token);
        handler = new ReserveNowResponseHandler(RESERVATION_ID, EVSE_ID, new Date(), correlationToken);
    }

    @Test
    public void handleAcceptedResponse() {
        ReservenowResponse payload = new ReservenowResponse();
        payload.setStatus(ReservenowResponse.Status.ACCEPTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).informRequestResult(CHARGING_STATION_ID, RequestResult.SUCCESS, correlationToken, "", ADD_ON_IDENTITY);
    }

    @Test
    public void handleRejectedResponse() {
        ReservenowResponse payload = new ReservenowResponse();
        payload.setStatus(ReservenowResponse.Status.REJECTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).informRequestResult(CHARGING_STATION_ID, RequestResult.FAILURE, correlationToken, "", ADD_ON_IDENTITY);
    }

    @Test
    public void handleFaultedResponse() {
        ReservenowResponse payload = new ReservenowResponse();
        payload.setStatus(ReservenowResponse.Status.FAULTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).informRequestResult(CHARGING_STATION_ID, RequestResult.FAILURE, correlationToken, "", ADD_ON_IDENTITY);
    }

    @Test
    public void handleUnavailableResponse() {
        ReservenowResponse payload = new ReservenowResponse();
        payload.setStatus(ReservenowResponse.Status.UNAVAILABLE);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).informRequestResult(CHARGING_STATION_ID, RequestResult.FAILURE, correlationToken, "", ADD_ON_IDENTITY);
    }

    @Test
    public void handleOccupiedResponse() {
        ReservenowResponse payload = new ReservenowResponse();
        payload.setStatus(ReservenowResponse.Status.OCCUPIED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).informRequestResult(CHARGING_STATION_ID, RequestResult.FAILURE, correlationToken, "", ADD_ON_IDENTITY);
    }

}
