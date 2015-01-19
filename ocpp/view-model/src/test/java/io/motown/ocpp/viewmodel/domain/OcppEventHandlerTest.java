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
import io.motown.domain.api.chargingstation.UnconfiguredChargingStationBootedEvent;
import io.motown.ocpp.viewmodel.OcppEventHandler;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.viewmodel.domain.OccpViewModelTestUtils.deleteFromDatabase;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OcppEventHandlerTest {

    private OcppEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.clear();
        deleteFromDatabase(entityManager, ChargingStation.class);

        eventHandler = new OcppEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
    }

    @Test
    public void chargingStationCreatedEvent() {
        assertNull(chargingStationRepository.findOne(CHARGING_STATION_ID.getId()));

        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertNotNull(cs);

        assertEquals(cs.getId(), CHARGING_STATION_ID.getId());
    }

    @Test
    public void chargingStationBootedEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT));
        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        Assert.assertNull(cs.getProtocol());

        eventHandler.handle(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));

        cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        Assert.assertEquals(PROTOCOL, cs.getProtocol());
    }

    @Test
    public void chargingStationAcceptedEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT));

        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.isRegistered());
    }

    @Test
    public void unknownChargingStationAcceptedEvent() {
        // no exception expected on unknown charging station
        eventHandler.handle(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void chargingStationConfiguredEvent() {
        eventHandler.handle(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT));
        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertFalse(cs.isConfigured());
        assertNotSame(cs.getNumberOfEvses(), EVSES.size());

        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT));

        cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.isConfigured());
        assertEquals(cs.getNumberOfEvses(), EVSES.size());
    }

    @Test
    public void unknownChargingStationConfiguredEvent() {
        eventHandler.handle(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT));

        ChargingStation cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertTrue(cs.isConfigured());
        assertEquals(cs.getNumberOfEvses(), EVSES.size());
    }

}
