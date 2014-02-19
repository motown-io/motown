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

public class SendAuthorizationListJsonCommandHandlerTest {

    public static final String CHARGING_STATION_ID = "TEST_CP";
    private Gson gson;

    private SendAuthorizationListJsonCommandHandler handler = new SendAuthorizationListJsonCommandHandler();

    @Before
    public void setUp() {
        this.gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
    }

    @Test
    public void testSendAuthorizationListCommand() {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'FULL',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject);
    }

    @Test
    public void testDifferentialUpdateType() {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'DIFFERENTIAL',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpdateType() {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'NEW',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAuthenticationStatus() {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'FULL',items:[{token:'1',status:'NEW'}]}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID, commandObject);
    }

}
