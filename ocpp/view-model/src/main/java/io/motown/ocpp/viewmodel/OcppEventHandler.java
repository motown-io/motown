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
package io.motown.ocpp.viewmodel;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OcppEventHandler {
    private static final Logger log = LoggerFactory.getLogger(io.motown.ocpp.viewmodel.OcppEventHandler.class);

    @Autowired
    private ChargingStationOcpp15Client chargingStationOcpp15Client;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        log.info("ChargingStationBootedEvent");
    }

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        log.info("Handling ChargingStationCreatedEvent");
        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if(chargingStation == null){
            chargingStation = new ChargingStation(chargingStationId);
        }

        chargingStation.setRegistered(event.isAccepted());

        chargingStationRepository.save(chargingStation);
    }

    @EventHandler
    public void handle(ChargingStationRegisteredEvent event) {
        log.info("ChargingStationRegisteredEvent");
    }

    @EventHandler
    public void handle(ConnectorNotFoundEvent event) {
        log.info("ConnectorNotFoundEvent");
    }

    @EventHandler
    public void handle(UnlockConnectorRequestedEvent event) {
        log.info("UnlockConnectorRequestedEvent");
    }

    @EventHandler
    public void handle(ConfigurationRequestedEvent event) {
        log.info("Handling ConfigurationRequestedEvent");
        chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());
    }

}
