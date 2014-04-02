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
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo__;
import io.motown.ocpp.websocketjson.schema.generated.v15.Starttransaction;
import io.motown.ocpp.websocketjson.schema.generated.v15.StarttransactionResponse;
import org.atmosphere.websocket.WebSocket;

import java.util.GregorianCalendar;

public class StartTransactionRequestHandler extends RequestHandler {

    public static final String PROC_URI = "starttransaction";

    private Gson gson;

    private DomainService domainService;

    private String protocolIdentifier;

    public StartTransactionRequestHandler(Gson gson, DomainService domainService, String protocolIdentifier) {
        this.gson = gson;
        this.domainService = domainService;
        this.protocolIdentifier = protocolIdentifier;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Starttransaction request = gson.fromJson(payload, Starttransaction.class);

        ReservationId reservationId = null;
        if (request.getReservationId() != 0) {
            reservationId = new NumberedReservationId(chargingStationId, protocolIdentifier, request.getReservationId().intValue());
        }

        int transactionId = domainService.startTransaction(chargingStationId, new EvseId(request.getConnectorId().intValue()), new TextualToken(request.getIdTag()), request.getMeterStart().intValue(), request.getTimestamp(), reservationId, protocolIdentifier);

        GregorianCalendar expDate = new GregorianCalendar();
        expDate.add(GregorianCalendar.YEAR, 1);

        // TODO locally store identifications, so we can use these in the response. - Dennis Laumen, December 16th 2013

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
