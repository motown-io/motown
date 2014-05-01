package io.motown.domain.api.chargingstation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class SoftResetChargingStationRequestedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNull() {
        new SoftResetChargingStationRequestedEvent(null, PROTOCOL, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithProtocolNull() {
        new SoftResetChargingStationRequestedEvent(CHARGING_STATION_ID, null, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWitIdentityContextNull() {
        new SoftResetChargingStationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, null);
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(SoftResetChargingStationRequestedEvent.class).usingGetClass().verify();
    }
}
