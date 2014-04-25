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

/**
 * {@code RequestChangeChargingStationAvailabilityToOperativeCommand} is the command which is published when a change
 * availability to operative of a charging station is requested.
 */
public final class RequestChangeChargingStationAvailabilityToOperativeCommand extends RequestChangeAvailabilityToOperativeCommand {

    /**
     * Creates a {@code RequestChangeChargingStationAvailabilityToOperativeCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code identityContext} is {@code null}.
     */
    public RequestChangeChargingStationAvailabilityToOperativeCommand(ChargingStationId chargingStationId, IdentityContext identityContext) {
        super(chargingStationId, identityContext);
    }
}
