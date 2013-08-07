package io.motown.domain.api.chargingstation;

public class UnlockConnectorRequestedEvent implements CommunicationWithChargingStationRequestedEvent {
    private final ChargingStationId chargingStationId;
    private final Integer connectorId;

    public UnlockConnectorRequestedEvent(ChargingStationId chargingStationId, Integer connectorId) {
        this.chargingStationId = chargingStationId;
        this.connectorId = connectorId;
    }

    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public Integer getConnectorId() {
        return connectorId;
    }

}
