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
import io.motown.ocpp.viewmodel.domain.TransactionIdentifierTranslator;
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
        chargingStationRepository.save(chargingStation);
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        log.debug("ChargingStationAcceptedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = chargingStationRepository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setRegistered(true);
            chargingStationRepository.save(chargingStation);
        } else {
            log.error("OCPP module repo COULD NOT FIND CHARGEPOINT {} and mark it as registered", event.getChargingStationId());
        }
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
    public void handle(TransactionStartedEvent event) {
        log.info("TransactionStartedEvent");
    }

    @EventHandler
    public void handle(ConfigurationRequestedEvent event) {
        log.info("Handling ConfigurationRequestedEvent");
        chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());
    }

    @EventHandler
    public void handle(StopTransactionRequestedEvent event) {
        log.info("StopTransactionRequestedEvent");
        chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), TransactionIdentifierTranslator.toInt(event.getTransactionId()));
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        log.info("ChargingStationConfiguredEvent");

        String chargingStationId = event.getChargingStationId().getId();
        ChargingStation chargingStation = chargingStationRepository.findOne(chargingStationId);

        if(chargingStation == null) {
            log.warn("Received a ChargingStationConfiguredEvent for unknown charging station. Creating the chargingStation.");
            chargingStation = new ChargingStation(chargingStationId);
        }

        chargingStation.setNumberOfConnectors(event.getConnectors().size());
        chargingStation.setConfigured(true);

        chargingStationRepository.save(chargingStation);
    }

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }
}
