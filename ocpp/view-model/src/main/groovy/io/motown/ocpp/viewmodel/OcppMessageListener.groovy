package io.motown.ocpp.viewmodel

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.stereotype.Component

@Component
class OcppMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(OcppMessageListener.class);

    @Override
    void onMessage(Message message) {
        log.info("OCPP Message received: ${message.body}")
    }
}
