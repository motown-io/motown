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

import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.EVSES;

public final class VasViewModelTestUtils {

    public static final String CHARGING_STATION_ADDRESS = "127.0.0.1";

    private VasViewModelTestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static ChargingStation getRegisteredAndConfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId(), CHARGING_STATION_ADDRESS);
        cs.setRegistered(true);
        cs.setNumberOfEvses(EVSES.size());
        cs.setConfigured(true);

        return cs;
    }
}
