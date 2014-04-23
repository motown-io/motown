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
import io.motown.domain.api.chargingstation.AuthorizationListUpdateType;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.SendlocallistResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static org.mockito.Mockito.*;

public class SendLocalListResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private String token;
    private CorrelationToken correlationToken;
    private SendLocalListResponseHandler handler;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);

        token = UUID.randomUUID().toString();
        correlationToken = new CorrelationToken(token);
        handler = new SendLocalListResponseHandler(LIST_VERSION, AuthorizationListUpdateType.FULL, IDENTIFYING_TOKENS, correlationToken);
    }

    @Test
    public void handleAcceptedResponse() {
        SendlocallistResponse payload = new SendlocallistResponse();
        payload.setStatus(SendlocallistResponse.Status.ACCEPTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).authorizationListChange(CHARGING_STATION_ID, LIST_VERSION, AuthorizationListUpdateType.FULL, IDENTIFYING_TOKENS, correlationToken, ADD_ON_IDENTITY);
    }

    @Test
    public void handleFailedResponse() {
        SendlocallistResponse payload = new SendlocallistResponse();
        payload.setStatus(SendlocallistResponse.Status.FAILED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

    @Test
    public void handleNotSupportedResponse() {
        SendlocallistResponse payload = new SendlocallistResponse();
        payload.setStatus(SendlocallistResponse.Status.NOT_SUPPORTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

    @Test
    public void handleHashErrorResponse() {
        SendlocallistResponse payload = new SendlocallistResponse();
        payload.setStatus(SendlocallistResponse.Status.HASH_ERROR);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

    @Test
    public void handleVersionMismatchResponse() {
        SendlocallistResponse payload = new SendlocallistResponse();
        payload.setStatus(SendlocallistResponse.Status.VERSION_MISMATCH);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

}
