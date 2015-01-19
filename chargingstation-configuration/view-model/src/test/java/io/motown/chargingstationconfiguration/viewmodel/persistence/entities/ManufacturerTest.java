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
package io.motown.chargingstationconfiguration.viewmodel.persistence.entities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.deleteFromDatabase;
import static io.motown.chargingstationconfiguration.viewmodel.domain.TestUtils.insertIntoDatabase;

@ContextConfiguration("classpath:chargingstation-configuration-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ManufacturerTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        deleteFromDatabase(entityManagerFactory, ChargingStationType.class);
        deleteFromDatabase(entityManagerFactory, Manufacturer.class);
    }

    @Test(expected = PersistenceException.class)
    public void testManufacturerCodeUnique() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCode("MOTOWN");

        insertIntoDatabase(entityManagerFactory, manufacturer);

        manufacturer = new Manufacturer();
        manufacturer.setCode("MOTOWN");
        insertIntoDatabase(entityManagerFactory, manufacturer);
    }

}
