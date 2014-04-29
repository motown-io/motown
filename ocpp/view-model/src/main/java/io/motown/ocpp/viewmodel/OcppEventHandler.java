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

import io.motown.domain.api.chargingstation.ChargingStationAcceptedEvent;
import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import io.motown.domain.api.chargingstation.ChargingStationConfiguredEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OcppEventHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(io.motown.ocpp.viewmodel.OcppEventHandler.class);

    private ChargingStationRepository chargingStationRepository;

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        LOG.info("Handling ChargingStationCreatedEvent");
        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if (chargingStation == null) {
            chargingStation = new ChargingStation(chargingStationId);
            chargingStationRepository.createOrUpdate(chargingStation);
        }
    }

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        LOG.info("Handling ChargingStationBootedEvent");

        ChargingStation chargingStation = chargingStationRepository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setProtocol(event.getProtocol());
            chargingStationRepository.createOrUpdate(chargingStation);
        } else {
            LOG.info("OCPP module repo could not find charging station {} and set the protocol", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        LOG.debug("ChargingStationAcceptedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = chargingStationRepository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setRegistered(true);
            chargingStationRepository.createOrUpdate(chargingStation);
        } else {
            LOG.info("OCPP module repo COULD NOT FIND CHARGEPOINT {} and mark it as registered", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        LOG.info("ChargingStationConfiguredEvent");

        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if (chargingStation == null) {
            LOG.info("Received a ChargingStationConfiguredEvent for unknown charging station. Creating the chargingStation.");
            chargingStation = new ChargingStation(chargingStationId);
        }

        chargingStation.setNumberOfEvses(event.getEvses().size());
        chargingStation.setConfigured(true);
        chargingStationRepository.createOrUpdate(chargingStation);
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }
}
