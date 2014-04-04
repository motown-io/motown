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
package io.motown.operatorapi.viewmodel.model;

import io.motown.domain.api.chargingstation.EvseId;
import io.motown.domain.api.chargingstation.ReservationId;
import io.motown.domain.api.chargingstation.TextualToken;

import java.util.Date;

public class RequestReserveNowApiCommand implements ApiCommand {
    private EvseId evseId;
    private ReservationId reservationId;
    private TextualToken identifyingToken;
    private Date expiryDate;

    public RequestReserveNowApiCommand() {
    }

    public EvseId getEvseId() {
        return evseId;
    }

    public void setEvseId(EvseId evseId) {
        this.evseId = evseId;
    }

    public ReservationId getReservationId() {
        return reservationId;
    }

    public void setReservationId(ReservationId reservationId) {
        this.reservationId = reservationId;
    }

    public TextualToken getIdentifyingToken() {
        return identifyingToken;
    }

    public void setIdentifyingToken(TextualToken identifyingToken) {
        this.identifyingToken = identifyingToken;
    }

    public Date getExpiryDate() {
        return expiryDate != null ? new Date(expiryDate.getTime()) : null;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate != null ? new Date(expiryDate.getTime()) : null;
    }
}
