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

import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ChargingStationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationRepository.class);

    private EntityManager entityManager;

    public ChargingStation findOne(String id) {
        return entityManager.find(ChargingStation.class, id);
    }

    public void insert(ChargingStation chargingStation) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(chargingStation);
            transaction.commit();
        } catch (EntityExistsException e) {
            // because the identifier of the charging station entity is not generated it can occur that (for example)
            // 2 event handlers try to create the same charging station, therefore we catch this exception.
            LOG.warn("EntityExistsException while trying to persist chargingStation, other thread created charging station [{}] before we could.", chargingStation.getId(), e);
        } finally {
            if (transaction.isActive()) {
                LOG.warn("Transaction is still active while it should not be, rolling back.");
                transaction.rollback();
            }
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}