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

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ChargingStationSentHeartbeatEvent;
import io.motown.domain.api.chargingstation.HardResetChargingStationRequestedEvent;
import org.axonframework.domain.EventMessage;
import org.axonframework.domain.GenericEventMessage;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class MotownRoutingKeyResolverTest {

    private MotownRoutingKeyResolver resolver;

    private String routingKeyPrefix;

    @Before
    public void setUp() throws Exception {
        resolver = new MotownRoutingKeyResolver();

        // access private member to be able to verify routing key based on this private member
        Field f = MotownRoutingKeyResolver.class.getDeclaredField("ROUTING_KEY_PREFIX");
        f.setAccessible(true);
        routingKeyPrefix = (String) f.get(resolver);
    }

    @Test
    public void testResolveRoutingKeyCommunicationWithChargingStationEvent() {
        String protocol = "OCPP15S";

        EventMessage message = new GenericEventMessage<>(new HardResetChargingStationRequestedEvent(getChargingStationId(), protocol));

        String routingKey = resolver.resolveRoutingKey(message);
        String expected = routingKeyPrefix + protocol;

        assertEquals(expected, routingKey);
    }

    @Test
    public void testResolveRoutingKeyNoCommunicationEvent() {
        EventMessage message = new GenericEventMessage<>(new ChargingStationSentHeartbeatEvent(getChargingStationId()));

        String routingKey = resolver.resolveRoutingKey(message);
        String expected = ChargingStationSentHeartbeatEvent.class.getPackage().getName();

        assertEquals(expected, routingKey);
    }

    private ChargingStationId getChargingStationId() {
        return new ChargingStationId("id");
    }

}
