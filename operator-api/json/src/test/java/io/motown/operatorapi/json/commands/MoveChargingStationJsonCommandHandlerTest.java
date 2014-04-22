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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTITY_CONTEXT;

public class MoveChargingStationJsonCommandHandlerTest {
    private Gson gson;
    private MoveChargingStationJsonCommandHandler handler = new MoveChargingStationJsonCommandHandler();

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testAddress() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{address:{addressLine1:'Teststraat 1',city:'Deurne',country:'NL'},accessibility:'PUBLIC'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddressWrongAccessibility() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{address:{addressLine1:'Teststraat 1',city:'Deurne',country:'NL'},accessibility:'NON-PUBLIC'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidAddress() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{address:{addressLine:'Teststraat 1',city:'Deurne',country:'NL'},accessibility:'PUBLIC'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test
    public void testCoordinates() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{coordinates:{latitude:'0.0',longitude:'0.0'},accessibility:'PUBLIC'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidCoordinatesNumber() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{coordinates:{latitude:'center',longitude:'0.0'},accessibility:'PUBLIC'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidCoordinates() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{coordinates:{lattitude:'0.0',longitude:'0.0'},accessibility:'PUBLIC'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }
}
