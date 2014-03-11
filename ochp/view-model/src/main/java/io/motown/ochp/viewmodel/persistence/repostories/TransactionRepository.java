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
package io.motown.ochp.viewmodel.persistence.repostories;

import io.motown.ochp.viewmodel.persistence.TransactionStatus;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class TransactionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionRepository.class);

    private EntityManager entityManager;

    public void save(Transaction t) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(t);
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to persist transaction.", e);
            transaction.rollback();
            throw e;
        }
    }

    public void save(Iterable<Transaction> transactions) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            for (Transaction t : transactions) {
                entityManager.persist(t);
            }
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to persist transaction.", e);
            transaction.rollback();
            throw e;
        }
    }

    public void deleteAll() {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            List<Transaction> result = entityManager.createQuery("SELECT t FROM Transaction t", Transaction.class).getResultList();
            for (Transaction t : result) {
                entityManager.remove(t);
            }
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to delete all transactions.", e);
            transaction.rollback();
            throw e;
        }
    }

    public Transaction findByTransactionId(String transactionId) {
        List<Transaction> result = entityManager.createQuery("SELECT t FROM Transaction t WHERE t.transactionId = :transactionId", Transaction.class)
                .setParameter("transactionId", transactionId)
                .getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

    public List<Transaction> findTransactionsByStatus(TransactionStatus status) {
        return entityManager.createQuery("SELECT t FROM Transaction t WHERE t.status = :status", Transaction.class)
                .setParameter("status", status)
                .getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
