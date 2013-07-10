package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.RequestUnlockConnectorCommand;
import io.motown.domain.api.chargingstation.UnlockConnectorRequestedEvent;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class ChargingStationTest {

    private FixtureConfiguration<ChargingStation> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
        ChargingStationCommandHandler commandHandler = new ChargingStationCommandHandler();
        commandHandler.setRepository(fixture.getRepository());
        fixture.registerAnnotatedCommandHandler(commandHandler);
    }

    @Test
    public void testRequestUnlockConnector() {
        fixture.given(new ChargingStation("CS-001"))
                .when(new RequestUnlockConnectorCommand("CS-001", 1))
                .expectEvents(new UnlockConnectorRequestedEvent("CS-001", 1));
    }
}
