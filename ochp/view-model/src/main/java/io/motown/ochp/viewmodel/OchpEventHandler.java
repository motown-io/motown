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

import io.motown.domain.api.chargingstation.*;
import io.motown.ochp.viewmodel.persistence.TransactionStatus;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.entities.Identification;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.IdentificationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OchpEventHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(io.motown.ochp.viewmodel.OchpEventHandler.class);

    private ChargingStationRepository chargingStationRepository;

    private TransactionRepository transactionRepository;

    private IdentificationRepository identificationRepository;

    @EventHandler
    public void handle(TransactionStartedEvent event) {
        ChargingStation chargingStation = getChargingStation(event.getChargingStationId());
        Transaction transaction;

        if (chargingStation == null) {
            transaction = new Transaction(event.getTransactionId().getId());
        } else {
            transaction = new Transaction(chargingStation, event.getTransactionId().getId());
        }

        transaction.setIdentificationId(event.getIdentifyingToken().getToken());
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

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        ChargingStation chargingStation = getChargingStation(event.getChargingStationId());

        if (chargingStation == null) {
            String chargingStationId = event.getChargingStationId().getId();
            LOG.info("Storing chargingstation with chargingStationId {}", chargingStationId);

            chargingStation = new ChargingStation(chargingStationId);
            chargingStationRepository.save(chargingStation);
        } else {
            LOG.warn("Received a ChargingStationAcceptedEvent for an already accepted charging station. Skipping the creation of the chargingStation.");
        }
    }

    //TODO: Listen to ChargingStationLocationChangedEvent event in order to obtain the address information - Ingo Pak, 17 Mar 2014

    @EventHandler
    public void handle(AuthorizationResultEvent event) {
        String identificationToken = event.getIdentifyingToken().getToken();
        AuthorizationResultStatus status = event.getAuthenticationStatus();

        Identification identification = identificationRepository.findByIdentificationId(identificationToken);

        if (identification == null) {
            LOG.info("Storing identification {} with status {}", identificationToken, status);
            identification = new Identification(identificationToken, status);
        } else {
            LOG.info("Updating identification {} status to {}", identificationToken, status);
            identification.setAuthorizationStatus(status);
        }

        identificationRepository.save(identification);
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

    public void setIdentificationRepository(IdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }
}
