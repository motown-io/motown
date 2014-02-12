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
package io.motown.vas.viewmodel;

import io.motown.domain.api.chargingstation.*;
import io.motown.vas.viewmodel.model.ChargingStation;
import io.motown.vas.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.*;
import static io.motown.vas.viewmodel.VasViewModelTestUtils.getRegisteredAndConfiguredChargingStation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;

@ContextConfiguration("classpath:vas-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class VasEventHandlerTest {

    private VasEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        eventHandler = new VasEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
    }

    @Test
    public void chargingStationBootedEventChargingStationCreated() {
        assertNull(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()));

        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());

        assertNotNull(cs);
        assertEquals(cs.getId(), CHARGING_STATION_ID.getId());
    }

    @Test
    public void chargingStationAcceptedEventChargingStationRegistered() {
        chargingStationRepository.saveAndFlush(new ChargingStation(CHARGING_STATION_ID.getId()));
        assertFalse(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()).isRegistered());

        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));

        assertTrue(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()).isRegistered());
    }

    @Test
    public void chargingStationAcceptedEventUnknownChargingStationNoExceptionThrown() {
        assertNull(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()));

        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));
    }

    //TODO: Finish tests - Ingo Pak, 21 Jan 2014

    @Test
    public void chargingStationPlacedEventCoordinatesEmptyAddress() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, null));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertEquals(Double.valueOf(COORDINATES.getLatitude()), cs.getLatitude());
        assertEquals(Double.valueOf(COORDINATES.getLongitude()), cs.getLongitude());
    }

    @Test
    public void chargingStationPlacedEventAddressEmptyCoordinates() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, null, ADDRESS));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertEquals(ADDRESS.getAddressline1(), cs.getAddress());
        assertEquals(ADDRESS.getPostalCode(), cs.getPostalCode());
        assertEquals(ADDRESS.getRegion(), cs.getRegion());
        assertEquals(ADDRESS.getCity(), cs.getCity());
        assertEquals(ADDRESS.getCountry(), cs.getCountry());
    }

    @Test
    public void chargingStationPlacedEventAddressAndCoordinates() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertEquals(Double.valueOf(COORDINATES.getLatitude()), cs.getLatitude());
        assertEquals(Double.valueOf(COORDINATES.getLongitude()), cs.getLongitude());
        assertEquals(ADDRESS.getAddressline1(), cs.getAddress());
        assertEquals(ADDRESS.getPostalCode(), cs.getPostalCode());
        assertEquals(ADDRESS.getRegion(), cs.getRegion());
        assertEquals(ADDRESS.getCity(), cs.getCity());
        assertEquals(ADDRESS.getCountry(), cs.getCountry());
    }

    @Test
    public void chargingStationPlacedEventUnknownChargingStationNoExceptionThrown() {
        assertNull(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()));

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS));
    }

    @Test
    public void chargingStationMadeReservableEventChargingStationReservable() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID));

        assertTrue(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()).isReservable());
    }

    @Test
    public void chargingStationMadeNotReservableEventChargingStationNotReservable() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationMadeNotReservableEvent(CHARGING_STATION_ID));

        assertFalse(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()).isReservable());
    }

    @Test
    public void chargingStationMadeReservableEventUnknownChargingStationNoExceptionThrown() {
        assertNull(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()));

        eventHandler.handle(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID));
    }

}
