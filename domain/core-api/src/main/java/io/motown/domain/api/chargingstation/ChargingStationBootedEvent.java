package io.motown.domain.api.chargingstation;

public class ChargingStationBootedEvent {

    private final String chargingStationId;

    private final String model;

    public ChargingStationBootedEvent(String chargingStationId, String model) {
        this.chargingStationId = chargingStationId;
        this.model = model;
    }

    public String getChargingStationId() {
        return this.chargingStationId;
    }

    public String getModel() {
        return this.model;
    }
}
