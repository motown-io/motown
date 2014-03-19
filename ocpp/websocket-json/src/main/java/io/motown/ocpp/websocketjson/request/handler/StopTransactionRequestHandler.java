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
import io.motown.ocpp.websocketjson.request.chargingstation.StopTransactionRequest;
import io.motown.ocpp.websocketjson.request.chargingstation.TransactionData;
import io.motown.ocpp.websocketjson.response.centralsystem.AuthorizationStatus;
import io.motown.ocpp.websocketjson.response.centralsystem.IdTagInfo;
import io.motown.ocpp.websocketjson.response.centralsystem.StopTransactionResponse;

import java.util.*;

public class StopTransactionRequestHandler implements RequestHandler {

    private String protocolIdentifier;

    private Gson gson;

    private DomainService domainService;

    public StopTransactionRequestHandler(Gson gson, DomainService domainService, String protocolIdentifier) {
        this.gson = gson;
        this.domainService = domainService;
        this.protocolIdentifier = protocolIdentifier;
    }

    @Override
    public StopTransactionResponse handleRequest(ChargingStationId chargingStationId, String payload) {
        StopTransactionRequest request = gson.fromJson(payload, StopTransactionRequest.class);

        List<MeterValue> meterValues = new ArrayList<>();
        for(TransactionData data : request.getTransactionData()) {
            for(io.motown.ocpp.websocketjson.request.chargingstation.MeterValue meterValue : data.getValues()) {
                for(io.motown.ocpp.websocketjson.request.chargingstation.MeterValue.Value value : meterValue.getValues()) {
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
        if(requestTransactionId != null && requestTransactionId > 0) {
            transactionId = new NumberedTransactionId(chargingStationId, protocolIdentifier, requestTransactionId);
        }

        domainService.stopTransaction(chargingStationId, transactionId, new TextualToken(request.getIdTag()), request.getMeterStop(), request.getTimestamp(), meterValues);

        // TODO locally store identifications, so we can use these in the response. - Dennis Laumen, December 16th 2013
        GregorianCalendar expDate = new GregorianCalendar();
        expDate.add(GregorianCalendar.YEAR, 1);
        return new StopTransactionResponse(new IdTagInfo(AuthorizationStatus.ACCEPTED, expDate.getTime(), request.getIdTag()));
    }

}
