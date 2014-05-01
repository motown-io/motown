package io.motown.domain.api.chargingstation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class UnlockEvseRequestedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new UnlockEvseRequestedEvent(null, PROTOCOL, EVSE_ID, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithProtocolNull() {
        new UnlockEvseRequestedEvent(CHARGING_STATION_ID, null, EVSE_ID, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithEvseIdNull() {
        new UnlockEvseRequestedEvent(CHARGING_STATION_ID, PROTOCOL, null, IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithIdentityContextNull() {
        new UnlockEvseRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, null);
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(UnlockEvseRequestedEvent.class).usingGetClass().verify();
    }
}
