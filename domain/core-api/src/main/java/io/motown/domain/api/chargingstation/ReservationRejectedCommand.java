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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import io.motown.domain.api.security.IdentityContext;

/**
 * {@code ReservationRejectedCommand} will mark an EVSE as reserved.
 */
public final class ReservationRejectedCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final IdentityContext identityContext;
    private final EvseId evseId;

    /**
     * Creates a {@code ReservationRejectedCommand}.
     *
     * @param chargingStationId   the identifier of the charging station.
     * @param evseId              the evse for which a reservatino attempt has been made.
     * @param identityContext     identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code status}, {@code statusMessage} or {@code identityContext} is {@code null}.
     */
    public ReservationRejectedCommand(ChargingStationId chargingStationId, EvseId evseId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.identityContext = checkNotNull(identityContext);
        this.evseId = checkNotNull(evseId);
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
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    /**
     * Gets the evse identifier.
     *
     * @return evse identifier
     */
    public EvseId getEvseId() {
        return evseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, identityContext, evseId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ReservationRejectedCommand other = (ReservationRejectedCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.identityContext, other.identityContext) && Objects.equals(this.evseId, other.evseId);
    }
}
