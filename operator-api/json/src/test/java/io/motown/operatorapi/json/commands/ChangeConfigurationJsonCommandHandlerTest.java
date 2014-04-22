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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.ConfigurationItem;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestChangeConfigurationItemCommand;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChangeConfigurationJsonCommandHandlerTest {

    private Gson gson;

    private ChangeConfigurationJsonCommandHandler handler = new ChangeConfigurationJsonCommandHandler();

    private DomainCommandGateway gateway;

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);

        this.gateway = mock(DomainCommandGateway.class);
        handler.setCommandGateway(this.gateway);
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testHandleChangeConfigurationOnRegisteredStation() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{'key' : 'foo', 'value': 'bar'}", JsonObject.class);

        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID.getId(), commandObject, ROOT_IDENTITY_CONTEXT);

        RequestChangeConfigurationItemCommand command = new RequestChangeConfigurationItemCommand(OperatorApiJsonTestUtils.CHARGING_STATION_ID, new ConfigurationItem("foo", "bar"), ROOT_IDENTITY_CONTEXT);
        verify(this.gateway).send(eq(command), any(CorrelationToken.class));
    }
}
