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
package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.ChargingStationRegistrationStatus;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.chargingstation.ChargingStationTestUtils.*;

public class BootChargingStationCommandHandlerTest {

    private FixtureConfiguration<ChargingStation> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        BootChargingStationCommandHandler commandHandler = new BootChargingStationCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testBootingChargingStationForKnownChargingStation() {
        fixture.given(getRegisteredChargingStation())
                .when(new BootChargingStationCommand(getChargingStationId(), getAttributes()))
                .expectEvents(new ChargingStationBootedEvent(getChargingStationId(), getAttributes()))
                .expectReturnValue(ChargingStationRegistrationStatus.REGISTERED);
    }

    @Test
    public void testBootingChargingStationForUnknownChargingStation() {
        fixture.given()
                .when(new BootChargingStationCommand(getChargingStationId(), getAttributes()))
                .expectEvents(new ChargingStationCreatedEvent(getChargingStationId()),
                        new ChargingStationBootedEvent(getChargingStationId(), getAttributes()))
                .expectReturnValue(ChargingStationRegistrationStatus.UNREGISTERED);
    }
}
