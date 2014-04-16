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

import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code InformRequestResultCommand} serves as a base class for commands which will inform about the resulting status of a request
 * that has been sent to the charging station.
 */
public final class NotReserveNowCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String reason;

    private final IdentityContext identityContext;

    private final ReservationId reservationId;

    private final EvseId evseId;

    private final Date expiryDate;

    /**
     * Creates a {@code InformRequestResultCommand} with an identifier and new status.
     *
     * @param chargingStationId   the identifier of the charging station.
     * @param reservationId       the unique reservation identifier.
     * @param evseId              the evse for which a reservatino attempt has been made.
     * @param reason              the reason the reservation was not successfull.
     * @param identityContext     identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code status}, {@code statusMessage} or {@code identityContext} is {@code null}.
     */
    public NotReserveNowCommand(ChargingStationId chargingStationId, ReservationId reservationId, EvseId evseId, Date expiryDate, String reason, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.reason = checkNotNull(reason);
        this.identityContext = checkNotNull(identityContext);
        this.reservationId = checkNotNull(reservationId);
        this.evseId = checkNotNull(evseId);
        this.expiryDate = checkNotNull(expiryDate);
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
     * Gets the resulting protocol specific status message.
     *
     * @return the reason
     */
    public String getReason() {
        return reason;
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

    /**
     * Gets the evse identifier.
     *
     * @return evse identifier
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * The expiration date of the reservation.
     * @return expiration Date
     */
    public Date getExpiryDate() {
        return expiryDate != null ? new Date(expiryDate.getTime()) : null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, reason, identityContext, reservationId, evseId, expiryDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NotReserveNowCommand other = (NotReserveNowCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.reason, other.reason) && Objects.equals(this.identityContext, other.identityContext) && Objects.equals(this.reservationId, other.reservationId) && Objects.equals(this.evseId, other.evseId) && Objects.equals(this.expiryDate, other.expiryDate);
    }
}
