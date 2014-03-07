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
package io.motown.ochp.viewmodel;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.TransactionId;
import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.ochp.viewmodel.persistence.TransactionStatus;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OchpEventHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(io.motown.ochp.viewmodel.OchpEventHandler.class);

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //TODO: Add eventhandlers for keeping internal OCHP state up to date - Ingo Pak, 05 Mar 2014
    
    @EventHandler
    public void handle(TransactionStartedEvent event) {
        ChargingStation chargingStation = getChargingStation(event.getChargingStationId());
        Transaction transaction;

        if (chargingStation == null) {
            transaction = new Transaction(event.getTransactionId().getId());
        } else {
            transaction = new Transaction(chargingStation, event.getTransactionId().getId());
        }

        transaction.setEvseId(event.getEvseId().getId());
        transaction.setIdentifyingToken(event.getIdentifyingToken().getToken());
        transaction.setMeterStart(event.getMeterStart());
        transaction.setTimeStart(event.getTimestamp());
        transaction.setAttributes(event.getAttributes());
        transaction.setStatus(TransactionStatus.STARTED);

        transactionRepository.save(transaction);
    }

    @EventHandler
    public void handle(TransactionStoppedEvent event) {
        Transaction transaction = getTransaction(event.getTransactionId());

        if (transaction != null) {
            transaction.setMeterStop(event.getMeterStop());
            transaction.setTimeStop(event.getTimestamp());
            transaction.setStatus(TransactionStatus.STOPPED);

            transactionRepository.save(transaction);
        }
    }

    /**
     * Get a charging station from the repository based on the charging station id.
     * @param chargingStationId The identifier of the charging station.
     * @return The charging station.
     */
    private ChargingStation getChargingStation(ChargingStationId chargingStationId) {
        ChargingStation chargingStation = chargingStationRepository.findByChargingStationId(chargingStationId.getId());

        if (chargingStation == null) {
            LOG.error("Could not find charging station '{}'", chargingStationId);
        }

        return chargingStation;
    }

    /**
     * Get a transaction from the repository based on the transaction id.
     * @param transactionId The identifier of the transaction.
     * @return The transaction.
     */
    private Transaction getTransaction(TransactionId transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId.getId());

        if (transaction == null) {
            LOG.error("Could not find transaction '{}'", transactionId);
        }

        return transaction;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
