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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestCancelReservationCommand} is the command which is published when a reservation should be cancelled.
 */
public final class RequestCancelReservationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final ReservationId reservationId;

    /**
     * Creates a {@code RequestCancelReservationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param reservationId
     * @throws NullPointerException if {@code chargingStationId} or {@code reservationId} is {@code null}.
     */
    public RequestCancelReservationCommand(ChargingStationId chargingStationId, ReservationId reservationId) {
        this.chargingStationId = checkNotNull(chargingStationId);
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
     * Gets the reservation identifier.
     *
     * @return the reservation identifier.
     */
    public ReservationId getReservationId() {
        return reservationId;
    }
}
