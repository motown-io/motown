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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestResult;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.DatatransferResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTransferResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DataTransferResponseHandler.class);

    public DataTransferResponseHandler(CorrelationToken correlationToken) {
        this.setCorrelationToken(correlationToken);
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        DatatransferResponse response = gson.fromJson(wampMessage.getPayloadAsString(), DatatransferResponse.class);

        DatatransferResponse.Status responseStatus =  response.getStatus();
        if(DatatransferResponse.Status.UNKNOWN_MESSAGE_ID.equals(responseStatus)) {
            LOG.warn(String.format("Unknown message id for datatransfer request with correlation %s", getCorrelationToken().getToken()));
        }
        if(DatatransferResponse.Status.UNKNOWN_VENDOR_ID.equals(responseStatus)) {
            LOG.warn(String.format("Unknown vendor id for datatransfer request with correlation %s", getCorrelationToken().getToken()));
        }

        RequestResult requestResult = DatatransferResponse.Status.ACCEPTED.equals(responseStatus) ? RequestResult.SUCCESS : RequestResult.FAILURE;

        domainService.informRequestResult(chargingStationId, requestResult, getCorrelationToken(), "");
    }
}
