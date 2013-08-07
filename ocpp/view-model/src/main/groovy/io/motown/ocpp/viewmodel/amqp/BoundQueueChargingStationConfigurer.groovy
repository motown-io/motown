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
