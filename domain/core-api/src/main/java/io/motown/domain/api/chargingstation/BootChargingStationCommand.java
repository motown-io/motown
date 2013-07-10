package io.motown.domain.api.chargingstation;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class BootChargingStationCommand {

    @TargetAggregateIdentifier
    private final String chargingStationId;

    private final String model;

    public BootChargingStationCommand(String chargingStationId, String model) {
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
