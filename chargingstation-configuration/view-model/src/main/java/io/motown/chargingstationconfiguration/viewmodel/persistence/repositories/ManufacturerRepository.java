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
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ManufacturerRepository {

    private EntityManagerFactory entityManagerFactory;

    public Manufacturer createOrUpdate(Manufacturer manufacturer) {
        EntityManager em = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            Manufacturer persistedManufacturer = em.merge(manufacturer);

            tx.commit();

            return persistedManufacturer;
        } catch (Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Manufacturer> findAll(int page, int recordsPerPage) {
        return getEntityManager().createQuery("SELECT m FROM Manufacturer m", Manufacturer.class)
                .setFirstResult((page - 1) * recordsPerPage)
                .setMaxResults(recordsPerPage)
                .getResultList();
    }

    public Long getTotalNumberOfManufacturers() {
        return getEntityManager().createQuery("SELECT COUNT(m) FROM Manufacturer m", Long.class).getSingleResult();
    }

    public Manufacturer findOne(Long id) {
        return findOne(id, getEntityManager());
    }

    public void delete(Long id) {
        EntityManager em = getEntityManager();

        Manufacturer manufacturer = findOne(id, em);

        List<ChargingStationType> chargingStationTypes = em.createQuery("SELECT cst FROM ChargingStationType AS cst where UPPER(cst.manufacturer.code) = UPPER(:manufacturerCode)", ChargingStationType.class)
                .setParameter("manufacturerCode", manufacturer.getCode())
                .getResultList();

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            for (ChargingStationType chargingStationType:chargingStationTypes) {
                em.remove(chargingStationType);
            }

            em.remove(manufacturer);

            tx.commit();
        } catch (Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private Manufacturer findOne(Long id, EntityManager entityManager) {
        Manufacturer manufacturer = entityManager.find(Manufacturer.class, id);
        if (manufacturer != null) {
            return manufacturer;
        }
        throw new EntityNotFoundException(String.format("Unable to find manufacturer with id '%s'", id));
    }
}
