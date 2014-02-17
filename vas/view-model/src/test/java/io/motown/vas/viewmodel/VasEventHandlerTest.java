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
import io.motown.domain.api.chargingstation.ComponentStatus;
import io.motown.domain.api.chargingstation.OpeningTime;
import io.motown.vas.viewmodel.model.*;
import io.motown.vas.viewmodel.model.ConnectorType;
import io.motown.vas.viewmodel.model.Evse;
import io.motown.vas.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
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

    @Autowired
    private ConfigurationConversionService configurationConversionService;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        eventHandler = new VasEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
        eventHandler.setConfigurationConversionService(configurationConversionService);
    }

    @Test
    public void chargingStationBootedEventChargingStationCreated() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID));

        ChargingStation cs = getTestChargingStationFromRepository();

        assertNotNull(cs);
        assertEquals(cs.getChargingStationId(), CHARGING_STATION_ID.getId());
    }

    @Test
    public void chargingStationAcceptedEventChargingStationRegistered() {
        chargingStationRepository.saveAndFlush(new ChargingStation(CHARGING_STATION_ID.getId()));
        assertFalse(getTestChargingStationFromRepository().isRegistered());

        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));

        assertTrue(getTestChargingStationFromRepository().isRegistered());
    }

    @Test
    public void chargingStationAcceptedEventUnknownChargingStationNoExceptionThrown() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));
    }

    //TODO: Finish tests - Ingo Pak, 21 Jan 2014

    @Test
    public void chargingStationPlacedEventCoordinatesEmptyAddress() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, null));

        ChargingStation cs = getTestChargingStationFromRepository();
        assertEquals(Double.valueOf(COORDINATES.getLatitude()), cs.getLatitude());
        assertEquals(Double.valueOf(COORDINATES.getLongitude()), cs.getLongitude());
    }

    @Test
    public void chargingStationPlacedEventAddressEmptyCoordinates() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, null, ADDRESS));

        ChargingStation cs = getTestChargingStationFromRepository();
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

        ChargingStation cs = getTestChargingStationFromRepository();
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
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS));
    }

    @Test
    public void chargingStationMadeReservableEventChargingStationReservable() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID));

        assertTrue(getTestChargingStationFromRepository().isReservable());
    }

    @Test
    public void chargingStationMadeNotReservableEventChargingStationNotReservable() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationMadeNotReservableEvent(CHARGING_STATION_ID));

        assertFalse(getTestChargingStationFromRepository().isReservable());
    }

    @Test
    public void chargingStationMadeReservableEventUnknownChargingStationNoExceptionThrown() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID));
    }

    @Test
    public void chargingStationConfiguredEventUnknownChargingStationShouldCreateChargingStation() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        assertNotNull(getTestChargingStationFromRepository());
    }

    @Test
    public void chargingStationConfiguredEventChargingStationShouldBeConfigured() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        assertTrue(getTestChargingStationFromRepository().isConfigured());
    }

    @Test
    public void chargingStationConfiguredEventVerifyChargeMode() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        ChargeMode expectedChargeMode = ChargeMode.fromChargingProtocol(EVSES.iterator().next().getConnectors().get(0).getChargingProtocol());

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        assertEquals(expectedChargeMode, getTestChargingStationFromRepository().getChargeMode());
    }

    @Test
    public void chargingStationConfiguredEventVerifyConnectorTypes() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        Set<ConnectorType> expectedConnectorTypes = configurationConversionService.getConnectorTypesFromEvses(EVSES);

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        // not testing if Set with expected values contain the correct values, configurationConversionService has its own test set
        assertEquals(expectedConnectorTypes, getTestChargingStationFromRepository().getConnectorTypes());
    }

    @Test
    public void chargingStationConfiguredEventVerifyEvses() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        Set<Evse> expectedEvses = configurationConversionService.getEvsesFromEventEvses(EVSES);

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        // not testing if Set with expected values contain the correct values, configurationConversionService has its own test set
        assertEquals(expectedEvses, getTestChargingStationFromRepository().getEvses());
    }

    @Test
    public void chargingStationConfiguredEventVerifyChargingCapabilities() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        Set<ChargingCapability> expectedChargingCapabilities = configurationConversionService.getChargingCapabilitiesFromEvses(EVSES);

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        // not testing if Set with expected values contain the correct values, configurationConversionService has its own test set
        assertEquals(expectedChargingCapabilities, getTestChargingStationFromRepository().getChargingCapabilities());
    }

    @Test
    public void chargingStationOpeningTimesSetEvent() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        eventHandler.handle(new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, OPENING_TIMES));

        ChargingStation chargingStation = getTestChargingStationFromRepository();
        assertEquals(OPENING_TIMES.size(), chargingStation.getOpeningTimes().size());
        OpeningTime[] cOT = OPENING_TIMES.toArray(new OpeningTime[OPENING_TIMES.size()]);
        io.motown.vas.viewmodel.model.OpeningTime[] vOT = chargingStation.getOpeningTimes().toArray(new io.motown.vas.viewmodel.model.OpeningTime[chargingStation.getOpeningTimes().size()]);
        assertEquals(cOT[0].getDay().value(), vOT[0].getDay().value());
        assertEquals(cOT[0].getTimeStart().getHourOfDay(), vOT[0].getTimeStart() / 60);
        assertEquals(cOT[0].getTimeStart().getMinutesInHour(), vOT[0].getTimeStart() % 60);
        assertEquals(cOT[0].getTimeStop().getHourOfDay(), vOT[0].getTimeStop() / 60);
        assertEquals(cOT[0].getTimeStop().getMinutesInHour(), vOT[0].getTimeStop() % 60);
    }

    @Test
    public void chargingStationStatusNotificationReceivedEventUnknownChargingStationNoException() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, new Date(), new HashMap<String, String>()));
    }

    @Test
    public void chargingStationStatusNotificationAvailableReceivedEventVerifyChargingStationState() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        ChargingStation chargingStation = getTestChargingStationFromRepository();
        assertTrue(chargingStation.getState().equals(io.motown.vas.viewmodel.model.ComponentStatus.UNKNOWN));

        eventHandler.handle(new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, new Date(), new HashMap<String, String>()));

        chargingStation = getTestChargingStationFromRepository();
        assertTrue(chargingStation.getState().equals(io.motown.vas.viewmodel.model.ComponentStatus.AVAILABLE));
    }

    @Test
    public void componentStatusNotificationReceivedEventUnknownChargingStationNoException() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ComponentStatusNotificationReceivedEvent(CHARGING_STATION_ID, ChargingStationComponent.EVSE, EVSE_ID, ComponentStatus.AVAILABLE, new Date(), new HashMap<String, String>()));
    }

    @Test
    public void componentStatusNotificationReceivedEventUnknownEvseNoException() {
        assertNull(getTestChargingStationFromRepository());

        eventHandler.handle(new ComponentStatusNotificationReceivedEvent(CHARGING_STATION_ID, ChargingStationComponent.EVSE, UNKNOWN_EVSE_ID, ComponentStatus.AVAILABLE, new Date(), new HashMap<String, String>()));
    }

    @Test
    public void componentStatusNotificationAvailableReceivedEventVerifyEvseState() {
        chargingStationRepository.saveAndFlush(getRegisteredAndConfiguredChargingStation());
        ChargingStation chargingStation = getTestChargingStationFromRepository();
        assertTrue(chargingStation.getEvse(EVSE_ID.getNumberedId()).getState().equals(io.motown.vas.viewmodel.model.ComponentStatus.UNKNOWN));

        eventHandler.handle(new ComponentStatusNotificationReceivedEvent(CHARGING_STATION_ID, ChargingStationComponent.EVSE, EVSE_ID, ComponentStatus.AVAILABLE, new Date(), new HashMap<String, String>()));

        chargingStation = getTestChargingStationFromRepository();
        assertTrue(chargingStation.getEvse(EVSE_ID.getNumberedId()).getState().equals(io.motown.vas.viewmodel.model.ComponentStatus.AVAILABLE));
    }

    private ChargingStation getTestChargingStationFromRepository() {
        return chargingStationRepository.findByChargingStationId(CHARGING_STATION_ID.getId());
    }

}
