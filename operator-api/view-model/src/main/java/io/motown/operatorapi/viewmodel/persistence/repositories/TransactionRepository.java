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
package io.motown.operatorapi.viewmodel.persistence.repositories;

import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class TransactionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionRepository.class);

    private EntityManager entityManager;

    /**
     * Find transactions by transaction id (not the auto-increment transaction.id)
     */
    public List<Transaction> findByTransactionId(String transactionId) {
        Query query = entityManager.createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Transaction AS t WHERE t.transactionId = :transactionId").setParameter("transactionId", transactionId);
        return (List<Transaction>) query.getResultList();
    }

    public List<Transaction> findAll() {
        Query query = entityManager.createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Transaction AS t");
        return (List<Transaction>) query.getResultList();
    }

    public void save(Transaction transaction) {
        EntityTransaction entityTransaction = entityManager.getTransaction();

        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }

        try {
            entityManager.persist(transaction);
            entityTransaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to persist transaction.", e);
            entityTransaction.rollback();
            throw e;
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
