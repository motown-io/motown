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

/**
 * {@code ReservationStatusChangedEvent} is the event which is published when the reservation status changed.
 * has booted.
 */
public final class ReservationStatusChangedEvent {

    private final ChargingStationId chargingStationId;

    private final ReservationId reservationId;

    private final ReservationStatus newStatus;

    /**
     * Creates a {@code ReservationStatusChangedEvent} with an identifier and the new status.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param reservationId     the identifier of the reservation.
     * @param newStatus         the new status.
     * @throws NullPointerException if {@code chargingStationId}, {@code reservationId} or {@code newStatus} is {@code null}.
     */
    public ReservationStatusChangedEvent(ChargingStationId chargingStationId, ReservationId reservationId, ReservationStatus newStatus) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.reservationId = checkNotNull(reservationId);
        this.newStatus = checkNotNull(newStatus);
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public ReservationStatus getNewStatus() {
        return newStatus;
    }

}
