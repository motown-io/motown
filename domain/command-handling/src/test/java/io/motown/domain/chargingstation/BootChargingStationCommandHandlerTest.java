package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.Connector;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class BootChargingStationCommandHandlerTest {

    private FixtureConfiguration<ChargingStation> fixture;

    private List<Connector> connectors = new LinkedList<Connector>();

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        // simple default Connector for the test ChargingPoint.
        connectors.add(new Connector(1, "CONTYPE", 32));
        BootChargingStationCommandHandler commandHandler = new BootChargingStationCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testBootingChargingStationForKnownChargingStation() {
        fixture.given(new ChargingStationCreatedEvent("CS-001", "MODEL-001", connectors))
                .when(new BootChargingStationCommand("CS-001", "MODEL-001", connectors))
                .expectEvents(new ChargingStationBootedEvent("CS-001"));
    }

    @Test
    public void testBootingChargingStationForUnknownChargingStation() {
        fixture.given()
                .when(new BootChargingStationCommand("CS-001", "MODEL-001", connectors))
                .expectEvents(new ChargingStationCreatedEvent("CS-001", "MODEL-001", connectors), new ChargingStationBootedEvent("CS-001"));
    }
}
