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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.RemotestarttransactionResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code ResponseHandler} which handles responses for start transaction.
 */
public class RemoteStartTransactionResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteStartTransactionResponseHandler.class);

    /**
     * Creates a new {@code RemoteStartTransactionResponseHandler}.
     *
     * @param correlationToken the correlation token.
     */
    public RemoteStartTransactionResponseHandler(CorrelationToken correlationToken) {
        this.setCorrelationToken(correlationToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        RemotestarttransactionResponse response = gson.fromJson(wampMessage.getPayloadAsString(), RemotestarttransactionResponse.class);

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Remote start transaction request on {} has been accepted", chargingStationId);
                break;
            case REJECTED:
                LOG.info("Remote start transaction request on {} has been rejected", chargingStationId);
                break;
            default:
                throw new AssertionError(String.format("Unknown start transaction response status: %s", response.getStatus()));
        }
    }
}
