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
import com.google.gson.JsonSyntaxException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;

public class ConfigureJsonCommandHandlerTest {

    public static final String CHARGING_STATION_ID = "TEST_CP";
    private Gson gson;

    private ConfigureJsonCommandHandler handler = new ConfigureJsonCommandHandler();

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
    }

    @Test
    public void testHandleComplete() {
        JsonObject commandObject = gson.fromJson("{'evses' : [{'evseId' : 1, 'connectors' : [{'maxAmp': 32, 'phase': 3, 'voltage': 230, 'chargingProtocol': 'MODE3', 'current': 'AC', 'connectorType': 'TESLA'}]}], 'settings' : {'key':'value', 'key2':'value2'}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void testHandleSettingsOnly() {
        JsonObject commandObject = gson.fromJson("{'settings' : {'key':'value', 'key2':'value2'}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void testHandleConnectorsOnly() {
        JsonObject commandObject = gson.fromJson("{'evses' : [{'evseId' : 1, 'connectors' : [{'maxAmp': 32, 'phase': 3, 'voltage': 230, 'chargingProtocol': 'MODE3', 'current': 'AC', 'connectorType': 'TESLA'}]}]}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected=IllegalArgumentException.class)
    public void throwIllegalArgumentExceptionIfEvsesAreNotInList() {
        JsonObject commandObject = gson.fromJson("{'evses' : {'evseId' : 1, 'connectors' : [{'maxAmp': 32, 'phase': 3, 'voltage': 230, 'chargingProtocol': 'MODE3', 'current': 'AC', 'connectorType': 'TESLA'}] }}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected=JsonSyntaxException.class)
    public void throwJsonSyntaxExceptionIfEvsesAreInListInsteadOfArray() {
        JsonObject commandObject = gson.fromJson("{'evses' : [{['evseId' : 1, 'connectors' : [{'maxAmp': 32, 'phase': 3, 'voltage': 230, 'chargingProtocol': 'MODE3', 'current': 'AC', 'connectorType': 'TESLA'}]}]}]}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject, ROOT_IDENTITY_CONTEXT);
    }

}
