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
package io.motown.mobieurope.destination.persistence.repository;

import io.motown.mobieurope.destination.persistence.entities.SourceEndpoint;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class SourceEndpointRepository {

    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public SourceEndpoint findSourceEndpointByPmsIdentifier(String pmsIdentifier) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.createQuery("SELECT se FROM SourceEndpoint AS se WHERE se.pmsIdentifier = :pmsIdentifier", SourceEndpoint.class)
                    .setParameter("pmsIdentifier", pmsIdentifier)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public SourceEndpoint insertOrUpdateSourceEndpoint(SourceEndpoint sourceEndpoint) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            SourceEndpoint persistedSourceEndpoint = entityManager.merge(sourceEndpoint);

            entityTransaction.commit();
            return persistedSourceEndpoint;
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
