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
import io.motown.ocpp.websocketjson.schema.generated.v15.SendlocallistResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SendLocalListResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SendLocalListResponseHandler.class);

    private final int version;

    private final AuthorizationListUpdateType updateType;

    private final Set<IdentifyingToken> identifyingTokens;

    public SendLocalListResponseHandler(int version, AuthorizationListUpdateType updateType, Set<IdentifyingToken> identifyingTokens, CorrelationToken correlationToken) {
        this.version = version;
        this.updateType = updateType;
        this.identifyingTokens = identifyingTokens;
        this.setCorrelationToken(correlationToken);
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        SendlocallistResponse response = gson.fromJson(wampMessage.getPayloadAsString(), SendlocallistResponse.class);
        RequestResult requestResult = response.getStatus().equals(SendlocallistResponse.Status.ACCEPTED) ? RequestResult.SUCCESS : RequestResult.FAILURE;

        switch (requestResult) {
            case SUCCESS:
                domainService.authorizationListChange(chargingStationId, version, updateType, identifyingTokens, getCorrelationToken(), addOnIdentity);
                break;
            case FAILURE:
                LOG.info("Failed to send authorization list to charging station {}", chargingStationId.getId());
                break;
            default:
                throw new AssertionError(String.format("Unknown send local list response status: %s", requestResult));
        }
    }
}
