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

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.OcppWebSocketRequestHandler;
import io.motown.ocpp.websocketjson.schema.generated.v15.Metervalues;
import io.motown.ocpp.websocketjson.schema.generated.v15.Value;
import io.motown.ocpp.websocketjson.schema.generated.v15.Value_;
import org.atmosphere.websocket.WebSocket;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getMockWebSocket;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MeterValuesRequestHandlerTest {

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
        MeterValuesRequestHandler handler = new MeterValuesRequestHandler(gson, domainService, OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER, ADD_ON_IDENTITY, null);

        Metervalues requestPayload = new Metervalues();
        requestPayload.setConnectorId(2);
        requestPayload.setTransactionId(((NumberedTransactionId) TRANSACTION_ID).getNumber());

        List<Value> meterValues = Lists.newArrayList();

        Value meterValue = new Value();
        meterValue.setTimestamp(new Date());
        List<Value_> values = Lists.newArrayList();
        Value_ value = new Value_();
        value.setValue("0");
        value.setUnit(UNIT);
        value.setLocation("Outlet");
        value.setMeasurand(MEASURAND);
        value.setFormat("Raw");
        value.setContext("Sample.Periodic");
        values.add(value);
        meterValue.setValues(values);
        meterValues.add(meterValue);

        requestPayload.setValues(meterValues);

        WebSocket webSocket = getMockWebSocket();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocket);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(webSocket).write(argumentCaptor.capture());
        String response = argumentCaptor.getValue();
        assertNotNull(response);
    }

}
