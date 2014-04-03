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
import io.motown.domain.api.chargingstation.MakeChargingStationNotReservableCommand;
import io.motown.domain.api.chargingstation.MakeChargingStationReservableCommand;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;
import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID;
import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateChangeChargingStationReservableJsonCommandHandlerTest {

    private Gson gson;

    private UpdateChargingStationReservableJsonCommandHandler handler = new UpdateChargingStationReservableJsonCommandHandler();

    private DomainCommandGateway gateway;

    @Before
    public void setUp() {
        gateway = mock(DomainCommandGateway.class);

        gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(gateway);
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
    }

    @Test
    public void updateReservableToTrueNoExceptions() {
        JsonObject commandObject = gson.fromJson("{'reservable': true}", JsonObject.class);

        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void updateReservableToFalseNoExceptions() {
        JsonObject commandObject = gson.fromJson("{'reservable': false}", JsonObject.class);

        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void updateReservableToTrueValidateGatewayCall() {
        JsonObject commandObject = gson.fromJson("{'reservable': true}", JsonObject.class);

        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);

        verify(gateway).send(new MakeChargingStationReservableCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void updateReservableToFalseValidateGatewayCall() {
        JsonObject commandObject = gson.fromJson("{'reservable': false}", JsonObject.class);

        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);

        verify(gateway).send(new MakeChargingStationNotReservableCommand(CHARGING_STATION_ID));
    }
}
