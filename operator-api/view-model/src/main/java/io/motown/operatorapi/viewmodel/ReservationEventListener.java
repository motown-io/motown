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
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.viewmodel.persistence.entities.Reservation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ReservationRepository;

import java.util.Date;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReservationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationEventListener.class);

    private ReservationRepository repository;

    public void setRepository(ReservationRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(ReservedNowEvent event) {
        LOG.debug("ReservedNowEvent for [{}] received!", event.getChargingStationId());
        
        createReservation(event.getChargingStationId(), ReservationStatus.ACCEPTED, event.getReservationId().getId(), 
        		event.getIdentityContext().getUserIdentity(), event.getEvseId(), event.getExpiryDate());
    }

    private void createReservation(ChargingStationId chargingStationId, ReservationStatus status, String reservationId, UserIdentity userId, EvseId evseId, Date expiryDate) 
    {
        Reservation reservation = new Reservation(chargingStationId.getId(), 
        		status, 
        		reservationId, 
        		userId.getId(), 
        		evseId, 
        		expiryDate);
        repository.createOrUpdate(reservation);
    }
    
    @EventHandler
    public void handle(ReservationFaultedEvent event) {
        LOG.debug("ReservationFaultedEvent for [{}] received!", event.getChargingStationId());
        
        createReservation(event.getChargingStationId(), ReservationStatus.FAULTED, null, 
        		event.getIdentityContext().getUserIdentity(), event.getEvseId(), null);
    }

    @EventHandler
    public void handle(ReservationRejectedEvent event) {
        LOG.debug("ReservationRejectedEvent for [{}] received!", event.getChargingStationId());
        
        createReservation(event.getChargingStationId(), ReservationStatus.REJECTED, null, 
        		event.getIdentityContext().getUserIdentity(), event.getEvseId(), null);
    }

    @EventHandler
    public void handle(ChargingStationUnavailableEvent event) {
        LOG.debug("ChargingStationUnavailableEvent for [{}] received!", event.getChargingStationId());

        createReservation(event.getChargingStationId(), ReservationStatus.UNAVAILABLE, null, 
        		event.getIdentityContext().getUserIdentity(), event.getEvseId(), null);
    }

    @EventHandler
    public void handle(ChargingStationOccupiedEvent event) {
        LOG.debug("ChargingStationOccupiedEvent for [{}] received!", event.getChargingStationId());
        
        createReservation(event.getChargingStationId(), ReservationStatus.OCCUPIED, null, 
        		event.getIdentityContext().getUserIdentity(), event.getEvseId(), null);
    }
    
    @EventHandler
    public void handle(ReservationCancelledEvent event) {
        LOG.debug("ReservationCancelledEvent for [{}] received!", event.getChargingStationId());

        Reservation reservation = repository.findByReservationId(event.getReservationId());
        reservation.setStatus(ReservationStatus.CANCELLED);
        
        repository.createOrUpdate(reservation);
    }

}
