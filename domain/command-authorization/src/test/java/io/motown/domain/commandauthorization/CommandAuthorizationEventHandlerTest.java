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
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.PermissionGrantedEvent;
import io.motown.domain.api.chargingstation.PermissionRevokedEvent;
import io.motown.domain.api.security.AllPermissions;
import io.motown.domain.commandauthorization.repositories.CommandAuthorizationRepository;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.mockito.Mockito.*;

public class CommandAuthorizationEventHandlerTest {

    private CommandAuthorizationEventHandler commandAuthorizationEventHandler;

    private CommandAuthorizationRepository commandAuthorizationRepository;

    private static final Class<AcceptChargingStationCommand> COMMAND_CLASS = AcceptChargingStationCommand.class;

    @Before
    public void setup() {
        commandAuthorizationRepository = mock(CommandAuthorizationRepository.class);

        commandAuthorizationEventHandler = new CommandAuthorizationEventHandler();
        commandAuthorizationEventHandler.setCommandAuthorizationRepository(commandAuthorizationRepository);
    }

    @Test
    public void handleChargingStationCreatedEvent() {
        commandAuthorizationEventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, IDENTITY_CONTEXT));

        verify(commandAuthorizationRepository).createOrUpdate(CHARGING_STATION_ID.getId(), ROOT_USER_IDENTITY.getId(), AllPermissions.class);
    }

    @Test
    public void handlePermissionGrantedEvent() {
        commandAuthorizationEventHandler.handle(new PermissionGrantedEvent(CHARGING_STATION_ID, USER_IDENTITY, COMMAND_CLASS, IDENTITY_CONTEXT));

        verify(commandAuthorizationRepository).createOrUpdate(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);
    }

    @Test
    public void handlePermissionRevokedEvent() {
        commandAuthorizationEventHandler.handle(new PermissionRevokedEvent(CHARGING_STATION_ID, USER_IDENTITY, COMMAND_CLASS, IDENTITY_CONTEXT));

        verify(commandAuthorizationRepository).remove(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);
    }

}
