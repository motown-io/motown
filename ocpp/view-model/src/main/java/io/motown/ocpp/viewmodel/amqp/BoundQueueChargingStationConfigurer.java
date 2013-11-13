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
package io.motown.ocpp.viewmodel.amqp;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.viewmodel.ChargingStationConfigurer;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.amqp.core.BindingBuilder.bind;

@Component
public class BoundQueueChargingStationConfigurer implements ChargingStationConfigurer {

    @Resource(name = "ocppExchange")
    private Exchange exchange;
    @Autowired
    private ChargingStationQueueNameProvider queueNameProvider;
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private ChargingStationRoutingKeyProvider routingKeyProvider;

    @Override
    public void configure(ChargingStationId chargingStationId) {
        Queue queue = createQueue(chargingStationId);
        bindQueue(chargingStationId, queue);
    }

    private Queue createQueue(ChargingStationId chargingStationId) {
        String queueName = queueNameProvider.getQueueName(chargingStationId);
        Queue chargingStationQueue = new Queue(queueName, false, false, false);
        rabbitAdmin.declareQueue(chargingStationQueue);
        return chargingStationQueue;
    }

    private void bindQueue(ChargingStationId chargingStationId, Queue queue) {
        String routingKey = routingKeyProvider.getRoutingKey(chargingStationId);
        rabbitAdmin.declareBinding(bind(queue).to(exchange).with(routingKey).noargs());
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public ChargingStationQueueNameProvider getQueueNameProvider() {
        return queueNameProvider;
    }

    public void setQueueNameProvider(ChargingStationQueueNameProvider queueNameProvider) {
        this.queueNameProvider = queueNameProvider;
    }

    public RabbitAdmin getRabbitAdmin() {
        return rabbitAdmin;
    }

    public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    public ChargingStationRoutingKeyProvider getRoutingKeyProvider() {
        return routingKeyProvider;
    }

    public void setRoutingKeyProvider(ChargingStationRoutingKeyProvider routingKeyProvider) {
        this.routingKeyProvider = routingKeyProvider;
    }

}
