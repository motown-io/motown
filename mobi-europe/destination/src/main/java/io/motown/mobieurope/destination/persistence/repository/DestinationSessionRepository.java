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

import io.motown.mobieurope.shared.persistence.entities.SessionInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class DestinationSessionRepository {

    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public SessionInfo findSessionInfoByAuthorizationId(String authorizationId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.createQuery("SELECT si FROM SessionInfo AS si WHERE si.authorizationIdentifier = :authorizationIdentifier", SessionInfo.class)
                    .setParameter("authorizationIdentifier", authorizationId)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public SessionInfo findSessionInfoByTransactionId(Integer transactionId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.createQuery("SELECT si FROM SessionInfo AS si WHERE si.transactionId = :transactionId", SessionInfo.class)
                    .setParameter("transactionId", transactionId)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public SessionInfo findSessionInfoByUserId(String userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager.createQuery("SELECT si FROM SessionInfo AS si WHERE si.requestIdentifier = :userIdentifier", SessionInfo.class)
                    .setParameter("userIdentifier", userId)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public SessionInfo insertOrUpdateSessionInfo(SessionInfo sessionInfo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;

        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            SessionInfo persistedSessionInfo;
            if (sessionInfo.getId() == null) {
                entityManager.persist(sessionInfo);
                persistedSessionInfo = sessionInfo;
            } else {
                persistedSessionInfo = entityManager.merge(sessionInfo);
            }
            entityTransaction.commit();
            return persistedSessionInfo;
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
