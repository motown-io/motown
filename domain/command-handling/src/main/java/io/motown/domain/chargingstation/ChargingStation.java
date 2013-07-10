package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.UnlockConnectorRequestedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class ChargingStation extends AbstractAnnotatedAggregateRoot {

    @AggregateIdentifier
    private String id;

    ChargingStation() {
    }

    ChargingStation(String id, String model) {
        apply(new ChargingStationCreatedEvent(id, model));
    }

    public void boot(String chargingStationId, String model) {
        apply(new ChargingStationBootedEvent(chargingStationId, model));
    }

    public void requestUnlockConnector(int connectorId) {
        apply(new UnlockConnectorRequestedEvent(this.id, connectorId));
    }

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        this.id = event.getChargingStationId();
    }
}
