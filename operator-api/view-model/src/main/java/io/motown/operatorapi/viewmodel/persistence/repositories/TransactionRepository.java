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
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class TransactionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionRepository.class);

    private EntityManagerFactory entityManagerFactory;

    public Transaction createOrUpdate(Transaction transaction) {
        EntityManager entityManager = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Transaction persistedTransaction = entityManager.merge(transaction);

            tx.commit();

            return persistedTransaction;
        } catch (Exception e) {
            LOG.error("Exception while trying to persist transaction.", e);
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Find transactions by transaction id (not the auto-increment transaction.id)
     */
    public Transaction findByTransactionId(String transactionId) {
        return getEntityManager().createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Transaction AS t WHERE t.transactionId = :transactionId", Transaction.class)
                .setParameter("transactionId", transactionId)
                .getSingleResult();
    }

    public List<Transaction> findAll(int offset, int limit) {
        return getEntityManager().createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Transaction AS t", Transaction.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long getTotalNumberOfTransactions() {
        return getEntityManager().createQuery("SELECT COUNT(t) FROM io.motown.operatorapi.viewmodel.persistence.entities.Transaction t", Long.class).getSingleResult();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
