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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class GrantPermissionCommandTest {

    @Test
    public void createCommand() {
        new GrantPermissionCommand(CHARGING_STATION_ID, ROOT_USER_IDENTITY, CreateChargingStationCommand.class, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullChargingStationId() {
        new GrantPermissionCommand(null, ROOT_USER_IDENTITY, CreateChargingStationCommand.class, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullUserIdentity() {
        new GrantPermissionCommand(CHARGING_STATION_ID, null, CreateChargingStationCommand.class, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullCommandClass() {
        new GrantPermissionCommand(CHARGING_STATION_ID, ROOT_USER_IDENTITY, null, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void createCommandWithNullIdentityContext() {
        new GrantPermissionCommand(CHARGING_STATION_ID, ROOT_USER_IDENTITY, CreateChargingStationCommand.class, null);
    }

}
