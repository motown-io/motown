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

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class TransactionEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionEventListener.class);

    private TransactionRepository repository;

    private ChargingStationRepository chargingStationRepository;

    @EventHandler
    public void handle(TransactionStartedEvent event) {
        LOG.debug("TransactionStartedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = new Transaction(event.getChargingStationId().getId(), event.getTransactionId().getId(), event.getEvseId(), event.getIdentifyingToken().getToken(), event.getMeterStart(), event.getTimestamp());
        repository.save(transaction);

        if (!updateLastContactChargingStation(event.getChargingStationId())) {
            LOG.warn("registered transaction (start) in operator api repo for unknown chargepoint {}", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(TransactionStoppedEvent event) {
        LOG.debug("TransactionStoppedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = repository.findByTransactionId(event.getTransactionId().getId());

        if (transaction == null) {
            LOG.error("cannot find unique transaction with transaction id {}", event.getTransactionId());
        } else {
            transaction.setMeterStop(event.getMeterStop());
            transaction.setStoppedTimestamp(event.getTimestamp());
            repository.save(transaction);
        }

        updateLastContactChargingStation(event.getChargingStationId());
    }

    /**
     * Updates the last contact field of the charging station if it can be found in the repository. Returns true if
     * the update has been performed, false if the charging station cannot be found.
     *
     * @param id charging station identifier.
     * @return true if the field has been updated, false if the charging station cannot be found.
     */
    private boolean updateLastContactChargingStation(ChargingStationId id) {
        ChargingStation chargingStation = chargingStationRepository.findOne(id.getId());
        if (chargingStation != null) {
            chargingStation.setLastContact(new Date());
            chargingStationRepository.save(chargingStation);
        }
        return chargingStation != null;
    }

    public void setRepository(TransactionRepository repository) {
        this.repository = repository;
    }

    public void setChargingStationRepository(ChargingStationRepository chargingStationRepository) {
        this.chargingStationRepository = chargingStationRepository;
    }
}
