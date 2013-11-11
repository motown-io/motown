/**
 * Copyright (C) 2013 Alliander N.V. (info@motown.io)
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
package io.motown.ocpp.viewmodel.axon

import io.motown.domain.api.chargingstation.ChargingStationId
import io.motown.ocpp.viewmodel.ChargingStationSubscriber
import io.motown.ocpp.viewmodel.amqp.ChargingStationQueueNameProvider
import org.axonframework.eventhandling.Cluster
import org.axonframework.eventhandling.EventBusTerminal
import org.axonframework.eventhandling.EventListener as AxonEventListener
import org.axonframework.eventhandling.amqp.AMQPConsumerConfiguration
import org.axonframework.eventhandling.amqp.spring.SpringAMQPConsumerConfiguration
import org.axonframework.eventhandling.async.AsynchronousCluster
import org.axonframework.eventhandling.async.FullConcurrencyPolicy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.concurrent.ExecutorService

@Component
class ClusterChargingStationSubscriber implements ChargingStationSubscriber {

    @Autowired
    ExecutorService asyncExecutor

    @Autowired
    ChargingStationQueueNameProvider queueNameProvider

    @Autowired
    AxonEventListener eventListener

    @Autowired
    EventBusTerminal terminal

    @Override
    void subscribe(ChargingStationId chargingStationId) {
        def queueName = queueNameProvider.getQueueName(chargingStationId)

        def config = new SpringAMQPConsumerConfiguration()
        config.queueName = queueName
        config.exclusive = false

        Cluster cluster = new AsynchronousCluster(queueName, asyncExecutor, new FullConcurrencyPolicy())
        cluster.subscribe(eventListener)
        cluster.metaData.setProperty(AMQPConsumerConfiguration.AMQP_CONFIG_PROPERTY, config)

        terminal.onClusterCreated(cluster)
    }
}
