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
import javax.persistence.Query;
import java.util.List;

public class ChargingStationTypeRepository {

    private EntityManager entityManager;

    public List<ChargingStationType> findByCodeAndManufacturerCode(String code, String manufacturerCode) {
        @SuppressWarnings("JpaQlInspection")
        Query query = entityManager.createQuery("SELECT cst FROM ChargingStationType AS cst where UPPER(cst.code) = UPPER(:code) and UPPER(cst.manufacturer.code) = UPPER(:manufacturerCode)").setParameter("code", code).setParameter("manufacturerCode", manufacturerCode);
        return (List<ChargingStationType>) query.getResultList();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
