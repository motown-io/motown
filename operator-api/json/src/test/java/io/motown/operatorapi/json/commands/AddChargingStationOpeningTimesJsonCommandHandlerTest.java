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
import com.google.gson.JsonParseException;
import org.junit.Before;
import org.junit.Test;

public class AddChargingStationOpeningTimesJsonCommandHandlerTest {
    private Gson gson;
    private AddChargingStationOpeningTimesJsonCommandHandler handler = new AddChargingStationOpeningTimesJsonCommandHandler();

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
    }

    @Test
    public void testSetOpeningTimes() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:1,timeStart:'12:00',timeStop:'12:30'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test
    public void testSetOpeningTimesOtherHour() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:1,timeStart:'12:00',timeStop:'13:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOpeningTimesInvalidDay() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:8,timeStart:'12:00',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test(expected = NumberFormatException.class)
    public void testSetOpeningTimesInvalidArgument() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'MONDAY',timeStart:'12:00',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test(expected = JsonParseException.class)
    public void testSetOpeningTimesInvalidTime() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'2',timeStart:'12:60',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test(expected = JsonParseException.class)
    public void testSetOpeningTimesInvalidTime2() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'2',timeStart:'24:00',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpeningTimesTimeStartEqualToTimeStop() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'2',timeStart:'12:00',timeStop:'12:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpeningTimesTimeStartNotBeforeTimeStop() {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'2',timeStart:'12:00',timeStop:'11:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, null);
    }
}
