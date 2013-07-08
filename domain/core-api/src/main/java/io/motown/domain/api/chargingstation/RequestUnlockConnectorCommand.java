package io.motown.domain.api.chargingstation;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class RequestUnlockConnectorCommand {

    @TargetAggregateIdentifier
    private final String chargingStationId;
    private final Integer connectorId;

    public RequestUnlockConnectorCommand(String chargingStationId, Integer connectorId) {
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