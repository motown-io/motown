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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.MeterValue;
import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.*;
import org.atmosphere.websocket.WebSocket;

import java.util.*;

public class StopTransactionRequestHandler extends RequestHandler {

    private String protocolIdentifier;

    private Gson gson;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public StopTransactionRequestHandler(Gson gson, DomainService domainService, String protocolIdentifier, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.protocolIdentifier = protocolIdentifier;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Stoptransaction request = gson.fromJson(payload, Stoptransaction.class);

        List<MeterValue> meterValues = new ArrayList<>();
        for (TransactionDatum data : request.getTransactionData()) {
            for (Value__ meterValue : data.getValues()) {
                for (Value___ value : meterValue.getValues()) {
                    Map<String, String> attributes = new HashMap<>();
                    DomainService.addAttributeIfNotNull(attributes, DomainService.CONTEXT_KEY, value.getContext());
                    DomainService.addAttributeIfNotNull(attributes, DomainService.CONTEXT_KEY, value.getFormat());
                    DomainService.addAttributeIfNotNull(attributes, DomainService.CONTEXT_KEY, value.getLocation());
                    DomainService.addAttributeIfNotNull(attributes, DomainService.CONTEXT_KEY, value.getMeasurand());
                    DomainService.addAttributeIfNotNull(attributes, DomainService.CONTEXT_KEY, value.getUnit());
                    meterValues.add(new MeterValue(meterValue.getTimestamp(), value.getValue(), attributes));
                }
            }
        }

        NumberedTransactionId transactionId = null;
        Integer requestTransactionId = request.getTransactionId();
        if (requestTransactionId > 0) {
            transactionId = new NumberedTransactionId(chargingStationId, protocolIdentifier, requestTransactionId);
        }

        domainService.stopTransaction(chargingStationId, transactionId, new TextualToken(request.getIdTag()), request.getMeterStop(), request.getTimestamp(), meterValues, addOnIdentity);

        GregorianCalendar expDate = new GregorianCalendar();
        expDate.add(GregorianCalendar.YEAR, 1);

        IdTagInfo___ idTagInfo = new IdTagInfo___();
        idTagInfo.setStatus(IdTagInfo___.Status.ACCEPTED);
        idTagInfo.setExpiryDate(expDate.getTime());
        idTagInfo.setParentIdTag(request.getIdTag());

        StoptransactionResponse response = new StoptransactionResponse();
        response.setIdTagInfo(idTagInfo);

        writeResponse(webSocket, response, callId, gson);
    }

}
