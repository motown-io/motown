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
package io.motown.chargingstationconfiguration.viewmodel.persistence.repositories;

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ChargingStationTypeRepository {

    private EntityManager entityManager;

    public List<ChargingStationType> findByCodeAndManufacturerCode(String code, String manufacturerCode) {
        return entityManager.createQuery("SELECT cst FROM ChargingStationType AS cst where UPPER(cst.code) = UPPER(:code) and UPPER(cst.manufacturer.code) = UPPER(:manufacturerCode)", ChargingStationType.class)
                .setParameter("code", code)
                .setParameter("manufacturerCode", manufacturerCode)
                .getResultList();
    }

    public void create(ChargingStationType chargingStationType) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(chargingStationType);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void update(ChargingStationType chargingStationType) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.merge(chargingStationType);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<ChargingStationType> findAll() {
        return entityManager.createQuery("SELECT cst FROM ChargingStationType cst", ChargingStationType.class).getResultList();
    }

    public ChargingStationType findOne(Long id) {
        ChargingStationType chargingStationType = entityManager.find(ChargingStationType.class, id);
        if (chargingStationType != null) {
            return chargingStationType;
        }
        throw new EntityNotFoundException(String.format("Unable to find charging station type with id '%s'", id));
    }

    public void delete(Long id) {
        ChargingStationType chargingStationType = findOne(id);
        if (chargingStationType != null) {
            EntityTransaction transaction = entityManager.getTransaction();

            if (!transaction.isActive()) {
                transaction.begin();
            }

            try {
                entityManager.remove(chargingStationType);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } else {
            throw new EntityNotFoundException(String.format("Unable to find charging station type with id '%s'", id));
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
