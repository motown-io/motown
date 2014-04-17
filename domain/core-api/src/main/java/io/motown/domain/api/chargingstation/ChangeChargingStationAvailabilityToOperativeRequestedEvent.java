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
 * {@code ChangeChargingStationAvailabilityToOperativeRequestedEvent} is the event which is published when a request has
 * been made to change the availability of a charging station to operative.
 */
public final class ChangeChargingStationAvailabilityToOperativeRequestedEvent extends ChangeAvailabilityToOperativeRequestedEvent {

    /**
     * Creates a {@code ChangeComponentAvailabilityToOperativeRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @param identityContext   identity context.
     * @throws NullPointerException     if {@code chargingStationId}, {@code protocol}, {@code componentId}, or {@code identityContext} is {@code null}.
     * @throws IllegalArgumentException if {@code protocol} is empty.
     */
    public ChangeChargingStationAvailabilityToOperativeRequestedEvent(ChargingStationId chargingStationId, String protocol, IdentityContext identityContext) {
        super(chargingStationId, protocol, identityContext);
    }
}
