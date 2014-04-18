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

import io.motown.domain.api.chargingstation.ChargingStationSentMeterValuesEvent;
import io.motown.domain.api.chargingstation.MeterValue;
import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionEventListener.class);

    private TransactionRepository repository;

    @EventHandler
    public void handle(TransactionStartedEvent event) {
        LOG.debug("TransactionStartedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = new Transaction(event.getChargingStationId().getId(), event.getTransactionId().getId(), event.getEvseId(), event.getIdentifyingToken().getToken(), event.getMeterStart(), event.getTimestamp());
        repository.save(transaction);
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
    }

    @EventHandler
    public void handle(ChargingStationSentMeterValuesEvent event) {
        LOG.debug("ChargingStationSentMeterValuesEvent for [{}] received!", event.getChargingStationId());

        if (event.getTransactionId() != null) {
            Transaction transaction = repository.findByTransactionId(event.getTransactionId().getId());

            if (transaction != null) {
                for (MeterValue coreMeterValue : event.getMeterValueList()) {
                    transaction.getMeterValues().add(new io.motown.operatorapi.viewmodel.persistence.entities.MeterValue(coreMeterValue.getTimestamp(), coreMeterValue.getValue()));
                }
                repository.save(transaction);
            }
        } else {
            LOG.debug("No transaction found with transactionId [{}]", event.getTransactionId());
        }

    }

    public void setRepository(TransactionRepository repository) {
        this.repository = repository;
    }
}
