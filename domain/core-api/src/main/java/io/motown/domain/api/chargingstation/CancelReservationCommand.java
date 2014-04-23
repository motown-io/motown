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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code CancelReservationCommand} is the command that triggers the cancellation of a reservation.
 */
public final class CancelReservationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final ReservationId reservationId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code CancelReservationCommand}.
     *
     * @param chargingStationId   the identifier of the charging station.
     * @param reservationId       the unique reservation identifier.
     * @param identityContext     identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code status}, {@code statusMessage} or {@code identityContext} is {@code null}.
     */
    public CancelReservationCommand(ChargingStationId chargingStationId, ReservationId reservationId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.identityContext = checkNotNull(identityContext);
        this.reservationId = checkNotNull(reservationId);
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
     * Gets the unique reservation identifier.
     *
     * @return reservation identifier
     */
    public ReservationId getReservationId() {
        return reservationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, identityContext, reservationId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CancelReservationCommand other = (CancelReservationCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.identityContext, other.identityContext) && Objects.equals(this.reservationId, other.reservationId);
    }
}
