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

import com.google.common.collect.Lists;
import io.motown.domain.api.chargingstation.*;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ChargingStationTest {

    private FixtureConfiguration<ChargingStation> fixture;

    private List<Connector> connectors = new LinkedList<>();

    private Map<String, String> attributes = new HashMap<>();

    private Map<String, String> configurationItems = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        // simple default Connector for the test ChargingPoint.
        connectors.add(new Connector(1, "CONTYPE", 32));
        connectors.add(new Connector(2, "CONTYPE", 32));

        attributes.put("model", "MODEL-001");

        configurationItems.put("socket1_id", "1");
        configurationItems.put("socket1_type", "Type1");
        configurationItems.put("socket1_charge", "19");
        configurationItems.put("socket2_id", "1");
        configurationItems.put("socket2_type", "Type2");
        configurationItems.put("socket2_charge", "10");
    }

    @Test
    public void testChargePointCreation() {
        fixture.given()
                .when(new CreateChargingStationCommand(new ChargingStationId("CS-001")))
                .expectEvents(new ChargingStationCreatedEvent(new ChargingStationId("CS-001")));
    }

    @Test
    public void testChargePointRegistration() {
        fixture.given(new ChargingStationCreatedEvent(new ChargingStationId("CS-001")))
                .when(new RegisterChargingStationCommand(new ChargingStationId("CS-001")))
                .expectEvents(new ChargingStationRegisteredEvent(new ChargingStationId("CS-001")));
    }

    @Test
    public void testAttemptToRegisterNonExistingChargePoint() {
        fixture.given()
                .when(new RegisterChargingStationCommand(new ChargingStationId("CS-002")))
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void testRetrieveChargingStationConfiguration() {
        fixture.given( Lists.newArrayList(new ChargingStationCreatedEvent(new ChargingStationId("CS-001")),
                                            new ChargingStationRegisteredEvent(new ChargingStationId("CS-001"))) )
                .when(new RequestConfigurationCommand(new ChargingStationId("CS-001")))
                .expectEvents(new ConfigurationRequestedEvent(new ChargingStationId("CS-001")));
    }

    @Test
    public void testRetrieveChargingStationConfigurationForNonRegisteredChargingStation() {
        fixture.given(new ChargingStationCreatedEvent(new ChargingStationId("CS-001")))
                .when(new RequestConfigurationCommand(new ChargingStationId("CS-001")))
                .expectException(RuntimeException.class);
    }

    @Test
    public void testConfigureChargingStation() {
        ChargingStationId csid = new ChargingStationId("CS-001");
        fixture.given( Lists.newArrayList(new ChargingStationCreatedEvent(csid),
                                            new ChargingStationRegisteredEvent(csid)) )
                .when(new ConfigureChargingStationCommand(new ChargingStationId("CS-001"), configurationItems))
                .expectEvents(new ChargingStationConfiguredEvent(new ChargingStationId("CS-001"), configurationItems));
    }

    @Test
    public void testAttemptToConfigureUnregisteredChargingStation() {
        ChargingStationId csid = new ChargingStationId("CS-001");
        fixture.given( new ChargingStationCreatedEvent(csid))
                .when(new ConfigureChargingStationCommand(new ChargingStationId("CS-001"), configurationItems))
                .expectException(RuntimeException.class);
    }

    @Test
    public void testRequestingToUnlockConnector() {
        ChargingStationId csid = new ChargingStationId("CS-001");
        fixture.given( Lists.newArrayList(new ChargingStationCreatedEvent(csid),
                                            new ChargingStationRegisteredEvent(csid),
                                            new ChargingStationConfiguredEvent(csid, new HashMap<String, String>())) )
                .when(new RequestUnlockConnectorCommand(csid, 1))
                .expectEvents(new UnlockConnectorRequestedEvent(csid, 1));
    }

    @Test
    public void testRequestingToUnlockUnknownConnector() {
        ChargingStationId csid = new ChargingStationId("CS-001");
        fixture.given( Lists.newArrayList(new ChargingStationCreatedEvent(csid),
                                            new ChargingStationRegisteredEvent(csid),
                                            new ChargingStationConfiguredEvent(csid, new HashMap<String, String>())) )
                .when(new RequestUnlockConnectorCommand(csid, 3))
                .expectEvents(new ConnectorNotFoundEvent(csid, 3));
    }

    @Test
    public void testRequestingToUnlockAllConnectors() {
        ChargingStationId csid = new ChargingStationId("CS-001");
        fixture.given( Lists.newArrayList(new ChargingStationCreatedEvent(csid),
                                            new ChargingStationRegisteredEvent(csid),
                                            new ChargingStationConfiguredEvent(csid, new HashMap<String, String>())) )
                .when(new RequestUnlockConnectorCommand(new ChargingStationId("CS-001"), Connector.ALL))
                .expectEvents(new UnlockConnectorRequestedEvent(new ChargingStationId("CS-001"), 1), new UnlockConnectorRequestedEvent(new ChargingStationId("CS-001"), 2));
    }
}
