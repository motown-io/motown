package io.motown.ocpp.viewmodel

import org.springframework.amqp.core.Exchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.Resource

import static org.springframework.amqp.core.BindingBuilder.bind

@Component
class SpringRabbitMqOcppSubscriber implements OcppSubscriber {

    private static final String QUEUE_PREFIX = 'io.motown.queues.ocpp.soap.chargingstations.'

    private static final String ROUTING_KEY_PREFIX = 'io.motown.routingkeys.ocpp.soap.chargingstations.'

    private ConnectionFactory connectionFactory

    private Exchange exchange

    private OcppMessageListener listener

    private SimpleMessageListenerContainer listenerContainer

    private RabbitAdmin rabbitAdmin

    @Override
    void subscribe(String chargingStationId) {
        def queueName = this.generateQueueName(chargingStationId)
        def chargingStationQueue = new org.springframework.amqp.core.Queue(queueName, false, false, false)
        rabbitAdmin.declareQueue(chargingStationQueue)

        String routingKey = this.generateRoutingKey(chargingStationId)

        rabbitAdmin.declareBinding(bind(chargingStationQueue).to(exchange).with(routingKey))

        if (!listenerContainer) {
            listenerContainer = new SimpleMessageListenerContainer(connectionFactory)
            listenerContainer.messageListener = listener
            listenerContainer.queueNames = []
        }

        if (!listenerContainer.queueNames.contains(queueName)) {
            if (listenerContainer.running) {
                listenerContainer.stop()
            }

            listenerContainer.queueNames = (listenerContainer.queueNames.toList() << queueName).toArray()

            listenerContainer.start()
        }
    }

    private String generateQueueName(String chargingStationId) {
        QUEUE_PREFIX + chargingStationId.toLowerCase()
    }

    private String generateRoutingKey(String chargingStationId) {
        ROUTING_KEY_PREFIX + chargingStationId.toLowerCase()
    }

    @Autowired
    void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory
    }

    @Resource(name = 'ocpp-exchange')
    void setExchange(Exchange exchange) {
        this.exchange = exchange
    }

    @Autowired
    void setListener(OcppMessageListener listener) {
        this.listener = listener
    }

    @Autowired
    void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin
    }
}
