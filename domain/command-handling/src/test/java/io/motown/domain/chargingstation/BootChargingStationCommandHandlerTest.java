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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.motown.domain.api.chargingstation.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class BootChargingStationCommandHandlerTest {

    private FixtureConfiguration<ChargingStation> fixture;

    private List<Connector> connectors = new LinkedList<Connector>();

    private Map<String, String> attributes;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        // simple default Connector for the test ChargingPoint.
        connectors.add(new Connector(1, "CONTYPE", 32));
        attributes = ImmutableMap.<String, String>builder()
                .put("vendor", "VENDOR001")
                .put("model", "MODEL001").build();
        BootChargingStationCommandHandler commandHandler = new BootChargingStationCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testBootingChargingStationForKnownChargingStation() {
        ChargingStationId csid = new ChargingStationId("CS-001");

        fixture.given( Lists.newArrayList(new ChargingStationCreatedEvent(csid), new ChargingStationRegisteredEvent(csid)) )
                .when(new BootChargingStationCommand(csid, attributes))
                .expectEvents(new ChargingStationBootedEvent(csid, attributes))
                .expectReturnValue(ChargingStationRegistrationStatus.REGISTERED);
    }

    @Test
    public void testBootingChargingStationForUnknownChargingStation() {
        ChargingStationId csid = new ChargingStationId("CS-001");
        fixture.given()
                .when(new BootChargingStationCommand(csid, attributes))
                .expectEvents(new ChargingStationCreatedEvent(csid),
                        new ChargingStationBootedEvent(csid, attributes))
                .expectReturnValue(ChargingStationRegistrationStatus.UNREGISTERED);
    }

}
