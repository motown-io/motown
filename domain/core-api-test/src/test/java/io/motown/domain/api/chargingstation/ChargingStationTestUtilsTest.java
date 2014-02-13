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

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;

public class ChargingStationTestUtilsTest {

    @Test
    public void getAttributesShouldReturnMapWithGivenVendor() {
        final Map<String, String> attributes = getAttributes(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL);

        assertEquals(CHARGING_STATION_VENDOR, attributes.get("vendor"));
    }

    @Test
    public void getAttributesShouldReturnMapWithGivenModel() {
        final Map<String, String> attributes = getAttributes(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL);

        assertEquals(CHARGING_STATION_MODEL, attributes.get("model"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAttributesShouldReturnImmutableMap() {
        final Map<String, String> attributes = getAttributes(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL);

        attributes.put("another.key", "VALUE");
    }

    @Test(expected = NullPointerException.class)
    public void getAttributesWithNullVendorShouldThrowNullPointerException() {
        getAttributes(null, CHARGING_STATION_MODEL);
    }

    @Test(expected = NullPointerException.class)
    public void getAttributesWithNullModelShouldThrowNullPointerException() {
        getAttributes(CHARGING_STATION_VENDOR, null);
    }

    @Test
    public void getConnectorsShouldReturnCorrectNumberOfConnectors() {
        final int numberOfConnectors = 30;

        final List<Connector> connectors = getConnectors(numberOfConnectors);

        assertEquals(numberOfConnectors, connectors.size());
    }

    @Test
    public void getConnectorsShouldReturnListOfConnectors() {
        final List<Connector> connectors = getConnectors(2);

        assertEquals(CONNECTOR, connectors.get(0));
        assertEquals(CONNECTOR, connectors.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConnectorsWithZeroConnectorsShouldThrowIllegalArgumentException() {
        getConnectors(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getConnectorsWithNegativeNumberOfConnectorsShouldThrowIllegalArgumentException() {
        getConnectors(-1);
    }

    @Test
    public void getEvseShouldReturnEvseWithGivenIdentifier() {
        Evse evse = getEvse(EVSE_ID.getNumberedId());

        assertEquals(EVSE_ID, evse.getEvseId());
    }

    @Test
    public void getEvseShouldReturnEvseWithCorrectNumberOfConnectors() {
        Evse evse = getEvse(EVSE_ID.getNumberedId());

        assertEquals(NUMBER_OF_CONNECTORS, evse.getConnectors().size());
    }

    @Test
    public void getEvsesShouldReturnTheCorrectNumberOfEvses() {
        final int numberOfEvses = 3;

        Set<Evse> evses = getEvses(numberOfEvses);

        assertEquals(numberOfEvses, evses.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEvsesWithZeroEvsesShouldThrowIllegalArgumentException() {
        getEvses(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEvsesWithNegativeNumberOfEvsesShouldThrowIllegalArgumentException() {
        getEvses(-1);
    }
}
