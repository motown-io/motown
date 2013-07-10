package io.motown.domain.api.chargingstation;

public class ChargingStationCreatedEvent {

    private final String chargingStationId;

    private final String model;

    public ChargingStationCreatedEvent(String chargingStationId, String model) {
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
