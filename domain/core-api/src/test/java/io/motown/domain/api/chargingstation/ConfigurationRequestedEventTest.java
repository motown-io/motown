package io.motown.domain.api.chargingstation;

import org.junit.Test;

public class ConfigurationRequestedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new ConfigurationRequestedEvent(null);
    }
}
