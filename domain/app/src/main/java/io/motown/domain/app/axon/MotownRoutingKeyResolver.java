package io.motown.domain.app.axon;

import io.motown.domain.api.chargingstation.CommunicationWithChargingStationRequestedEvent;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.amqp.PackageRoutingKeyResolver;
import org.axonframework.eventhandling.amqp.RoutingKeyResolver;

public class MotownRoutingKeyResolver implements RoutingKeyResolver {

    private static final String ROUTING_KEY_PREFIX = "io.motown.routingkeys.ocpp.soap.chargingstations.";

    private PackageRoutingKeyResolver packageRoutingKeyResolver;

    public MotownRoutingKeyResolver() {
        this.packageRoutingKeyResolver = new PackageRoutingKeyResolver();
    }

    @Override
    public String resolveRoutingKey(EventMessage eventMessage) {
        if (eventMessage.getPayload() instanceof CommunicationWithChargingStationRequestedEvent) {
            CommunicationWithChargingStationRequestedEvent event = (CommunicationWithChargingStationRequestedEvent) eventMessage.getPayload();
            return ROUTING_KEY_PREFIX + event.getChargingStationId().getId().toLowerCase();
        } else {
            return this.packageRoutingKeyResolver.resolveRoutingKey(eventMessage);
        }
    }
}
