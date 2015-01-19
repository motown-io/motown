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
import io.motown.ocpp.websocketjson.schema.generated.v15.Stoptransaction;
import io.motown.ocpp.websocketjson.schema.generated.v15.TransactionDatum;
import io.motown.ocpp.websocketjson.schema.generated.v15.Value__;
import io.motown.ocpp.websocketjson.schema.generated.v15.Value___;
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

public class StopTransactionRequestHandlerTest {

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
        StopTransactionRequestHandler handler = new StopTransactionRequestHandler(gson, domainService, OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER, ADD_ON_IDENTITY);

        Stoptransaction requestPayload = new Stoptransaction();
        requestPayload.setIdTag(IDENTIFYING_TOKEN.getToken());
        requestPayload.setTimestamp(new Date());
        requestPayload.setTransactionId(((NumberedTransactionId) TRANSACTION_ID).getNumber());
        requestPayload.setMeterStop(20);

        List<TransactionDatum> transactionData = Lists.newArrayList();
        TransactionDatum transactionDetails = new TransactionDatum();
        Value__ value = new Value__();
        value.setTimestamp(new Date());

        List<Value___> meterValues = Lists.newArrayList();
        Value___ meterValue = new Value___();
        meterValue.setValue("0");
        meterValue.setUnit(UNIT);
        meterValue.setMeasurand(MEASURAND);
        meterValues.add(meterValue);
        value.setValues(meterValues);

        List<Value__> values = Lists.newArrayList();
        values.add(value);
        transactionDetails.setValues(values);
        transactionData.add(transactionDetails);
        requestPayload.setTransactionData(transactionData);

        WebSocket webSocket = getMockWebSocket();
        handler.handleRequest(CHARGING_STATION_ID, token, gson.toJson(requestPayload), webSocket);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(webSocket).write(argumentCaptor.capture());
        String response = argumentCaptor.getValue();
        assertNotNull(response);
    }

}
