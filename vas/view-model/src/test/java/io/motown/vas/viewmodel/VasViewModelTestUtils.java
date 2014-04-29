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
package io.motown.vas.viewmodel;

import com.google.common.collect.ImmutableList;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;
import io.motown.vas.viewmodel.model.ComponentStatus;
import io.motown.vas.viewmodel.persistence.entities.Evse;
import io.motown.vas.viewmodel.persistence.entities.Subscription;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;

public final class VasViewModelTestUtils {

    private VasViewModelTestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static void deleteFromDatabase(EntityManager entityManager, Class jpaEntityClass) {
        entityManager.getTransaction().begin();
        List resultList = entityManager.createQuery("SELECT entity FROM " + jpaEntityClass.getName() + " as entity").getResultList();
        for (Object obj : resultList) {
            entityManager.remove(obj);
        }
        entityManager.getTransaction().commit();
    }

    public static List<Subscription> SUBSCRIPTIONS = ImmutableList.<Subscription>builder()
            .add(new Subscription("identity1", "http://localhost/identity1"))
            .add(new Subscription("identity2", "http://localhost/identity2"))
            .build();

    public static ChargingStation getRegisteredAndConfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setRegistered(true);
        cs.setConfigured(true);

        Set<Evse> evses = new HashSet<>();
        evses.add(new Evse(1, ComponentStatus.UNKNOWN));
        evses.add(new Evse(2, ComponentStatus.UNKNOWN));
        cs.setEvses(evses);

        return cs;
    }
}
