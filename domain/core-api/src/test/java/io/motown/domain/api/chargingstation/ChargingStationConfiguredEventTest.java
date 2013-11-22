package io.motown.domain.api.chargingstation;

import org.junit.Test;

public class ChargingStationConfiguredEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdAndConnectorsNull() {
        new ChargingStationConfiguredEvent(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithConnectorsNull() {
        new ChargingStationConfiguredEvent(new ChargingStationId("CS-001"), null);
    }
}
