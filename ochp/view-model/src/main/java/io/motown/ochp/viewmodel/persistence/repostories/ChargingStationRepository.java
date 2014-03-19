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

import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ChargingStationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationRepository.class);

    private EntityManager entityManager;

    public void save(ChargingStation chargingStation) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(chargingStation);
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to persist chargingStation.", e);
            transaction.rollback();
            throw e;
        }
    }

    public List<ChargingStation> all() {
        List<ChargingStation> result = entityManager.createQuery("SELECT c FROM ChargingStation c", ChargingStation.class)
                .getResultList();
        return result;
    }

    public void deleteAll() {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            List<ChargingStation> result = entityManager.createQuery("SELECT cs FROM ChargingStation cs", ChargingStation.class).getResultList();
            for (ChargingStation chargingStation : result) {
                entityManager.remove(chargingStation);
            }
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to delete all chargingStations.", e);
            transaction.rollback();
            throw e;
        }
    }

    public ChargingStation findByChargingStationId(String chargingStationId) {
        List<ChargingStation> result = entityManager.createQuery("SELECT cs FROM ChargingStation cs WHERE cs.chargingStationId = :chargingStationId", ChargingStation.class)
                .setParameter("chargingStationId", chargingStationId)
                .getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
