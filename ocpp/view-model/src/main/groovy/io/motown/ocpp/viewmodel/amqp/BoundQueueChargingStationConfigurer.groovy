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
package io.motown.ocpp.viewmodel.amqp

import io.motown.domain.api.chargingstation.ChargingStationId
import io.motown.ocpp.viewmodel.ChargingStationConfigurer
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue as AmqpQueue
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.Resource

import static org.springframework.amqp.core.BindingBuilder.bind

@Component
class BoundQueueChargingStationConfigurer implements ChargingStationConfigurer {

    @Resource(name = "ocppExchange")
    Exchange exchange

    @Autowired
    ChargingStationQueueNameProvider queueNameProvider

    @Autowired
    RabbitAdmin rabbitAdmin

    @Autowired
    ChargingStationRoutingKeyProvider routingKeyProvider

    @Override
    void configure(ChargingStationId chargingStationId) {
        AmqpQueue queue = createQueue(chargingStationId)
        bindQueue(chargingStationId, queue)
    }

    private AmqpQueue createQueue(ChargingStationId chargingStationId) {
        def queueName = queueNameProvider.getQueueName(chargingStationId)
        def chargingStationQueue = new AmqpQueue(queueName, false, false, false)
        rabbitAdmin.declareQueue(chargingStationQueue)
        return chargingStationQueue
    }

    private void bindQueue(ChargingStationId chargingStationId, AmqpQueue queue) {
        String routingKey = routingKeyProvider.getRoutingKey(chargingStationId)
        rabbitAdmin.declareBinding(bind(queue).to(exchange).with(routingKey))
    }
}
