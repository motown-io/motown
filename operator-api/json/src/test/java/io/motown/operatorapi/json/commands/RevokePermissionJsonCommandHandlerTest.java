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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CreateChargingStationCommand;
import io.motown.domain.api.chargingstation.RevokePermissionCommand;
import io.motown.domain.api.security.SimpleUserIdentity;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTITY_CONTEXT;
import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RevokePermissionJsonCommandHandlerTest {

    private Gson gson;

    private RevokePermissionJsonCommandHandler handler = new RevokePermissionJsonCommandHandler();

    private DomainCommandGateway gateway;

    @Before
    public void setUp() {
        gateway = mock(DomainCommandGateway.class);

        gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);
        handler.setCommandGateway(gateway);
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void handleGrantPermission() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{'commandClass': 'io.motown.domain.api.chargingstation.CreateChargingStationCommand', 'userIdentity': 'testUser'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test
    public void handleGrantPermissionVerifyGatewayCall() throws UserIdentityUnauthorizedException {
        Class commandClass = CreateChargingStationCommand.class;
        String user = "testUser";

        JsonObject commandObject = gson.fromJson("{'commandClass': '" + commandClass.getName() + "', 'userIdentity': '" + user + "'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);

        verify(gateway).send(new RevokePermissionCommand(new ChargingStationId(CHARGING_STATION_ID_STRING), new SimpleUserIdentity(user), commandClass, IDENTITY_CONTEXT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void handleGrantPermissionInvalidCommandClass() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{'commandClass': 'io.motown.domain.api.chargingstation.NotExistingCommandClass', 'userIdentity': 'testUser'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

}
