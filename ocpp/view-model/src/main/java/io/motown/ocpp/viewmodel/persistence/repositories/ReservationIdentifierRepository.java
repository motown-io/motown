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
package io.motown.ocpp.viewmodel.persistence.repositories;

import io.motown.ocpp.viewmodel.persistence.entities.ReservationIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class ReservationIdentifierRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationIdentifierRepository.class);

    private EntityManagerFactory entityManagerFactory;

    public void insert(ReservationIdentifier reservationIdentifier) {
        EntityManager em = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            em.persist(reservationIdentifier);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                LOG.warn("Transaction is still active while it should not be, rolling back.");
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
        return this.entityManagerFactory.createEntityManager();
    }
}