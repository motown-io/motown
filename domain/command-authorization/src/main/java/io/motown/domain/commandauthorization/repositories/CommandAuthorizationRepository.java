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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * Provides access to storage of {@code CommandAuthorization}.
 */
public class CommandAuthorizationRepository {

    private EntityManagerFactory entityManagerFactory;

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

        EntityManager em = getEntityManager();
        try {
            return em.find(CommandAuthorization.class, id);
        } finally {
            em.close();
        }
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
        EntityManager em = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            CommandAuthorization storedCommandAuthorization = em.merge(new CommandAuthorization(new CommandAuthorizationId(chargingStationId, userIdentity, commandClass)));

            tx.commit();

            return storedCommandAuthorization;
        } catch (Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Removes a {@code CommandAuthorization} based on charging station id, user identity and command class if it exists.
     *
     * @param chargingStationId    charging station identifier.
     * @param userIdentity         user identity (user name).
     * @param commandClass         command class.
     */
    public void remove(String chargingStationId, String userIdentity, Class commandClass) {
        EntityManager em = getEntityManager();

        EntityTransaction tx = null;
        try {
            CommandAuthorizationId id = new CommandAuthorizationId(chargingStationId, userIdentity, commandClass);
            CommandAuthorization commandAuthorization = em.find(CommandAuthorization.class, id);

            if (commandAuthorization != null) {
                tx = em.getTransaction();
                tx.begin();

                em.remove(commandAuthorization);

                tx.commit();
            }
        } catch (Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
