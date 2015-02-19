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
package io.motown.ocpp.websocketjson.request.handler;

import com.google.gson.Gson;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.ocpp.websocketjson.OcppWebSocketRequestHandler;
import io.motown.ocpp.websocketjson.schema.generated.v15.Starttransaction;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.OCPPJ_RESERVATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StartTransactionRequestHandlerTest {

    private Gson gson;

    private DomainService domainService;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);
    }

    @Test
    public void handleValidRequest() throws IOException {
        String token = UUID.randomUUID().toString();
        int meterStart = 4;
        StartTransactionRequestHandler handler = new StartTransactionRequestHandler(gson, domainService, OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER, ADD_ON_IDENTITY);

        Starttransaction requestPayload = new Starttransaction();
        requestPayload.setConnectorId(EVSE_ID.getNumberedId());
        requestPayload.setIdTag(IDENTIFYING_TOKEN.getToken());
        requestPayload.setTimestamp(FIVE_MINUTES_AGO);
        requestPayload.setMeterStart(meterStart);
        requestPayload.setReservationId(OCPPJ_RESERVATION_ID.getNumber());

        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), getMockWebSocket());

        verify(domainService).authorize(eq(CHARGING_STATION_ID), eq(IDENTIFYING_TOKEN.getToken()), any(FutureEventCallback.class), eq(ADD_ON_IDENTITY));
    }

    @Test
    public void handleValidRequestNoReservation() throws IOException {
        String token = UUID.randomUUID().toString();
        int meterStart = 4;
        StartTransactionRequestHandler handler = new StartTransactionRequestHandler(gson, domainService, OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER, ADD_ON_IDENTITY);

        Starttransaction requestPayload = new Starttransaction();
        requestPayload.setConnectorId(EVSE_ID.getNumberedId());
        requestPayload.setIdTag(IDENTIFYING_TOKEN.getToken());
        requestPayload.setTimestamp(FIVE_MINUTES_AGO);
        requestPayload.setMeterStart(meterStart);

        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), getMockWebSocket());

        verify(domainService).authorize(eq(CHARGING_STATION_ID), eq(IDENTIFYING_TOKEN.getToken()), any(FutureEventCallback.class), eq(ADD_ON_IDENTITY));
    }

}
