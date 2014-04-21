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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ReservationCancelledEvent} is published when a reservation cancellation has been successfully processed.
 */
public final class ReservationCancelledEvent {

    private final ChargingStationId chargingStationId;

    private final ReservationId reservationId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ReservationCancelledEvent}.
     *
     * @param chargingStationId charging station identifier.
     * @param reservationId     reservation identifier.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code reservationId} or {@code identityContext} is {@code null}.
     */
    public ReservationCancelledEvent(ChargingStationId chargingStationId, ReservationId reservationId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.reservationId = checkNotNull(reservationId);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station's id.
     *
     * @return the charging station's id.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the reservation identifier.
     *
     * @return reservation identifier.
     */
    public ReservationId getReservationId() {
        return reservationId;
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, reservationId, identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ReservationCancelledEvent other = (ReservationCancelledEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.reservationId, other.reservationId) && Objects.equals(this.identityContext, other.identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("reservationId", reservationId)
                .add("identityContext", identityContext)
                .toString();
    }
}
