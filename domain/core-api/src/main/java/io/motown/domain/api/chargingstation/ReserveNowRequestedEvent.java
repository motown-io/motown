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

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ReserveNowRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final ReservationId reservationId;

    private final String protocol;

    private final EvseId evseId;

    private final IdentifyingToken identifyingToken;

    private final Date expiryDate;

    private final IdentifyingToken parentIdentifyingToken;

    public ReserveNowRequestedEvent(ChargingStationId chargingStationId, String protocol, ReservationId reservationId, EvseId evseId, IdentifyingToken identifyingToken, Date expiryDate, IdentifyingToken parentIdentifyingToken) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.reservationId = checkNotNull(reservationId);
        this.protocol = checkNotNull(protocol);
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.expiryDate = new Date(checkNotNull(expiryDate).getTime());
        this.parentIdentifyingToken = parentIdentifyingToken;
    }

    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    @Override
    public String getProtocol() {
        return protocol;
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
