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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo__;
import io.motown.ocpp.websocketjson.schema.generated.v15.Starttransaction;
import io.motown.ocpp.websocketjson.schema.generated.v15.StarttransactionResponse;
import org.atmosphere.websocket.WebSocket;

import java.util.GregorianCalendar;

public class StartTransactionRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private String protocolIdentifier;

    private AddOnIdentity addOnIdentity;

    public StartTransactionRequestHandler(Gson gson, DomainService domainService, String protocolIdentifier, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.protocolIdentifier = protocolIdentifier;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Starttransaction request = gson.fromJson(payload, Starttransaction.class);

        ReservationId reservationId = null;
        if (request.getReservationId() != 0) {
            reservationId = new NumberedReservationId(chargingStationId, protocolIdentifier, request.getReservationId());
        }

        int transactionId = domainService.startTransaction(chargingStationId, new EvseId(request.getConnectorId()), new TextualToken(request.getIdTag()), request.getMeterStart(), request.getTimestamp(), reservationId, protocolIdentifier, addOnIdentity);

        GregorianCalendar expDate = new GregorianCalendar();
        expDate.add(GregorianCalendar.YEAR, 1);

        IdTagInfo__ idTagInfo = new IdTagInfo__();
        idTagInfo.setExpiryDate(expDate.getTime());
        idTagInfo.setStatus(IdTagInfo__.Status.ACCEPTED);
        idTagInfo.setParentIdTag(request.getIdTag());

        StarttransactionResponse response = new StarttransactionResponse();
        response.setTransactionId((double) transactionId);
        response.setIdTagInfo(idTagInfo);

        writeResponse(webSocket, response, callId, gson);
    }
}
