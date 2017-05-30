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

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.EvseId;
import io.motown.domain.api.chargingstation.ReservationId;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.viewmodel.persistence.entities.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import java.util.List;

public class ReservationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationRepository.class);

    private EntityManagerFactory entityManagerFactory;

    public Reservation createOrUpdate(Reservation reservation) {
        EntityManager entityManager = getEntityManager();

        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            tx.begin();

            Reservation persistedReservation = entityManager.merge(reservation);

            tx.commit();

            return persistedReservation;
        } catch (Exception e) {
            LOG.error("Exception while trying to persist reservation.", e);
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Find reservations by reservation id (not the auto-increment reservation.id)
     */
    public Reservation findByReservationId(ReservationId reservationId) throws NoResultException {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Reservation AS t WHERE t.reservationId = :reservationId", Reservation.class)
                    .setParameter("reservationId", reservationId.getId())
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    /**
     * find reservations by chargingstationid, evseid and userId
     * 
     * @param chargingStationId
     * @param evseid
     * @param UserId
     * @return
     * @throws NoResultException
     */
    public Reservation findByChargingStationIdEvseIdUserId(ChargingStationId chargingStationId, EvseId evseid, UserIdentity UserId) throws NoResultException {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Reservation AS t WHERE t.chargingStationId = :chargingStationId AND evseId = :evseId AND userId = :userId", Reservation.class)
                    .setParameter("chargingStationId", chargingStationId.getId())
                    .setParameter("evseId", evseid)
                    .setParameter("userId", UserId.getId())
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    /**
     * Find all reservations
     * 
     * @param offset
     * @param limit
     * @return
     */
    public List<Reservation> findAll(int offset, int limit) {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT t FROM io.motown.operatorapi.viewmodel.persistence.entities.Reservation AS t", Reservation.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Long getTotalNumberOfReservations() {
        EntityManager entityManager = getEntityManager();
        try {
            return entityManager.createQuery("SELECT COUNT(t) FROM io.motown.operatorapi.viewmodel.persistence.entities.Reservation t", Long.class).getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}

