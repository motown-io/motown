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

import io.motown.ochp.viewmodel.persistence.entities.Identification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class IdentificationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(IdentificationRepository.class);

    private EntityManager entityManager;

    public void save(Identification identification) {
        EntityTransaction transaction = entityManager.getTransaction();

        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(identification);
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to persist identification.", e);
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
            List<Identification> result = entityManager.createQuery("SELECT i FROM Identification i", Identification.class).getResultList();
            for (Identification identification : result) {
                entityManager.remove(identification);
            }
            transaction.commit();
        } catch (Exception e) {
            LOG.error("Exception while trying to delete all identifications.", e);
            transaction.rollback();
            throw e;
        }
    }

    public Identification findByIdentificationId(String identificationId) {
        List<Identification> result = entityManager.createQuery("SELECT i FROM Identification i WHERE i.identificationId = :identificationId", Identification.class)
                .setParameter("identificationId", identificationId)
                .getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

    public List<Identification> all() {
        List<Identification> result = entityManager.createQuery("SELECT i FROM Identification i", Identification.class)
                .getResultList();
        return result;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
