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

import io.motown.domain.api.security.IdentityContext;

import java.util.Set;

/**
 * Event generated when new opening times have been added to existing ones.
 */
public final class ChargingStationOpeningTimesAddedEvent extends ChargingStationOpeningTimesChangedEvent {

    /**
     * Creates a new {@code ChargingStationOpeningTimesAddedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param openingTimes      the new opening times.
     * @param identityContext   the identity context.
     * @throws java.lang.NullPointerException if one of the parameters is {@code null}.
     */
    public ChargingStationOpeningTimesAddedEvent(ChargingStationId chargingStationId, Set<OpeningTime> openingTimes, IdentityContext identityContext) {
        super(chargingStationId, openingTimes, identityContext);
    }
}
