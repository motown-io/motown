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
package io.motown.chargingstationconfiguration.viewmodel.domain;

import com.google.common.collect.Iterables;
import io.motown.chargingstationconfiguration.viewmodel.exceptions.ResourceAlreadyExistsException;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ChargingStationTypeRepository;
import io.motown.domain.api.chargingstation.Evse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.Set;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.*;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ContextConfiguration("classpath:chargingstation-configuration-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DomainServiceTest {

    public static final String VENDOR = "MOTOWN";

    public static final String MODEL = "MODEL1";

    public static final String UNKNOWN_VENDOR = "ACMECORP";

    public static final String UNKNOWN_MODEL = "TYPE1";

    public static final Long UNKNOWN_CHARGING_STATION_TYPE = 999l;

    public static final Long UNKNOWN_EVSE_ID = 999l;

    private DomainService domainService;

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        deleteFromDatabase(entityManagerFactory, ChargingStationType.class);
        deleteFromDatabase(entityManagerFactory, Manufacturer.class);

        domainService = new DomainService();
        chargingStationTypeRepository.setEntityManagerFactory(entityManagerFactory);
        domainService.setChargingStationTypeRepository(chargingStationTypeRepository);
    }

    @Test
    public void testGetEvsesForNonExistingManufacturerAndModel() {
        Manufacturer manufacturer = getManufacturerWithConfiguration(VENDOR, MODEL);
        insertIntoDatabase(entityManagerFactory, manufacturer);

        Set<Evse> evses = domainService.getEvses(UNKNOWN_VENDOR, UNKNOWN_MODEL);

        assertEquals(0, evses.size());
    }

    @Test
    public void testGetEvsesForNonExistingManufacturerAndExistingModel() {
        Manufacturer manufacturer = getManufacturerWithConfiguration(VENDOR, MODEL);
        insertIntoDatabase(entityManagerFactory, manufacturer);

        Set<Evse> evses = domainService.getEvses(UNKNOWN_VENDOR, MODEL);

        assertEquals(0, evses.size());
    }

    @Test
    public void createEvse() throws ResourceAlreadyExistsException {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse evse = getEvse(9);
        evse.setConnectors(TestUtils.getConnectors(3));

        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse createdEvse =
                domainService.createEvse(chargingStationType.getId(), evse);

        ChargingStationType chargingStationTypeUpdated = chargingStationTypeRepository.findOne(chargingStationType.getId());
        assertEquals(evse.getIdentifier(), createdEvse.getIdentifier());
        assertEquals(chargingStationTypeUpdated.getEvses().size(), chargingStationType.getEvses().size() + 1);
    }

    @Test(expected = ResourceAlreadyExistsException.class)
    public void createExistingEvse() throws ResourceAlreadyExistsException {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse evse =
                Iterables.get(chargingStationType.getEvses(), 0);

        domainService.createEvse(chargingStationType.getId(), evse);
    }

    @Test
    public void getEvses() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        Set<io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse> evses = chargingStationType.getEvses();

        Set<io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse> evsesFromService = domainService.getEvses(chargingStationType.getId());

        assertEquals(evses.size(), evsesFromService.size());
    }

    @Test
    public void deleteEvse() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        Long evseId = Iterables.get(chargingStationType.getEvses(), 0).getId();

        domainService.deleteEvse(chargingStationType.getId(), evseId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteEvseValidateGetEvse() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        Long evseId = Iterables.get(chargingStationType.getEvses(), 0).getId();

        domainService.deleteEvse(chargingStationType.getId(), evseId);

        domainService.getEvse(chargingStationType.getId(), evseId);
    }

    @Test
    public void createConnector() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        Long evseId = Iterables.get(chargingStationType.getEvses(), 0).getId();
        Connector connector = TestUtils.getConnector();

        Connector createdConnector = domainService.createConnector(chargingStationType.getId(), evseId, connector);

        connector.setId(createdConnector.getId());
        assertEquals(connector, createdConnector);
        assertNotNull(domainService.getConnector(chargingStationType.getId(), evseId, connector.getId()));
    }

    @Test
    public void getConnectors() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse evse =
                Iterables.get(chargingStationType.getEvses(), 0);

        Set<Connector> connectorsFromService = domainService.getConnectors(chargingStationType.getId(), evse.getId());

        assertEquals(connectorsFromService, evse.getConnectors());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getConnectorsUnknownChargingStationType() {
        domainService.getConnectors(UNKNOWN_CHARGING_STATION_TYPE, UNKNOWN_EVSE_ID);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getConnectorsUnknownEvse() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);

        domainService.getConnectors(chargingStationType.getId(), UNKNOWN_EVSE_ID);
    }

    @Test
    public void deleteConnector() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse evse =
                Iterables.get(chargingStationType.getEvses(), 0);
        Connector connector = Iterables.get(evse.getConnectors(), 0);

        domainService.deleteConnector(chargingStationType.getId(), evse.getId(), connector.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteConnectorVerifyGetConnector() {
        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse evse =
                Iterables.get(chargingStationType.getEvses(), 0);
        Connector connector = Iterables.get(evse.getConnectors(), 0);

        domainService.deleteConnector(chargingStationType.getId(), evse.getId(), connector.getId());

        // should throw exception
        domainService.getConnector(chargingStationType.getId(), evse.getId(), connector.getId());
    }

}
