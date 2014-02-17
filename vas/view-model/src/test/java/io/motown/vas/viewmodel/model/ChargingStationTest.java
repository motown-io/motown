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
package io.motown.vas.viewmodel.model;

import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ChargingStationTest {

    @Test
    public void newChargingStationStateNotNull() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());

        assertNotNull(cs.getState());
    }

    @Test
    public void setStateNullValueValidateStateUnknown() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());

        cs.setState(null);

        assertEquals(ComponentStatus.UNKNOWN, cs.getState());
    }

    @Test
    public void newChargingStationUnknownOperator() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());

        assertEquals(ChargingStation.UNKNOWN_OPERATOR, cs.getOperator());
    }

    @Test
    public void newChargingStationSetNullOperatorSetsUnknown() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setOperator(null);

        assertEquals(ChargingStation.UNKNOWN_OPERATOR, cs.getOperator());
    }

    @Test
    public void newChargingStationSetValueOperatorSetsValue() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        String operator = "Motown";
        cs.setOperator(operator);

        assertEquals(operator, cs.getOperator());
    }

}
