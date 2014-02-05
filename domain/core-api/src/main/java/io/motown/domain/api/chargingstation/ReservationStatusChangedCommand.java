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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ReservationStatusChangedCommand} is the command which is published when a charging station has indicated
 * the reservation status has changed.
 */
public final class ReservationStatusChangedCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final ReservationId reservationId;

    private final ReservationStatus newStatus;

    /**
     * Creates a {@code ReservationStatusChangedCommand} with an identifier and new status.
     *
     *
     * @param chargingStationId the identifier of the charging station.
     * @param reservationId the identifier of the reservation
     * @param newStatus the new status of the reservation
     * @throws NullPointerException if {@code chargingStationId}, {@code reservationId} or {@code newStatus} is {@code null}.
     */
    public ReservationStatusChangedCommand(ChargingStationId chargingStationId, ReservationId reservationId, ReservationStatus newStatus) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.reservationId = checkNotNull(reservationId);
        this.newStatus = checkNotNull(newStatus);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public ReservationStatus getNewStatus() {
        return newStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, reservationId, newStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ReservationStatusChangedCommand other = (ReservationStatusChangedCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.reservationId, other.reservationId) && Objects.equals(this.newStatus, other.newStatus);
    }
}
