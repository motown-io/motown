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
package io.motown.ocpp.websocketjson.response.handler;

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.ReservenowResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class ReserveNowResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReserveNowResponseHandler.class);

    private final ReservationId reservationId;

    private final EvseId evseId;

    private final Date expiryDate;

    public ReserveNowResponseHandler(ReservationId reservationId, EvseId evseId, Date expiryDate, CorrelationToken correlationToken) {
        this.setCorrelationToken(correlationToken);
        this.reservationId = reservationId;
        this.evseId = evseId;
        this.expiryDate = new Date(expiryDate.getTime());
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        ReservenowResponse response = gson.fromJson(wampMessage.getPayloadAsString(), ReservenowResponse.class);

        switch(response.getStatus()) {
            case ACCEPTED: domainService.informReserved(chargingStationId, reservationId, evseId, expiryDate, getCorrelationToken(), addOnIdentity);
                break;
            case REJECTED:
            case FAULTED:
            case OCCUPIED:
            case UNAVAILABLE:
                LOG.info("Failed to reserve evse {} on charging station {}: {}", evseId.getId(), chargingStationId.getId(), response.getStatus().toString());
                break;
            default: throw new AssertionError(String.format("Unexpected status: {}", response.getStatus()));
        }
    }
}
