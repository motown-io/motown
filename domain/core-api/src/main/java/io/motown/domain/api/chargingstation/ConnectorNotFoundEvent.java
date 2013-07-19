package io.motown.domain.api.chargingstation;

/**
 */
public class ConnectorNotFoundEvent {
    private final ChargingStationId chargingStationId;
    private final Integer connectorId;

    public ConnectorNotFoundEvent(ChargingStationId chargingStationId, Integer connectorId) {
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
