package io.motown.domain.api.chargingstation;

/**
 */
public class ConnectorNotFoundEvent {
    private final String chargingStationId;
    private final Integer connectorId;

    public ConnectorNotFoundEvent(String chargingStationId, Integer connectorId) {
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
