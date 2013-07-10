package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.UnlockConnectorRequestedEvent;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class ChargingStation extends AbstractAnnotatedAggregateRoot {

    @AggregateIdentifier
    private String id;

    // TODO Remove or update this constructor once we can create or register charging stations using commands.
    ChargingStation() {
    }

    // TODO Remove or update this constructor once we can create or register charging stations using commands.
    ChargingStation(String id) {
        this.id = id;
    }

    public void requestUnlockConnector(int connectorId) {
        apply(new UnlockConnectorRequestedEvent(this.id, connectorId));
    }
}
