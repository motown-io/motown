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
package io.motown.identificationauthorization.localplugin.persistence.repositories;

import io.motown.identificationauthorization.localplugin.persistence.entities.LocalToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class TokenRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TokenRepository.class);

    private EntityManagerFactory entityManagerFactory;

    public LocalToken findToken(String hiddenId, String chargingStationId) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT token FROM LocalToken AS token JOIN token.chargingStations cs WHERE hiddenId = :hiddenId and cs.id = :chargingStationId", LocalToken.class)
                    .setParameter("hiddenId", hiddenId)
                    .setParameter("chargingStationId", chargingStationId)
                    .setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            entityManager.close();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public LocalToken createOrUpdate(LocalToken token) {
        EntityManager entityManager = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            LocalToken localToken = entityManager.merge(token);

            tx.commit();

            return localToken;
        } catch (Exception e) {
            LOG.error("Exception while trying to persist localToken.", e);
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

}