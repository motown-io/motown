package io.motown.domain.api.chargingstation;

public class ChargingStationBootedEvent {

    private final String chargingStationId;


    public ChargingStationBootedEvent(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public String getChargingStationId() {
        return this.chargingStationId;
    }

}
