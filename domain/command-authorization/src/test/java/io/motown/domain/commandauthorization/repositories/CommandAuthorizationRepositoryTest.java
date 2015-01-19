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
package io.motown.domain.commandauthorization.repositories;

import io.motown.domain.api.chargingstation.AcceptChargingStationCommand;
import io.motown.domain.commandauthorization.model.CommandAuthorization;
import io.motown.domain.commandauthorization.model.CommandAuthorizationId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.USER_IDENTITY;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@ContextConfiguration("classpath:domain-authorization-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CommandAuthorizationRepositoryTest {

    @Autowired
    private CommandAuthorizationRepository commandAuthorizationRepository;

    @Autowired
    private EntityManager entityManager;

    private static final Class<AcceptChargingStationCommand> COMMAND_CLASS = AcceptChargingStationCommand.class;

    @Before
    public void setUp() {
        entityManager.clear();
        deleteFromDatabase(entityManager, CommandAuthorization.class);
    }

    @Test
    public void findAuthorizationEmptyRepository() {
        CommandAuthorization commandAuthorization = commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        assertNull(commandAuthorization);
    }

    @Test
    public void findAuthorization() {
        commandAuthorizationRepository.createOrUpdate(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        CommandAuthorization commandAuthorization = commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        assertNotNull(commandAuthorization);
    }

    @Test
    public void authorizationFieldsStored() {
        commandAuthorizationRepository.createOrUpdate(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        CommandAuthorization commandAuthorization = commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        assertEquals(commandAuthorization, new CommandAuthorization(new CommandAuthorizationId(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS)));
    }

    @Test
    public void createDuplicate() {
        // no exceptions should be thrown
        commandAuthorizationRepository.createOrUpdate(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);
        commandAuthorizationRepository.createOrUpdate(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        CommandAuthorization commandAuthorization = commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);
        assertNotNull(commandAuthorization);
    }

    @Test
    public void removeUnknownAuthorization() {
        // make sure pre-condition is met
        assertNull(commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS));

        // no exception should be thrown
        commandAuthorizationRepository.remove(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);
    }

    @Test
    public void removeKnownAuthorization() {
        // make sure pre-condition is met
        commandAuthorizationRepository.createOrUpdate(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);
        assertNotNull(commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS));

        commandAuthorizationRepository.remove(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS);

        assertNull(commandAuthorizationRepository.find(CHARGING_STATION_ID.getId(), USER_IDENTITY.getId(), COMMAND_CLASS));
    }


    private static void deleteFromDatabase(EntityManager entityManager, Class jpaEntityClass) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            List resultList = entityManager.createQuery("SELECT entity FROM " + jpaEntityClass.getName() + " as entity").getResultList();
            for (Object obj : resultList) {
                entityManager.remove(obj);
            }
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

}
