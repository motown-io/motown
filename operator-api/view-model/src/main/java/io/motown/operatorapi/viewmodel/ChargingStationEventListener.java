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

import io.motown.domain.api.chargingstation.*;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ChargingStationEventListener {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationEventListener.class);

    private ChargingStationRepository repository;

    private TransactionRepository transactionRepository;

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        log.debug("ChargingStationCreatedEvent creates [{}] in operator api repo", event.getChargingStationId());
        ChargingStation station = new ChargingStation(event.getChargingStationId().getId());
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

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        log.debug("ChargingStationAcceptedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setAccepted(true);
            repository.save(chargingStation);
        } else {
            log.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as accepted", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(TransactionStartedEvent event) {
        log.debug("TransactionStartedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = new Transaction(event.getChargingStationId().getId(), event.getTransactionId().getId(), event.getConnectorId(), event.getIdentifyingToken().getToken(), event.getMeterStart(), event.getTimestamp());
        transactionRepository.save(transaction);

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());
        if (chargingStation != null) {
            log.warn("registered transaction (start) in operator api repo for unknown chargepoint {}", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(TransactionStoppedEvent event) {
        log.debug("TransactionStoppedEvent for [{}] received!", event.getChargingStationId());

        List<Transaction> transactions = transactionRepository.findByTransactionId(event.getTransactionId());

        if(transactions.isEmpty() || transactions.size() > 1) {
            log.error("cannot find unique transaction with transaction id {}", event.getTransactionId());
        } else {
            Transaction transaction = transactions.get(0);
            transaction.setMeterStop(event.getMeterValueStop());
            transaction.setStoppedTimestamp(event.getTimestamp());
            transactionRepository.save(transaction);
        }
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
