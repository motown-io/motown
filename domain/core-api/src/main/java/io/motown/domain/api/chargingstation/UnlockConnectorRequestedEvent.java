package io.motown.domain.api.chargingstation;

public class UnlockConnectorRequestedEvent {
    private final String chargingStationId;
    private final Integer connectorId;

    public UnlockConnectorRequestedEvent(String chargingStationId, Integer connectorId) {
        this.chargingStationId = chargingStationId;
        this.connectorId = connectorId;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

}
