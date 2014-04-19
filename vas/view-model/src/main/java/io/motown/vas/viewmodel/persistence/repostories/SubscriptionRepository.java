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

import io.motown.vas.viewmodel.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class SubscriptionRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionRepository.class);
    private static final String SUBSCRIBER_IDENTITY_PARAMETER = "subscriberIdentity";

    private EntityManager entityManager;

    public void insert(final Subscription subscription) {
        executeWithinTransaction(new TransactionalTask() {
            @Override
            public void execute() {
                entityManager.persist(subscription);
            }
        });
    }

    public void delete(final Subscription subscription) {
        executeWithinTransaction(new TransactionalTask() {
            @Override
            public void execute() {
                entityManager.remove(subscription);
            }
        });
    }

    private void executeWithinTransaction(TransactionalTask task) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            task.execute();
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                LOG.warn("Transaction is still active while it should not be, rolling back.");
                transaction.rollback();
            }
        }
    }

    public Subscription findById(Long id) {
        return entityManager.find(Subscription.class, id);
    }

    public Subscription findBySubscriptionId(String subscriptionId) {
        Query query = entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.model.Subscription AS s WHERE s.subscriptionId = :subscriptionId").setParameter("subscriptionId", subscriptionId);
        List resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return (Subscription) resultList.get(0);
        } else {
            return null;
        }
    }

    public Subscription findBySubscriberIdentityAndDeliveryAddress(String subscriberIdentity, String deliveryAddress) {
        Query query = entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.model.Subscription AS s WHERE s.subscriberIdentity = :subscriberIdentity AND s.deliveryAddress = :deliveryAddress").setParameter(SUBSCRIBER_IDENTITY_PARAMETER, subscriberIdentity).setParameter("deliveryAddress", deliveryAddress);
        List resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return (Subscription) resultList.get(0);
        } else {
            return null;
        }
    }

    public List<Subscription> findBySubscriberIdentity(String subscriberIdentity) {
        Query query = entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.model.Subscription AS s WHERE s.subscriberIdentity = :subscriberIdentity").setParameter(SUBSCRIBER_IDENTITY_PARAMETER, subscriberIdentity);
        return (List<Subscription>) query.getResultList();
    }

    public List<Subscription> findAll() {
        Query query = entityManager.createQuery("SELECT s FROM io.motown.vas.viewmodel.model.Subscription AS s");
        return (List<Subscription>) query.getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static interface TransactionalTask {
        void execute();
    }
}
