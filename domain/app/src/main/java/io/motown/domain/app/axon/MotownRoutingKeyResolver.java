/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.domain.app.axon;

import io.motown.domain.api.chargingstation.CommunicationWithChargingStationRequestedEvent;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.amqp.PackageRoutingKeyResolver;
import org.axonframework.eventhandling.amqp.RoutingKeyResolver;

public class MotownRoutingKeyResolver implements RoutingKeyResolver {

    private static final String ROUTING_KEY_PREFIX = "io.motown.routingkeys.ocpp.soap.chargingstations.";

    private RoutingKeyResolver routingKeyResolver;

    public MotownRoutingKeyResolver() {
        this.routingKeyResolver = new PackageRoutingKeyResolver();
    }

    @Override
    public String resolveRoutingKey(EventMessage eventMessage) {
        if (eventMessage.getPayload() instanceof CommunicationWithChargingStationRequestedEvent) {
            CommunicationWithChargingStationRequestedEvent event = (CommunicationWithChargingStationRequestedEvent) eventMessage.getPayload();
            return ROUTING_KEY_PREFIX + event.getProtocol();
        } else {
            return this.routingKeyResolver.resolveRoutingKey(eventMessage);
        }
    }
}
