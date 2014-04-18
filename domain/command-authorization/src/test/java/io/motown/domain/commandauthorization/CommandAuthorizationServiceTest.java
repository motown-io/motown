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
package io.motown.domain.commandauthorization;

import io.motown.domain.api.chargingstation.AcceptChargingStationCommand;
import io.motown.domain.commandauthorization.model.CommandAuthorization;
import io.motown.domain.commandauthorization.model.CommandAuthorizationId;
import io.motown.domain.commandauthorization.repositories.CommandAuthorizationRepository;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.USER_IDENTITY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandAuthorizationServiceTest {

    private CommandAuthorizationService commandAuthorizationService;

    private CommandAuthorizationRepository commandAuthorizationRepository;

    private static final Class<AcceptChargingStationCommand> COMMAND_CLASS = AcceptChargingStationCommand.class;

    @Before
    public void setup() {
        commandAuthorizationService = new CommandAuthorizationService();

        commandAuthorizationRepository = mock(CommandAuthorizationRepository.class);
        commandAuthorizationService.setCommandAuthorizationRepository(commandAuthorizationRepository);
    }

    @Test
    public void isAuthorized() {
        when(commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS)).thenReturn(new CommandAuthorization(new CommandAuthorizationId(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), AcceptChargingStationCommand.class)));

        assertTrue(commandAuthorizationService.isAuthorized(CHARGING_STATION_ID, USER_IDENTITY, COMMAND_CLASS));
    }

    @Test
    public void isNotAuthorized() {
        when(commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS)).thenReturn(null);

        assertFalse(commandAuthorizationService.isAuthorized(CHARGING_STATION_ID, USER_IDENTITY, COMMAND_CLASS));
    }

}
