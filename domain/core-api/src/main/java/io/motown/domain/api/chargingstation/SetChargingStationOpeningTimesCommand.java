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
 * Command that is used to set opening times of the charging station.
 */
public final class SetChargingStationOpeningTimesCommand extends ChangeChargingStationOpeningTimesCommand {

    /**
     * Creates a command object that sets opening times of a charging station.
     *
     * @param chargingStationId The identifier of the charging station.
     * @param openingTimes      The opening times of the charging station.
     * @param identityContext   the identity context.
     * @throws java.lang.NullPointerException if either {@code chargingStationId}, {@code openingTimes} or {@code identityContext} is {@code null}.
     */
    public SetChargingStationOpeningTimesCommand(ChargingStationId chargingStationId, Set<OpeningTime> openingTimes, IdentityContext identityContext) {
        super(chargingStationId, openingTimes, identityContext);
    }
}
