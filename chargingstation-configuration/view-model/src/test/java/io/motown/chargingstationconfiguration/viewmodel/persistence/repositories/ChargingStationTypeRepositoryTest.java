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
package io.motown.chargingstationconfiguration.viewmodel.persistence.repositories;

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Set;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:chargingstation-configuration-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ChargingStationTypeRepositoryTest {

    public static final String MANUFACTURER_CODE = "MOTOWN";

    public static final String TYPE_CODE = "MODEL1";

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager.clear();
        deleteFromDatabase(entityManager, ChargingStationType.class);
        deleteFromDatabase(entityManager, Manufacturer.class);
    }

    @Test
    public void testFindByCodeAndManufacturerCode() {
        String typeCode = TYPE_CODE;
        String manufacturerCode = MANUFACTURER_CODE;

        Manufacturer manufacturer = getManufacturerWithConfiguration(manufacturerCode, typeCode);
        insertIntoDatabase(entityManager, manufacturer);

        List<ChargingStationType> chargingStationTypes = chargingStationTypeRepository.findByCodeAndManufacturerCode(TYPE_CODE, MANUFACTURER_CODE);

        assertEquals(chargingStationTypes.size(), manufacturer.getChargingStationTypes().size());
        ChargingStationType chargingStationType = chargingStationTypes.get(0);
        assertNotNull(chargingStationType.getId());
        assertEquals(typeCode, chargingStationType.getCode());
        assertEquals(manufacturerCode, chargingStationType.getManufacturer().getCode());
        assertNotNull(chargingStationType.getEvses().iterator().next());
        assertNotNull(chargingStationType.getEvses().iterator().next().getConnectors().iterator().next());
    }

    @Test(expected = PersistenceException.class)
    public void testUniquenessOfManufacturerAndChargingStationType() {
        String typeCode = TYPE_CODE;
        String manufacturerCode = MANUFACTURER_CODE;

        Manufacturer manufacturer = getManufacturerWithConfiguration(manufacturerCode, typeCode);
        insertIntoDatabase(entityManager, manufacturer);

        Set<ChargingStationType> chargingStationTypes = getChargingStationTypes(manufacturer, TYPE_CODE);
        for (ChargingStationType type : chargingStationTypes) {
            insertIntoDatabase(entityManager, type);
        }
    }

}
