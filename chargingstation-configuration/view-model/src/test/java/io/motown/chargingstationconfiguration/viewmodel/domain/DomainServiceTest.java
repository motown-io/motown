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

import javax.persistence.EntityManager;
import java.util.Set;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.deleteFromDatabase;
import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.getManufacturerWithConfiguration;
import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.insertIntoDatabase;
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
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager.clear();
        deleteFromDatabase(entityManager, ChargingStationType.class);
        deleteFromDatabase(entityManager, Manufacturer.class);

        domainService = new DomainService();
        domainService.setChargingStationTypeRepository(chargingStationTypeRepository);
    }

    @Test
    public void testGetEvsesForExistingManufacturerAndModel() {
        Manufacturer manufacturer = getManufacturerWithConfiguration(VENDOR, MODEL);
        insertIntoDatabase(entityManager, manufacturer);

        Set<Evse> evses = domainService.getEvses(VENDOR, MODEL);
        assertEquals(manufacturer.getChargingStationTypes().iterator().next().getEvses().size(), evses.size());

        // validate consistent results
        evses = domainService.getEvses(VENDOR, MODEL);
        assertEquals(manufacturer.getChargingStationTypes().iterator().next().getEvses().size(), evses.size());
    }

    @Test
    public void testGetEvsesForNonExistingManufacturerAndModel() {
        Manufacturer manufacturer = getManufacturerWithConfiguration(VENDOR, MODEL);
        insertIntoDatabase(entityManager, manufacturer);

        Set<Evse> evses = domainService.getEvses(UNKNOWN_VENDOR, UNKNOWN_MODEL);

        assertEquals(0, evses.size());
    }

    @Test
    public void testGetEvsesForExistingManufacturerAndNonExistingModel() {
        Manufacturer manufacturer = getManufacturerWithConfiguration(VENDOR, MODEL);
        insertIntoDatabase(entityManager, manufacturer);

        Set<Evse> evses = domainService.getEvses(VENDOR, UNKNOWN_MODEL);

        assertEquals(0, evses.size());
    }

    @Test
    public void testGetEvsesForNonExistingManufacturerAndExistingModel() {
        Manufacturer manufacturer = getManufacturerWithConfiguration(VENDOR, MODEL);
        insertIntoDatabase(entityManager, manufacturer);

        Set<Evse> evses = domainService.getEvses(UNKNOWN_VENDOR, MODEL);

        assertEquals(0, evses.size());
    }

}
