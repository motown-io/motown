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
package io.motown.ocpp.websocketjson.request.handler;

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.StartTransactionRequest;
import io.motown.ocpp.websocketjson.response.centralsystem.AuthorizationStatus;
import io.motown.ocpp.websocketjson.response.centralsystem.IdTagInfo;
import io.motown.ocpp.websocketjson.response.centralsystem.StartTransactionResponse;

import java.util.GregorianCalendar;

public class StartTransactionRequestHandler implements RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private String protocolIdentifier;

    public StartTransactionRequestHandler(Gson gson, DomainService domainService, String protocolIdentifier) {
        this.gson = gson;
        this.domainService = domainService;
        this.protocolIdentifier = protocolIdentifier;
    }

    @Override
    public StartTransactionResponse handleRequest(ChargingStationId chargingStationId, String payload) {
        StartTransactionRequest request = gson.fromJson(payload, StartTransactionRequest.class);

        ReservationId reservationId = null;
        if (request.getReservationId() != 0) {
            reservationId = new NumberedReservationId(chargingStationId, protocolIdentifier, request.getReservationId());
        }

        int transactionId = domainService.startTransaction(chargingStationId, new EvseId(request.getConnectorId()), new TextualToken(request.getIdTag()), request.getMeterStart(), request.getTimestamp(), reservationId, protocolIdentifier);

        // TODO locally store identifications, so we can use these in the response. - Dennis Laumen, December 16th 2013
        GregorianCalendar expDate = new GregorianCalendar();
        expDate.add(GregorianCalendar.YEAR, 1);
        return new StartTransactionResponse(transactionId, new IdTagInfo(AuthorizationStatus.ACCEPTED, expDate.getTime(), request.getIdTag()));
    }
}
