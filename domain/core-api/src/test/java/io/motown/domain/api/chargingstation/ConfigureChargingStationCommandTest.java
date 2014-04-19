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

import java.util.Collections;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class ConfigureChargingStationCommandTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNullAndEvses() {
        new ConfigureChargingStationCommand(null, Collections.<Evse>emptySet(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithEvsesNull() {
        new ConfigureChargingStationCommand(CHARGING_STATION_ID, null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperationExceptionThrownWhenModifyingEvses() {
        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT);

        command.getEvses().add(new Evse(EVSE_ID, CONNECTORS));
    }
}
