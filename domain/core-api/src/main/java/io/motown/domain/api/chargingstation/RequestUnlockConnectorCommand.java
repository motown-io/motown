package io.motown.domain.api.chargingstation;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class RequestUnlockConnectorCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final Integer connectorId;

    public RequestUnlockConnectorCommand(ChargingStationId chargingStationId, Integer connectorId) {
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