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
package io.motown.ochp.viewmodel.domain;

import io.motown.domain.api.chargingstation.ChargingStationAcceptedEvent;
import io.motown.domain.api.chargingstation.ChargingStationConfiguredEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.ochp.viewmodel.OchpEventHandler;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:ochp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OchpEventHandlerTest {

    private OchpEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        eventHandler = new OchpEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
    }

    @Test
    public void testChargingStationBootedEvent() {
        assertNull(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()));

        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertNotNull(cs);

        assertEquals(cs.getId(), CHARGING_STATION_ID.getId());
    }

    @Test
    public void testChargingStationAcceptedEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID));

        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.isRegistered());
    }

    @Test
    public void testUnknownChargingStationAcceptedEvent() {
        // no exception expected on unknown charging station
        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID));
    }

    @Test
    public void testChargingStationConfiguredEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID));
        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertFalse(cs.isConfigured());
        assertNotSame(cs.getNumberOfEvses(), EVSES.size());

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.isConfigured());
        assertEquals(cs.getNumberOfEvses(), EVSES.size());
    }

    @Test
    public void testUnknownChargingStationConfiguredEvent() {
        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.isConfigured());
        assertEquals(cs.getNumberOfEvses(), EVSES.size());
    }

}
