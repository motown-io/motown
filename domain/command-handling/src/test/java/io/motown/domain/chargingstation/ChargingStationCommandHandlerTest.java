package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

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
    public void testRequestingToUnlockConnector() {
        fixture.given(new ChargingStationCreatedEvent("CS-001", "MODEL-001"))
                .when(new RequestUnlockConnectorCommand("CS-001", 1))
                .expectEvents(new UnlockConnectorRequestedEvent("CS-001", 1));
    }

    @Test
    public void testBootingChargingStationForKnownChargingStation() {
        fixture.given(new ChargingStationCreatedEvent("CS-001", "MODEL-001"))
                .when(new BootChargingStationCommand("CS-001", "MODEL-001"))
                .expectEvents(new ChargingStationBootedEvent("CS-001", "MODEL-001"));
    }

    @Test
    public void testBootingChargingStationForUnknownChargingStation() {
        fixture.given()
                .when(new BootChargingStationCommand("CS-001", "MODEL-001"))
                .expectEvents(new ChargingStationCreatedEvent("CS-001", "MODEL-001"), new ChargingStationBootedEvent("CS-001", "MODEL-001"));
    }

}
