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
package io.motown.vas.viewmodel;

import io.motown.domain.api.chargingstation.ChargingStationAcceptedEvent;
import io.motown.domain.api.chargingstation.ChargingStationConfiguredEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;
import io.motown.vas.viewmodel.persistence.repostories.ChargingStationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VasEventHandler {
    private static final Logger log = LoggerFactory.getLogger(io.motown.vas.viewmodel.VasEventHandler.class);

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    //TODO: Determine which event handlers are necessary to keep the model state up to date - Ingo Pak, 22 Jan 2014

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        log.info("Handling ChargingStationCreatedEvent");
        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if (chargingStation == null) {
            chargingStation = new ChargingStation(chargingStationId);
        }
        chargingStationRepository.save(chargingStation);
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        log.debug("ChargingStationAcceptedEvent for {} received!", event.getChargingStationId());

        ChargingStation chargingStation = chargingStationRepository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setRegistered(true);
            chargingStationRepository.save(chargingStation);
        } else {
            log.error("Could not find charging station {} and mark it as registered", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        log.info("ChargingStationConfiguredEvent");

        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if (chargingStation == null) {
            log.warn("Received a ChargingStationConfiguredEvent for unknown charging station. Creating the chargingStation.");
            chargingStation = new ChargingStation(chargingStationId);
        }

        chargingStation.setNumberOfConnectors(event.getConnectors().size());
        chargingStation.setConfigured(true);

        chargingStationRepository.save(chargingStation);
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }
}
