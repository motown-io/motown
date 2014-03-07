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
package io.motown.chargingstationconfiguration.viewmodel.domain;

import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.ChargingStationType;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Connector;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Evse;
import io.motown.chargingstationconfiguration.viewmodel.persistence.entities.Manufacturer;
import io.motown.domain.api.chargingstation.ChargingProtocol;
import io.motown.domain.api.chargingstation.ConnectorType;
import io.motown.domain.api.chargingstation.Current;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TestUtils {

    public static final int MAX_AMP_32 = 32;
    public static final int NUMBER_OF_CONNECTORS = 3;
    public static final int PHASE_3 = 3;
    public static final int VOLTAGE_230 = 230;

    private TestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static void deleteFromDatabase(EntityManager entityManager, Class jpaEntityClass) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            List resultList = entityManager.createQuery("SELECT entity FROM " + jpaEntityClass.getName() + " as entity").getResultList();
            for (Object obj : resultList) {
                entityManager.remove(obj);
            }
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public static void insertIntoDatabase(EntityManager entityManager, Object entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }

        try {
            entityManager.persist(entity);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    public static Manufacturer getManufacturerWithConfiguration(String vendor, String model) {
        Manufacturer manufacturer = getManufacturer(vendor);
        manufacturer.setChargingStationTypes(getChargingStationTypes(manufacturer, model));

        return manufacturer;
    }

    public static Set<ChargingStationType> getChargingStationTypes(Manufacturer manufacturer, String model) {
        ChargingStationType type = getChargingStationType(model);

        Evse evseFirst = getEvse(1);
        evseFirst.setConnectors(getConnectors(NUMBER_OF_CONNECTORS));

        Evse evseSecond = getEvse(2);
        evseSecond.setConnectors(getConnectors(NUMBER_OF_CONNECTORS));

        Set<Evse> evses = new HashSet<>(2);
        evses.add(evseFirst);
        evses.add(evseSecond);

        type.setEvses(evses);
        type.setManufacturer(manufacturer);

        Set<ChargingStationType> types = new HashSet<>();
        types.add(type);
        return types;
    }

    public static Manufacturer getManufacturer(String code) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setCode(code);
        return manufacturer;
    }

    public static ChargingStationType getChargingStationType(String code) {
        ChargingStationType type = new ChargingStationType();
        type.setCode(code);
        return type;
    }

    public static Evse getEvse(int identifier) {
        Evse evse = new Evse();
        evse.setIdentifier(identifier);
        return evse;
    }

    public static Set<Connector> getConnectors(int numberOfConnectors) {
        Set<Connector> connectors = new HashSet<>(numberOfConnectors);
        for (int i = 0; i < numberOfConnectors; i++) {
            connectors.add(getConnector());
        }
        return connectors;
    }

    public static Connector getConnector() {
        Connector connector = new Connector();
        connector.setChargingProtocol(ChargingProtocol.MODE3);
        connector.setConnectorType(ConnectorType.TESLA);
        connector.setCurrent(Current.AC);
        connector.setMaxAmp(MAX_AMP_32);
        connector.setPhase(PHASE_3);
        connector.setVoltage(VOLTAGE_230);
        return connector;
    }

}
