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

import io.motown.domain.api.chargingstation.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static io.motown.domain.chargingstation.ChargingStationTestUtils.*;

public class ChargingStationCommandHandlerTest {

    private FixtureConfiguration<ChargingStation> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        ChargingStationCommandHandler commandHandler = new ChargingStationCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testBootingUncreatedChargingStation() {
        fixture.given()
               .when(new BootChargingStationCommand(getChargingStationId(), getAttributes()))
               .expectEvents(new ChargingStationCreatedEvent(getChargingStationId(), false),
                             new UnconfiguredChargingStationBootedEvent(getChargingStationId(), getAttributes()))
               .expectReturnValue(ChargingStationRegistrationStatus.DENIED);
    }

    @Test
    public void testBootingRegisteredChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new BootChargingStationCommand(getChargingStationId(), getAttributes()))
               .expectEvents(new UnconfiguredChargingStationBootedEvent(getChargingStationId(), getAttributes()))
               .expectReturnValue(ChargingStationRegistrationStatus.ACCEPTED);
    }

    @Test
    public void testBootingConfiguredChargingStation() {
        fixture.given(getChargingStation())
               .when(new BootChargingStationCommand(getChargingStationId(), getAttributes()))
               .expectEvents(new ConfiguredChargingStationBootedEvent(getChargingStationId(), getAttributes()))
               .expectReturnValue(ChargingStationRegistrationStatus.ACCEPTED);
    }

    @Test
    public void testRegisteringExistingUnAcceptedChargingStation() {
        fixture.given(getCreatedChargingStation(false))
               .when(new RegisterChargingStationCommand(getChargingStationId()))
               .expectEvents(new ChargingStationRegisteredEvent(getChargingStationId()));
    }

    @Test
    public void testRegisteringExistingAcceptedChargingStation() {
        fixture.given(getCreatedChargingStation(true))
                .when(new RegisterChargingStationCommand(getChargingStationId()))
                .expectException(IllegalStateException.class);
    }


    @Test @Ignore
    public void testRegisteringNonExistentChargingStation() {
        fixture.given()
               .when(new RegisterChargingStationCommand(getChargingStationId()))
               .expectEvents(new ChargingStationCreatedEvent(getChargingStationId(), true));
    }

    @Test
    public void testRegisteringAlreadyRegisteredChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new RegisterChargingStationCommand(getChargingStationId()))
               .expectException(IllegalStateException.class);
    }
}
