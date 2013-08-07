package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.ChargingStationBootedEvent
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent
import io.motown.domain.api.chargingstation.ConnectorNotFoundEvent
import io.motown.domain.api.chargingstation.UnlockConnectorRequestedEvent

import org.axonframework.eventhandling.annotation.EventHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OcppEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OcppEventHandler.class);

    @EventHandler
    void handle(ChargingStationBootedEvent event) {
        log.info("ChargingStationBootedEvent")
    }

    @EventHandler
    void handle(ChargingStationCreatedEvent event) {
        log.info("ChargingStationCreatedEvent")
    }

    @EventHandler
    void handle(ConnectorNotFoundEvent event) {
        log.info("ConnectorNotFoundEvent")
    }

    @EventHandler
    void handle(UnlockConnectorRequestedEvent event) {
        log.info("UnlockConnectorRequestedEvent")
    }
}
