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
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;
import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING;

public class UpdateFirmwareJsonCommandHandlerTest {

    private Gson gson;

    private UpdateFirmwareJsonCommandHandler handler = new UpdateFirmwareJsonCommandHandler();

    @Before
    public void setUp() {
        this.gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testUpdateFirmwareCommand() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{location:'DEURNE',retrieveDate:'2014-02-03T12:00:00Z'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpdateCommandInvalidDate() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{location:'DEURNE',retrieveDate:'2014-02-03'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidUpdateCommandInvalidLocation() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{loc:'DEURNE',retrieveDate:'2014-02-03T12:00:00Z'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }
}
