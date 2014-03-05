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

import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ChargingStationRepository {

    private EntityManager entityManager;

    public void save(ChargingStation chargingStation) {
        entityManager.getTransaction().begin();
        entityManager.persist(chargingStation);
        entityManager.getTransaction().commit();
    }

    public ChargingStation findOne(String id) {
        return entityManager.find(ChargingStation.class, id);
    }

    public List<ChargingStation> findAll() {
        Query query = entityManager.createQuery("SELECT cs FROM io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation AS cs");
        return (List<ChargingStation>) query.getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}