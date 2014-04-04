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

import javax.annotation.Nullable;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ReserveNowRequestedForUnreservableChargingStationEvent} is thrown when an attempt is made to reserve a
 * charging station which is not reservable.
 */
public final class ReserveNowRequestedForUnreservableChargingStationEvent {

    private final ChargingStationId chargingStationId;

    private final ReservationId reservationId;

    private final EvseId evseId;

    private final IdentifyingToken identifyingToken;

    private final Date expiryDate;

    private final IdentifyingToken parentIdentifyingToken;

    /**
     * Creates a {@code ReserveNowRequestedForUnreservableChargingStationEvent} with a charging station identifier, evse
     * identifier, identifying token, expiry date and parent identifying token.
     *
     * @param chargingStationId         charging station identifier.
     * @param evseId                    identifier of the EVSE.
     * @param identifyingToken          identifier of the token that would have reserved the charging station.
     * @param expiryDate                date at which the reservation would expire.
     * @param parentIdentifyingToken    parent identifier that would have reserved the charging station.
     * @throws NullPointerException     if chargingStationId, evseId, identifyingToken or expiryDate is null.
     */
    public ReserveNowRequestedForUnreservableChargingStationEvent(ChargingStationId chargingStationId, ReservationId reservationId, EvseId evseId, IdentifyingToken identifyingToken, Date expiryDate, @Nullable IdentifyingToken parentIdentifyingToken) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.reservationId = reservationId;
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.expiryDate = new Date(checkNotNull(expiryDate).getTime());
        this.parentIdentifyingToken = parentIdentifyingToken;
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public EvseId getEvseId() {
        return evseId;
    }

    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    public Date getExpiryDate() {
        return new Date(expiryDate.getTime());
    }

    public IdentifyingToken getParentIdentifyingToken() {
        return parentIdentifyingToken;
    }
}
