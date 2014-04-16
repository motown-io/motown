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

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.ConfigurationKey;
import io.motown.ocpp.websocketjson.schema.generated.v15.GetconfigurationResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ADD_ON_IDENTITY;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetConfigurationResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private String token;
    private CorrelationToken correlationToken;
    private GetConfigurationResponseHandler handler;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);

        token = UUID.randomUUID().toString();
        correlationToken = new CorrelationToken(token);
        handler = new GetConfigurationResponseHandler(correlationToken);
    }

    @Test
    public void handleValidResponse() {
        GetconfigurationResponse payload = new GetconfigurationResponse();
        List configurationKeys = Lists.<ConfigurationKey>newArrayList();
        ConfigurationKey key = new ConfigurationKey();
        key.setKey("KVCBX_PROFILE");
        key.setReadonly(true);
        key.setValue("NQC-ACDC");
        configurationKeys.add(key);
        payload.setConfigurationKey(configurationKeys);
        payload.setUnknownKey(Lists.<String>newArrayList());
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        ArgumentCaptor<HashMap> configurationKeysCaptor = ArgumentCaptor.forClass(HashMap.class);
        verify(domainService).receiveConfigurationItems(any(ChargingStationId.class), configurationKeysCaptor.capture(), eq(ADD_ON_IDENTITY));
        assertEquals(configurationKeysCaptor.getValue().size(), 1);
    }

}
