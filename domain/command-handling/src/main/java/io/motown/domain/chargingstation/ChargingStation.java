package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import java.util.List;

public class ChargingStation extends AbstractAnnotatedAggregateRoot {


    @AggregateIdentifier
    private String id;

    private List<Connector> connectors;

    protected ChargingStation() {
    }

    @CommandHandler
    public ChargingStation(CreateChargingStationCommand command) {
        this();

        apply(new ChargingStationCreatedEvent(command.getChargingStationId(), command.getModel(), command.getConnectors()));
    }

    @CommandHandler
    public void handle(BootChargingStationCommand command) {
        apply(new ChargingStationBootedEvent(id));
    }

    @CommandHandler
    public void handle(RequestUnlockConnectorCommand command) {
        if (command.getConnectorId() > connectors.size()) {
            apply(new ConnectorNotFoundEvent(this.id, command.getConnectorId()));
        } else {
            if (command.getConnectorId() == Connector.ALL) {
               for(Connector connector: connectors) {
                   apply(new UnlockConnectorRequestedEvent(this.id, connector.getConnectorId()));
               }
            } else {
                apply(new UnlockConnectorRequestedEvent(this.id, command.getConnectorId()));
            }
        }
    }


    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        this.id = event.getChargingStationId();
        this.connectors = event.getConnectors();
    }
}
