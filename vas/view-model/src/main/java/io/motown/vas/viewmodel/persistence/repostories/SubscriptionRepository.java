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
package io.motown.vas.viewmodel.persistence.repostories;

import io.motown.vas.viewmodel.persistence.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class SubscriptionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionRepository.class);

    private EntityManagerFactory entityManagerFactory;

    public void insert(final Subscription subscription) {
        final EntityManager entityManager = getEntityManager();
        executeWithinTransaction(new TransactionalTask() {
            @Override
            public void execute() {
                entityManager.persist(subscription);
            }
        }, entityManager);
    }

    public void delete(final Subscription subscription) {
        final EntityManager entityManager = getEntityManager();

        try {
            //Making sure the subscription is in the scope of this entitymanager
            final Subscription subscriptionToRemove = entityManager.contains(subscription) ? subscription : entityManager.merge(subscription);

            executeWithinTransaction(new TransactionalTask() {
                @Override
                public void execute() {
                    entityManager.remove(subscriptionToRemove);
                }
            }, entityManager);
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }

    }

    private void executeWithinTransaction(TransactionalTask task, EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            task.execute();
            transaction.commit();
        } finally {
            if (transaction != null && transaction.isActive()) {
                LOG.warn("Transaction is still active while it should not be, rolling back.");
                transaction.rollback();
            }
            entityManager.close();
        }
    }

    public Subscription findById(Long id) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.find(Subscription.class, id);
        } finally {
            entityManager.close();
        }
    }

    public Subscription findBySubscriptionId(String subscriptionId) {
        EntityManager entityManager = getEntityManager();
        try {
            List<Subscription> subscriptions = entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.persistence.entities.Subscription AS s " +
                    "WHERE s.subscriptionId = :subscriptionId", Subscription.class)
                    .setParameter("subscriptionId", subscriptionId)
                    .getResultList();

            return !subscriptions.isEmpty() ? subscriptions.get(0) : null;
        } finally {
            entityManager.close();
        }
    }

    public Subscription findBySubscriberIdentityAndDeliveryAddress(String subscriberIdentity, String deliveryAddress) {
        EntityManager entityManager = getEntityManager();
        try {
            List<Subscription> subscriptions = entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.persistence.entities.Subscription AS s " +
                    "WHERE s.subscriberIdentity = :subscriberIdentity " +
                    "AND s.deliveryAddress = :deliveryAddress", Subscription.class)
                    .setParameter("subscriberIdentity", subscriberIdentity)
                    .setParameter("deliveryAddress", deliveryAddress)
                    .getResultList();

            return !subscriptions.isEmpty() ? subscriptions.get(0) : null;
        } finally {
            entityManager.close();
        }
    }

    public List<Subscription> findBySubscriberIdentity(String subscriberIdentity) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.persistence.entities.Subscription AS s " +
                    "WHERE s.subscriberIdentity = :subscriberIdentity", Subscription.class)
                    .setParameter("subscriberIdentity", subscriberIdentity)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public List<Subscription> findAll() {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.persistence.entities.Subscription AS s", Subscription.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

    private static interface TransactionalTask {
        void execute();
    }
}
