package io.motown.operatorapi.viewmodel;

import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChargingStationEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationEventListener.class);

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        log.info("ChargingStationBootedEvent for ChargingStation [{}] received!", event.getChargingStationId());
    }
}
