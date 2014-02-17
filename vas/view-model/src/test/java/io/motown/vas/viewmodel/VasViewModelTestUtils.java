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

import io.motown.vas.viewmodel.model.ChargingStation;
import io.motown.vas.viewmodel.model.ComponentStatus;
import io.motown.vas.viewmodel.model.Evse;

import java.util.HashSet;
import java.util.Set;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;

public final class VasViewModelTestUtils {

    private VasViewModelTestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

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
