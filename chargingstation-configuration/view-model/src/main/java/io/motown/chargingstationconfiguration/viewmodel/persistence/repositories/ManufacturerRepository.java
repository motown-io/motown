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

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ManufacturerRepository {

    private EntityManager entityManager;

    public void create(Manufacturer manufacturer) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(manufacturer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void update(Manufacturer manufacturer) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.merge(manufacturer);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<Manufacturer> findAll() {
        return entityManager.createQuery("SELECT m FROM Manufacturer m", Manufacturer.class).getResultList();
    }

    public Manufacturer findOne(Long id) {
        return entityManager.find(Manufacturer.class, id);
    }

    public void delete(Long id) {
        Manufacturer manufacturer = findOne(id);
        if (manufacturer != null) {
            EntityTransaction transaction = entityManager.getTransaction();

            if (!transaction.isActive()) {
                transaction.begin();
            }

            try {
                entityManager.remove(manufacturer);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } else {
            throw new IllegalArgumentException(String.format("Unable to find manufacturer with id '%s'", id));
        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
