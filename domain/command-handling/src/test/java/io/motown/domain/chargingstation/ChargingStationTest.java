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
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static io.motown.domain.chargingstation.ChargingStationTestUtils.*;

public class ChargingStationTest {

    private FixtureConfiguration<ChargingStation> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
    }

    @Test
    public void testChargePointCreation() {
        fixture.given()
                .when(new CreateChargingStationCommand(getChargingStationId()))
                .expectEvents(new ChargingStationCreatedEvent(getChargingStationId()));
    }

    @Test
    public void testChargePointRegistration() {
        fixture.given(getCreatedChargingStation())
                .when(new RegisterChargingStationCommand(getChargingStationId()))
                .expectEvents(new ChargingStationRegisteredEvent(getChargingStationId()));
    }

    @Test
    public void testAttemptToRegisterNonExistingChargePoint() {
        fixture.given()
                .when(new RegisterChargingStationCommand(getChargingStationId()))
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void testRetrieveChargingStationConfiguration() {
        fixture.given(getRegisteredChargingStation())
                .when(new RequestConfigurationCommand(getChargingStationId()))
                .expectEvents(new ConfigurationRequestedEvent(getChargingStationId()));
    }

    @Test
    public void testRetrieveChargingStationConfigurationForNonRegisteredChargingStation() {
        fixture.given(getCreatedChargingStation())
                .when(new RequestConfigurationCommand(getChargingStationId()))
                .expectException(RuntimeException.class);
    }

    @Test
    public void testConfigureChargingStation() {
        fixture.given(getRegisteredChargingStation())
                .when(new ConfigureChargingStationCommand(getChargingStationId(), getConnectors(), getConfigurationItems()))
                .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), getConfigurationItems()));
    }

    @Test
    public void testConfigureChargingStationWithoutConnectors() {
        fixture.given(getRegisteredChargingStation())
                .when(new ConfigureChargingStationCommand(getChargingStationId(), getConfigurationItems()))
                .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), Collections.<Connector>emptySet(), getConfigurationItems()));
    }

    @Test
    public void testConfigureChargingStationWithoutConfigurationItems() {
        fixture.given(getRegisteredChargingStation())
                .when(new ConfigureChargingStationCommand(getChargingStationId(), getConnectors()))
                .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), Collections.<String, String>emptyMap()));
    }

    @Test
    public void testAttemptToConfigureUnregisteredChargingStation() {
        fixture.given(getCreatedChargingStation())
                .when(new ConfigureChargingStationCommand(getChargingStationId(), getConfigurationItems()))
                .expectException(RuntimeException.class);
    }

    @Test
    public void testRequestingToUnlockConnector() {
        fixture.given(getConfiguredChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), 1))
                .expectEvents(new UnlockConnectorRequestedEvent(getChargingStationId(), 1));
    }

    @Test
    public void testRequestingToUnlockUnknownConnector() {
        fixture.given(getConfiguredChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), 3))
                .expectEvents(new ConnectorNotFoundEvent(getChargingStationId(), 3));
    }

    @Test
    public void testRequestingToUnlockAllConnectors() {
        fixture.given(getConfiguredChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), Connector.ALL))
                .expectEvents(new UnlockConnectorRequestedEvent(getChargingStationId(), 1), new UnlockConnectorRequestedEvent(getChargingStationId(), 2));
    }
}
