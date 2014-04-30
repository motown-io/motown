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
import java.util.Set;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.*;
import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:chargingstation-configuration-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DomainServiceTest {

    public static final String VENDOR = "MOTOWN";
    public static final String MODEL = "MODEL1";
    public static final String UNKNOWN_VENDOR = "ACMECORP";
    public static final String UNKNOWN_MODEL = "TYPE1";
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
        evse.setConnectors(getConnectors(3));

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

        io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse createdEvse =
                domainService.createEvse(chargingStationType.getId(), evse);
    }

    //TODO rewrite test
//
//    @Test
//    public void deleteConnector() {
//        ChargingStationType chargingStationType = getChargingStationTypeNonTransient(entityManagerFactory);
//        Connector connector = chargingStationType.getEvses().iterator().next().getConnectors().iterator().next();
//        // mock the repository so we can verify the argument later
//        ChargingStationTypeRepository chargingStationTypeRepository = mock(ChargingStationTypeRepository.class);
//        when(chargingStationTypeRepository.findByConnectorId(connector.getId())).thenReturn(chargingStationType);
//        domainService.setChargingStationTypeRepository(chargingStationTypeRepository);
//
//        domainService.deleteConnector(connector.getId());
//
//        // remove connector from our local object so we can compare it to the argument on the repository call
//        chargingStationType.getEvses().iterator().next().getConnectors().remove(connector);
//        verify(chargingStationTypeRepository).createOrUpdate(chargingStationType);
//    }

}
