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

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

public class ConnectorRepository {

    private EntityManagerFactory entityManagerFactory;

    public Connector createOrUpdate(Connector connector) {
        EntityManager em = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            Connector persistedConnector = em.merge(connector);

            tx.commit();

            return persistedConnector;
        } catch (Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Connector findOne(Long id) {
        Connector connector = getEntityManager().find(Connector.class, id);
        if (connector != null) {
            return connector;
        }
        throw new EntityNotFoundException(String.format("Unable to find connector with id '%s'", id));
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
