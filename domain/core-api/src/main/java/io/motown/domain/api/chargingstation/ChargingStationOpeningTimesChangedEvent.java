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
package io.motown.domain.api.chargingstation;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic event which is generated when the opening times of a charging station have either been set or added to.
 */
public abstract class ChargingStationOpeningTimesChangedEvent {
    private final ChargingStationId chargingStationId;
    private final Set<OpeningTime> openingTimes;

    /**
     * Creates a new {@code ChargingStationOpeningTimesChangedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param openingTimes the new opening times.
     * @throws java.lang.NullPointerException if one of the parameters is {@code null}.
     */
    protected ChargingStationOpeningTimesChangedEvent(ChargingStationId chargingStationId, Set<OpeningTime> openingTimes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.openingTimes = ImmutableSet.copyOf(checkNotNull(openingTimes));
    }

    /**
     * Gets the identifier of the charging station.
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the opening times.
     * @return the opening times.
     */
    public Set<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }
}
