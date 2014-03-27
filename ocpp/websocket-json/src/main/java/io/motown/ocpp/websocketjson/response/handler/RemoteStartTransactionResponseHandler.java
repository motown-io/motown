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
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.response.chargingstation.RequestStatus;
import io.motown.ocpp.websocketjson.response.chargingstation.RemoteStartTransactionResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;

public class RemoteStartTransactionResponseHandler extends ResponseHandler {

    public RemoteStartTransactionResponseHandler(CorrelationToken correlationToken) {
        this.setCorrelationToken(correlationToken);
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService) {
        RemoteStartTransactionResponse response = gson.fromJson(wampMessage.getPayloadAsString(), RemoteStartTransactionResponse.class);
        RequestResult requestResult = response.getStatus().equals(RequestStatus.ACCEPTED) ? RequestResult.SUCCESS : RequestResult.FAILURE;

        domainService.informRequestResult(chargingStationId, requestResult, getCorrelationToken(), "");
    }

}
