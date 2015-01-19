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
import io.motown.ocpp.websocketjson.schema.generated.v15.DatatransferResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ADD_ON_IDENTITY;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static org.mockito.Mockito.*;

public class DataTransferResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private String token;
    private CorrelationToken correlationToken;
    private DataTransferResponseHandler handler;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);

        correlationToken = new CorrelationToken();
        token = correlationToken.getToken();
        handler = new DataTransferResponseHandler(correlationToken);
    }

    @Test
    public void handleAcceptedResponse() {
        String data = "response data";
        DatatransferResponse payload = new DatatransferResponse();
        payload.setStatus(DatatransferResponse.Status.ACCEPTED);
        payload.setData(data);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).informDataTransferResponse(CHARGING_STATION_ID, data, correlationToken, ADD_ON_IDENTITY);
    }

    @Test
    public void handleAcceptedResponseNoData() {
        DatatransferResponse payload = new DatatransferResponse();
        payload.setStatus(DatatransferResponse.Status.ACCEPTED);
        payload.setData(null);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

    @Test
    public void handleRejectedResponse() {
        DatatransferResponse payload = new DatatransferResponse();
        payload.setStatus(DatatransferResponse.Status.REJECTED);
        payload.setData("");
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

    @Test
    public void handleUnknownMessageIdResponse() {
        DatatransferResponse payload = new DatatransferResponse();
        payload.setStatus(DatatransferResponse.Status.UNKNOWN_MESSAGE_ID);
        payload.setData("");
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }

    @Test
    public void handleUnknownVendorIdResponse() {
        DatatransferResponse payload = new DatatransferResponse();
        payload.setStatus(DatatransferResponse.Status.UNKNOWN_VENDOR_ID);
        payload.setData("");
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyNoMoreInteractions(domainService);
    }
}
