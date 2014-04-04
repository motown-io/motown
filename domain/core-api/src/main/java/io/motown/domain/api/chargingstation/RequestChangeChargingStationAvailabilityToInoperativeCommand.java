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
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestChangeChargingStationAvailabilityToInoperativeCommand} is the command which is published when a change availability to inoperative
 * of a charging station is requested.
 */
public final class RequestChangeChargingStationAvailabilityToInoperativeCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final EvseId evseId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestChangeChargingStationAvailabilityToInoperativeCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param evseId            the identifier of the evse.
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code evseId} or {@code identityContext} is {@code null}.
     */
    public RequestChangeChargingStationAvailabilityToInoperativeCommand(ChargingStationId chargingStationId, EvseId evseId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.evseId = checkNotNull(evseId);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the evse id.
     *
     * @return the evse id.
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }
}
