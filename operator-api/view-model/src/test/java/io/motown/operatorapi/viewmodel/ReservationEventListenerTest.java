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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.EVSE_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTITY_CONTEXT;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.RESERVATION_ID;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableSet;

import io.motown.domain.api.chargingstation.ChargingStationOccupiedEvent;
import io.motown.domain.api.chargingstation.ChargingStationUnavailableEvent;
import io.motown.domain.api.chargingstation.ComponentStatus;
import io.motown.domain.api.chargingstation.ReservationCancelledEvent;
import io.motown.domain.api.chargingstation.ReservationFaultedEvent;
import io.motown.domain.api.chargingstation.ReservationRejectedEvent;
import io.motown.domain.api.chargingstation.ReservationStatus;
import io.motown.domain.api.chargingstation.ReservedNowEvent;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Evse;
import io.motown.operatorapi.viewmodel.persistence.entities.Reservation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.ReservationRepository;

@ContextConfiguration("classpath:operator-api-view-model-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationEventListenerTest {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    private ReservationEventListener listener;

    @Before
    public void setUp() throws Exception {
        listener = new ReservationEventListener();
        listener.setRepository(repository);
    }

    @Test
    public void testHandleReservedNowEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new ReservedNowEvent(CHARGING_STATION_ID, RESERVATION_ID, EVSE_ID, new Date(), IDENTITY_CONTEXT));
        Reservation reservation = repository.findByReservationId(RESERVATION_ID);
        assertNotNull(reservation);
        assert(ReservationStatus.ACCEPTED.equals(reservation.getStatus()));
    }

    @Test
    public void testHandleChargingStationOccupiedEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new ChargingStationOccupiedEvent(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT));
        Reservation reservation = repository.findByChargingStationIdEvseIdUserId(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT.getUserIdentity());
        assertNotNull(reservation);
        assert(ReservationStatus.OCCUPIED.equals(reservation.getStatus()));
    }

    @Test
    public void testHandleChargingStationUnavailableEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new ChargingStationUnavailableEvent(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT));
        Reservation reservation = repository.findByChargingStationIdEvseIdUserId(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT.getUserIdentity());
        assertNotNull(reservation);
        assert(ReservationStatus.UNAVAILABLE.equals(reservation.getStatus()));
    }

    @Test
    public void testHandleReservationFaultedEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new ReservationFaultedEvent(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT));
        Reservation reservation = repository.findByChargingStationIdEvseIdUserId(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT.getUserIdentity());
        assertNotNull(reservation);
        assert(ReservationStatus.FAULTED.equals(reservation.getStatus()));
    }
    
    @Test
    public void testHandleReservationRejectedEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new ReservationRejectedEvent(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT));
        Reservation reservation = repository.findByChargingStationIdEvseIdUserId(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT.getUserIdentity());
        assertNotNull(reservation);
        assert(ReservationStatus.REJECTED.equals(reservation.getStatus()));
    }
    
    @Test
    public void testHandleReservationCancelledEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        //create reservation
        listener.handle(new ReservedNowEvent(CHARGING_STATION_ID, RESERVATION_ID, EVSE_ID, new Date(), IDENTITY_CONTEXT));
        //cancel reservation
        listener.handle(new ReservationCancelledEvent(CHARGING_STATION_ID, RESERVATION_ID, IDENTITY_CONTEXT));
        Reservation reservation = repository.findByReservationId(RESERVATION_ID);
        assertNotNull(reservation);
        assert(ReservationStatus.CANCELLED.equals(reservation.getStatus()));
    }

 }
