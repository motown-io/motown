package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.UnlockConnectorRequestedEvent

import org.axonframework.eventhandling.annotation.EventHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OcppRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(OcppRequestHandler.class);

    @EventHandler
    void handle(UnlockConnectorRequestedEvent event) {
        log.info("UnlockConnectorRequestedEvent")
    }
}
