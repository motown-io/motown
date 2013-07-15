package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class ChargingStationTest {

    private FixtureConfiguration<ChargingStation> fixture;

    private List<Connector> connectors = new LinkedList<Connector>();

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        // simple default Connector for the test ChargingPoint.
        connectors.add(new Connector(1, "CONTYPE", 32));
    }

    @Test
    public void testChargePointCreation() {
        fixture.given()
                .when(new CreateChargingStationCommand("CS-001", "MODEL-001", connectors))
                .expectEvents(new ChargingStationCreatedEvent("CS-001", "MODEL-001", connectors));
    }

    @Test
    public void testRequestingToUnlockConnector() {
        fixture.given(new ChargingStationCreatedEvent("CS-001", "MODEL-001", connectors))
                .when(new RequestUnlockConnectorCommand("CS-001", 1))
                .expectEvents(new UnlockConnectorRequestedEvent("CS-001", 1));
    }

    @Test
    public void testBootingChargingStationForKnownChargingStation() {
        fixture.given(new ChargingStationCreatedEvent("CS-001", "MODEL-001", connectors))
                .when(new BootChargingStationCommand("CS-001", "MODEL-001", connectors))
                .expectEvents(new ChargingStationBootedEvent("CS-001"));
    }

//    @Test
//    public void testBootingChargingStationForUnknownChargingStation() {
//        fixture.given()
//                .when(new BootChargingStationCommand("CS-001", "MODEL-001"))
//                .expectEvents(new ChargingStationCreatedEvent("CS-001", "MODEL-001"), new ChargingStationBootedEvent("CS-001", "MODEL-001"));
//    }

}
