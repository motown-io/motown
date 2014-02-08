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

import java.util.List;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.getManufacturerWithConfiguration;
import static org.junit.Assert.*;

@ContextConfiguration("classpath:chargingstation-configuration-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ChargingStationTypeRepositoryTest {

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Before
    public void setUp() {
        chargingStationTypeRepository.deleteAll();
        manufacturerRepository.deleteAll();
    }

    @Test
    public void testFindByCodeAndManufacturerCode() {
        String typeCode = "MODEL1";
        String manufacturerCode = "MOTOWN";

        Manufacturer manufacturer = getManufacturerWithConfiguration(manufacturerCode, typeCode);
        manufacturerRepository.saveAndFlush(manufacturer);

        List<ChargingStationType> chargingStationTypes = chargingStationTypeRepository.findByCodeAndManufacturerCode("MODEL1", "MOTOWN");

        assertEquals(chargingStationTypes.size(), manufacturer.getChargingStationTypes().size());
        ChargingStationType chargingStationType = chargingStationTypes.get(0);
        assertNotNull(chargingStationType.getId());
        assertEquals(typeCode, chargingStationType.getCode());
        assertEquals(manufacturerCode, chargingStationType.getManufacturer().getCode());
        assertTrue(chargingStationType.getEvses().get(0).getId() > 0);
        assertTrue(chargingStationType.getEvses().get(0).getConnectors().get(0).getId() > 0);
    }

}
