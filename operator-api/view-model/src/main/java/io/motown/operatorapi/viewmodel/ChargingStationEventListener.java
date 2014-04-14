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

import java.util.Date;
import java.util.List;

public class ChargingStationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationEventListener.class);

    private ChargingStationRepository repository;

    private TransactionRepository transactionRepository;

    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        LOG.debug("ChargingStationCreatedEvent creates [{}] in operator api repo", event.getChargingStationId());
        ChargingStation station = new ChargingStation(event.getChargingStationId().getId());
        repository.save(station);
    }

    @EventHandler
    public void handle(ChargingStationBootedEvent event) {
        LOG.debug("ChargingStationBootedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setProtocol(event.getProtocol());
            chargingStation.setLastTimeBooted(new Date());
            chargingStation.setLastContact(new Date());
            repository.save(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as booted", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationSentHeartbeatEvent event) {
        LOG.debug("ChargingStationSentHeartbeatEvent for [{}] received!", event.getChargingStationId());

        if (!updateLastContactChargingStation(event.getChargingStationId())) {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark last contact", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(ChargingStationAcceptedEvent event) {
        LOG.debug("ChargingStationAcceptedEvent for [{}] received!", event.getChargingStationId());

        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            chargingStation.setAccepted(true);
            repository.save(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and mark it as accepted", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(TransactionStartedEvent event) {
        LOG.debug("TransactionStartedEvent for [{}] received!", event.getChargingStationId());

        Transaction transaction = new Transaction(event.getChargingStationId().getId(), event.getTransactionId().getId(), event.getEvseId(), event.getIdentifyingToken().getToken(), event.getMeterStart(), event.getTimestamp());
        transactionRepository.save(transaction);

        if (!updateLastContactChargingStation(event.getChargingStationId())) {
            LOG.warn("registered transaction (start) in operator api repo for unknown chargepoint {}", event.getChargingStationId());
        }
    }

    @EventHandler
    public void handle(TransactionStoppedEvent event) {
        LOG.debug("TransactionStoppedEvent for [{}] received!", event.getChargingStationId());

        List<Transaction> transactions = transactionRepository.findByTransactionId(event.getTransactionId().getId());

        if(transactions.isEmpty() || transactions.size() > 1) {
            LOG.error("cannot find unique transaction with transaction id {}", event.getTransactionId());
        } else {
            Transaction transaction = transactions.get(0);
            transaction.setMeterStop(event.getMeterStop());
            transaction.setStoppedTimestamp(event.getTimestamp());
            transactionRepository.save(transaction);
        }

        updateLastContactChargingStation(event.getChargingStationId());
    }

    @EventHandler
    public void handle(ChargingStationPlacedEvent event) {
        LOG.debug("ChargingStationPlacedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationLocation(event);
    }

    @EventHandler
    public void handle(ChargingStationMovedEvent event) {
        LOG.debug("ChargingStationMovedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationLocation(event);
    }

    @EventHandler
    public void handle(ChargingStationLocationImprovedEvent event) {
        LOG.debug("ChargingStationLocationImprovedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationLocation(event);
    }

    @EventHandler
    public void handle(ChargingStationOpeningTimesSetEvent event) {
        LOG.debug("ChargingStationOpeningTimesSetEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationOpeningTimes(event, true);
    }

    @EventHandler
    public void handle(ChargingStationOpeningTimesAddedEvent event) {
        LOG.debug("ChargingStationOpeningTimesAddedEvent for [{}] received!", event.getChargingStationId());
        updateChargingStationOpeningTimes(event, false);
    }

    @EventHandler
    public void handle(ChargingStationConfiguredEvent event) {
        LOG.debug("ChargingStationConfiguredEvent for [{}] received!", event.getChargingStationId());
    }

    /**
     * Updates the opening times of the charging station.
     *
     * @param event The event which contains the opening times.
     * @param clear Whether to clear the opening times or not.
     * @return {@code true} if the update has been performed, {@code false} if the charging station can't be found.
     */
    private boolean updateChargingStationOpeningTimes(ChargingStationOpeningTimesChangedEvent event, boolean clear) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            if (!event.getOpeningTimes().isEmpty()) {
                if (clear) {
                    chargingStation.getOpeningTimes().clear();
                }

                for (OpeningTime coreOpeningTime : event.getOpeningTimes()) {
                    Day dayOfWeek = coreOpeningTime.getDay();
                    String timeStart = String.format("%02d:%02d", coreOpeningTime.getTimeStart().getHourOfDay(), coreOpeningTime.getTimeStart().getMinutesInHour());
                    String timeStop = String.format("%02d:%02d", coreOpeningTime.getTimeStop().getHourOfDay(), coreOpeningTime.getTimeStop().getMinutesInHour());

                    io.motown.operatorapi.viewmodel.persistence.entities.OpeningTime openingTime = new io.motown.operatorapi.viewmodel.persistence.entities.OpeningTime(dayOfWeek, timeStart, timeStop);
                    chargingStation.getOpeningTimes().add(openingTime);
                }

                repository.save(chargingStation);
            }

        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and update its opening times", event.getChargingStationId());
        }

        return chargingStation != null;
    }

    /**
     * Updates the location of the charging station.
     *
     * @param event The event which contains the data of the location.
     * @return {@code true} if the update has been performed, {@code false} if the charging station can't be found.
     */
    private boolean updateChargingStationLocation(ChargingStationLocationChangedEvent event) {
        ChargingStation chargingStation = repository.findOne(event.getChargingStationId().getId());

        if (chargingStation != null) {
            if (event.getCoordinates() != null) {
                chargingStation.setLatitude(event.getCoordinates().getLatitude());
                chargingStation.setLongitude(event.getCoordinates().getLongitude());
            }

            if (event.getAddress() != null) {
                chargingStation.setAddressline1(event.getAddress().getAddressline1());
                chargingStation.setAddressline2(event.getAddress().getAddressline2());
                chargingStation.setPostalCode(event.getAddress().getPostalCode());
                chargingStation.setCity(event.getAddress().getCity());
                chargingStation.setRegion(event.getAddress().getRegion());
                chargingStation.setCountry(event.getAddress().getCountry());
            }

            chargingStation.setAccessibility(event.getAccessibility());

            repository.save(chargingStation);
        } else {
            LOG.error("operator api repo COULD NOT FIND CHARGEPOINT {} and update its location", event.getChargingStationId());
        }

        return chargingStation != null;
    }

    /**
     * Updates the last contact field of the charging station if it can be found in the repository. Returns true if
     * the update has been performed, false if the charging station cannot be found.
     *
     * @param id charging station identifier.
     * @return true if the field has been updated, false if the charging station cannot be found.
     */
    private boolean updateLastContactChargingStation(ChargingStationId id) {
        ChargingStation chargingStation = repository.findOne(id.getId());
        if (chargingStation != null) {
            chargingStation.setLastContact(new Date());
            repository.save(chargingStation);
        }
        return chargingStation != null;
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
