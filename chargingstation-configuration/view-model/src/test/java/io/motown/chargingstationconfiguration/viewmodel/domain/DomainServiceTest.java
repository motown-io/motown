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

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ChargingStationTypeRepository;
import io.motown.chargingstationconfiguration.viewmodel.persistence.repositories.ManufacturerRepository;
import io.motown.domain.api.chargingstation.Evse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.Set;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.*;
import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:chargingstation-configuration-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DomainServiceTest {

    private DomainService domainService;

    @Autowired
    private ChargingStationTypeRepository chargingStationTypeRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Before
    public void setUp() {
        chargingStationTypeRepository.deleteAll();
        manufacturerRepository.deleteAll();

        domainService = new DomainService();
        domainService.setChargingStationTypeRepository(chargingStationTypeRepository);
    }

    @Test
    public void testGetEvses() {
        Manufacturer manufacturer = getManufacturerWithConfiguration("motown", "model1");

        manufacturerRepository.saveAndFlush(manufacturer);

        Set<Evse> evses = domainService.getEvses("motown", "model1");

        assertEquals(manufacturer.getChargingStationTypes().get(0).getEvses().size(), evses.size());
    }

}
