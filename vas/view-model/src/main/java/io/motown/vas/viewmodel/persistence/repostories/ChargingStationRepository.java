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

import io.motown.vas.viewmodel.model.ChargingStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class ChargingStationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationRepository.class);

    private EntityManager entityManager;

    public void insert(ChargingStation chargingStation) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(chargingStation);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                LOG.warn("Transaction is still active while it should not be, rolling back.");
                transaction.rollback();
            }
        }
    }

    public ChargingStation findByChargingStationId(String chargingStationId) {
        Query query = entityManager.createQuery("SELECT cs FROM io.motown.vas.viewmodel.model.ChargingStation AS cs WHERE cs.chargingStationId = :chargingStationId").setParameter("chargingStationId", chargingStationId);
        List resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return (ChargingStation) resultList.get(0);
        } else {
            return null;
        }
    }

    public List<ChargingStation> findAll() {
        Query query = entityManager.createQuery("SELECT cs FROM io.motown.vas.viewmodel.model.ChargingStation AS cs");
        return (List<ChargingStation>) query.getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
