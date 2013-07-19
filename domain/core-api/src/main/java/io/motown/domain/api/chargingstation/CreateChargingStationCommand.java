package io.motown.domain.api.chargingstation;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;

/**
 * The Boot command contains all information about a chargepoint.
 * When the point does not exist (yet), you have the option to create it.
 */
public class CreateChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String model;

    private final List<Connector> connectors;

    public CreateChargingStationCommand(ChargingStationId chargingStationId, String model, List<Connector> connectors) {
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
