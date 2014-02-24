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
package io.motown.domain.api.chargingstation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class ChangeChargingStationLocationCommandTest {

    @Test(expected = NullPointerException.class)
    public void testPlaceChargingStationCommandWithNullChargingStationId() {
        new PlaceChargingStationCommand(null, COORDINATES, ADDRESS, ACCESSIBILITY);
    }

    @Test(expected = NullPointerException.class)
    public void testPlaceChargingStationCommandWithNullCoordinatesAndAddress() {
        new PlaceChargingStationCommand(CHARGING_STATION_ID, null, null, ACCESSIBILITY);
    }

    @Test(expected = NullPointerException.class)
    public void testPlaceChargingStationCommandWithNullAccessibility() {
        new PlaceChargingStationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, null);
    }

    @Test(expected = NullPointerException.class)
    public void testImproveChargingStationLocationCommandWithNullChargingStationId() {
        new ImproveChargingStationLocationCommand(null, COORDINATES, ADDRESS, ACCESSIBILITY);
    }

    @Test(expected = NullPointerException.class)
    public void testImproveChargingStationLocationCommandWithNullCoordinatesAndAddress() {
        new ImproveChargingStationLocationCommand(CHARGING_STATION_ID, null, null, ACCESSIBILITY);
    }

    @Test(expected = NullPointerException.class)
    public void testImproveChargingStationLocationCommandWithNullAccessibility() {
        new ImproveChargingStationLocationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, null);
    }

    @Test(expected = NullPointerException.class)
    public void testMoveChargingStationCommandWithNullChargingStationId() {
        new MoveChargingStationCommand(null, COORDINATES, ADDRESS, ACCESSIBILITY);
    }

    @Test(expected = NullPointerException.class)
    public void testMoveChargingStationCommandWithNullCoordinatesAndAddress() {
        new MoveChargingStationCommand(CHARGING_STATION_ID, null, null, ACCESSIBILITY);
    }

    @Test(expected = NullPointerException.class)
    public void testMoveChargingStationCommandWithNullAccessibility() {
        new MoveChargingStationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, null);
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(PlaceChargingStationCommand.class).usingGetClass().verify();
        EqualsVerifier.forClass(ImproveChargingStationLocationCommand.class).usingGetClass().verify();
        EqualsVerifier.forClass(MoveChargingStationCommand.class).usingGetClass().verify();
    }
}
