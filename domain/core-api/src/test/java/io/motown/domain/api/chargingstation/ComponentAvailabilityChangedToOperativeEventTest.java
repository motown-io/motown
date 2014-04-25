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

public class ComponentAvailabilityChangedToOperativeEventTest {

    @Test(expected = NullPointerException.class)
    public void creatingWithChargingStationIdNullShouldThrowNullPointerException() {
        new ComponentAvailabilityChangedToOperativeEvent(null, EVSE_ID, ChargingStationComponent.EVSE, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void creatingWithComponentIdNullShouldThrowNullPointerException() {
        new ComponentAvailabilityChangedToOperativeEvent(CHARGING_STATION_ID, null, ChargingStationComponent.EVSE, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void creatingWithComponentNullShouldThrowNullPointerException() {
        new ComponentAvailabilityChangedToOperativeEvent(CHARGING_STATION_ID, EVSE_ID, null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void creatingWithIdentityContextNullShouldThrowNullPointerException() {
        new ComponentAvailabilityChangedToOperativeEvent(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, null);
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(ComponentAvailabilityChangedToOperativeEvent.class).usingGetClass().verify();
    }
}
