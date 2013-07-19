package io.motown.domain.api.chargingstation;

public class ChargingStationBootedEvent {

    private final ChargingStationId chargingStationId;


    public ChargingStationBootedEvent(ChargingStationId chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

}
