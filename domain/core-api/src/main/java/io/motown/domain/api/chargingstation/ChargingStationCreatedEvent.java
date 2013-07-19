package io.motown.domain.api.chargingstation;

import java.util.List;

public class ChargingStationCreatedEvent {

    private final ChargingStationId chargingStationId;

    private final String model;

    private final List<Connector> connectors;

    public ChargingStationCreatedEvent(ChargingStationId chargingStationId, String model, List<Connector> connectors) {
        this.chargingStationId = chargingStationId;
        this.model = model;
        this.connectors = connectors;
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    public String getModel() {
        return this.model;
    }

    public List<Connector> getConnectors() {
        return this.connectors;
    }
}
