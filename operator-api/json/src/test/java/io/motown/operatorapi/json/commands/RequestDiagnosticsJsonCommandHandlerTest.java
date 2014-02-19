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
import org.junit.Before;
import org.junit.Test;

public class RequestDiagnosticsJsonCommandHandlerTest {

    private RequestDiagnosticsJsonCommandHandler handler = new RequestDiagnosticsJsonCommandHandler();
    private Gson gson;

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
    }

    @Test
    public void testHandler() {
        JsonObject commandObject = gson.fromJson("{targetLocation:'LOC001'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidJsonProperty() {
        JsonObject commandObject = gson.fromJson("{target:'LOC001'}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPropertyType() {
        JsonObject commandObject = gson.fromJson("{targetLocation:{location:'LOC001'}}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject);
    }
}
