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

import io.motown.domain.commandauthorization.model.CommandAuthorization;
import io.motown.domain.commandauthorization.model.CommandAuthorizationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Provides access to storage of {@code CommandAuthorization}.
 */
public class CommandAuthorizationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CommandAuthorizationRepository.class);

    private EntityManager entityManager;

    /**
     * Finds {@code CommandAuthorization} based on charging station id, user identity and command class.
     *
     * @param chargingStationId    charging station identifier.
     * @param userIdentity         user identity (user name).
     * @param commandClass         command class.
     * @return command authorization if it exists in the repository.
     */
    public CommandAuthorization find(String chargingStationId, String userIdentity, Class commandClass) {
        CommandAuthorizationId id = new CommandAuthorizationId(chargingStationId, userIdentity, commandClass);
        return entityManager.find(CommandAuthorization.class, id);
    }

    /**
     * Creates or updates a {@code CommandAuthorization} based on charging station id, user identity and command class.
     *
     * @param chargingStationId    charging station identifier.
     * @param userIdentity         user identity (user name).
     * @param commandClass         command class.
     * @return (updated) command authorization.
     */
    public CommandAuthorization createOrUpdate(String chargingStationId, String userIdentity, Class commandClass) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        CommandAuthorization storedCommandAuthorization = null;

        try {
            storedCommandAuthorization = entityManager.merge(new CommandAuthorization(new CommandAuthorizationId(chargingStationId, userIdentity, commandClass)));
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                LOG.warn("Transaction is still active while it should not be, rolling back.");
                transaction.rollback();
            }
        }
        return storedCommandAuthorization;
    }

    /**
     * Removes a {@code CommandAuthorization} based on charging station id, user identity and command class if it exists.
     *
     * @param chargingStationId    charging station identifier.
     * @param userIdentity         user identity (user name).
     * @param commandClass         command class.
     */
    public void remove(String chargingStationId, String userIdentity, Class commandClass) {
        CommandAuthorizationId id = new CommandAuthorizationId(chargingStationId, userIdentity, commandClass);
        CommandAuthorization commandAuthorization = entityManager.find(CommandAuthorization.class, id);

        if (commandAuthorization == null) {
            return;
        }

        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.remove(commandAuthorization);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * Sets the entity manager to use.
     *
     * @param entityManager    entity manager.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
