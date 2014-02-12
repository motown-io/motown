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

import static io.motown.vas.viewmodel.TestUtils.*;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertNull(chargingStationRepository.findOne(getChargingStationId().getId()));

        eventHandler.handle(new ChargingStationCreatedEvent(getChargingStationId()));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());

        assertNotNull(cs);
        assertEquals(cs.getId(), getChargingStationId().getId());
    }

    @Test
    public void chargingStationAcceptedEventChargingStationRegistered() {
        chargingStationRepository.saveAndFlush(new ChargingStation(getChargingStationId().getId()));
        assertFalse(chargingStationRepository.findOne(getChargingStationId().getId()).isRegistered());

        eventHandler.handle(new ChargingStationAcceptedEvent(getChargingStationId()));

        assertTrue(chargingStationRepository.findOne(getChargingStationId().getId()).isRegistered());
    }

    @Test
    public void chargingStationAcceptedEventUnknownChargingStationNoExceptionThrown() {
        assertNull(chargingStationRepository.findOne(getChargingStationId().getId()));

        eventHandler.handle(new ChargingStationAcceptedEvent(getChargingStationId()));
    }

    //TODO: Finish tests - Ingo Pak, 21 Jan 2014

    @Test
    public void chargingStationPlacedEventCoordinatesEmptyAddress() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(getChargingStationId(), COORDINATES, null));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertEquals(Double.valueOf(COORDINATES.getLatitude()), cs.getLatitude());
        assertEquals(Double.valueOf(COORDINATES.getLongitude()), cs.getLongitude());
    }

    @Test
    public void chargingStationPlacedEventAddressEmptyCoordinates() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(getChargingStationId(), null, ADDRESS));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertEquals(ADDRESS.getAddressline1(), cs.getAddress());
        assertEquals(ADDRESS.getPostalCode(), cs.getPostalCode());
        assertEquals(ADDRESS.getRegion(), cs.getRegion());
        assertEquals(ADDRESS.getCity(), cs.getCity());
        assertEquals(ADDRESS.getCountry(), cs.getCountry());
    }

    @Test
    public void chargingStationPlacedEventAddressAndCoordinates() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(getChargingStationId(), COORDINATES, ADDRESS));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
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
        assertNull(chargingStationRepository.findOne(getChargingStationId().getId()));

        eventHandler.handle(new ChargingStationPlacedEvent(getChargingStationId(), COORDINATES, ADDRESS));
    }

    @Test
    public void chargingStationMadeReservableEventChargingStationReservable() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationMadeReservableEvent(getChargingStationId()));

        assertTrue(chargingStationRepository.findOne(getChargingStationId().getId()).isReservable());
    }

    @Test
    public void chargingStationMadeNotReservableEventChargingStationNotReservable() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationMadeNotReservableEvent(getChargingStationId()));

        assertFalse(chargingStationRepository.findOne(getChargingStationId().getId()).isReservable());
    }

    @Test
    public void chargingStationMadeReservableEventUnknownChargingStationNoExceptionThrown() {
        assertNull(chargingStationRepository.findOne(getChargingStationId().getId()));

        eventHandler.handle(new ChargingStationMadeReservableEvent(getChargingStationId()));
    }

}
