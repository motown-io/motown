package io.motown.domain.api.chargingstation;

public class UnlockConnectorRequestedEvent {
    private final ChargingStationId chargingStationId;
    private final Integer connectorId;

    public UnlockConnectorRequestedEvent(ChargingStationId chargingStationId, Integer connectorId) {
        this.chargingStationId = chargingStationId;
        this.connectorId = connectorId;
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

}
