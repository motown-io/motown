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

public class GrantAuthorizationCommandTest {

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullChargingStationId() {
        new GrantAuthorizationCommand(null, IDENTIFYING_TOKEN_ACCEPTED, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullIdentifyingToken() {
        new GrantAuthorizationCommand(CHARGING_STATION_ID, null, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullIdentityContext() {
        new GrantAuthorizationCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN_ACCEPTED, null);
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(GrantAuthorizationCommand.class).usingGetClass().verify();
    }
}
