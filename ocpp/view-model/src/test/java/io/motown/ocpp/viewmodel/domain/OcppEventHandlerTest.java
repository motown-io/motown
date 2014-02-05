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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.ChargingStationAcceptedEvent;
import io.motown.domain.api.chargingstation.ChargingStationConfiguredEvent;
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.ocpp.viewmodel.OcppEventHandler;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.ocpp.viewmodel.domain.TestUtils.*;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OcppEventHandlerTest {

    private OcppEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        eventHandler = new OcppEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
    }

    @Test
    public void testChargingStationBootedEvent() {
        assertNull(chargingStationRepository.findOne(getChargingStationId().getId()));

        eventHandler.handle(new ChargingStationCreatedEvent(getChargingStationId()));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertNotNull(cs);

        assertEquals(cs.getId(), getChargingStationId().getId());
    }

    @Test
    public void testChargingStationAcceptedEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(getChargingStationId()));

        eventHandler.handle(new ChargingStationAcceptedEvent(getChargingStationId()));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertTrue(cs.isRegistered());
    }

    @Test
    public void testUnknownChargingStationAcceptedEvent() {
        // no exception expected on unknown charging station
        eventHandler.handle(new ChargingStationAcceptedEvent(getChargingStationId()));
    }

    @Test
    public void testChargingStationConfiguredEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(getChargingStationId()));
        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertFalse(cs.isConfigured());
        assertNotSame(cs.getNumberOfConnectors(), getConnectors().size());

        eventHandler.handle(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), getConfigurationItems()));

        cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertTrue(cs.isConfigured());
        assertEquals(cs.getNumberOfConnectors(), getConnectors().size());
    }

    @Test
    public void testUnknownChargingStationConfiguredEvent() {
        eventHandler.handle(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), getConfigurationItems()));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertTrue(cs.isConfigured());
        assertEquals(cs.getNumberOfConnectors(), getConnectors().size());
    }

}
