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
package io.motown.operatorapi.viewmodel;

import io.motown.domain.api.chargingstation.ChargingStationBootedEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.ChargingStationRegisteredEvent;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ChargingStationEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationEventListener.class);

    private ChargingStationRepository repository;

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        log.debug("ChargingStationCreatedEvent creates [{}] in operator api repo", event.getChargingStationId());
        ChargingStation station = new ChargingStation(event.getChargingStationId().getId());
        station.setAccepted(event.isAccepted());
        repository.save(station);
    }

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        log.debug("ChargingStationBootedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setLastTimeBooted(new Date());
            repository.save(chargingStation);
        } else {
            log.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as booted", event.getChargingStationId());
        }
    }

//    @EventHandler
//    public void handle(ChargingStationRegisteredEvent event) {
//        log.debug("ChargingStationBootedEvent for [{}] received!", event.getChargingStationId());
//
//        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());
//
//        if (chargingStation != null) {
//            chargingStation.setAccepted(true);
//            repository.save(chargingStation);
//        } else {
//            log.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as registered", event.getChargingStationId());
//        }
//    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}
