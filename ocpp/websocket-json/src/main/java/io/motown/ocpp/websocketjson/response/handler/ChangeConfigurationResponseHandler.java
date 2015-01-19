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
import io.motown.domain.api.chargingstation.ConfigurationItem;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.ChangeconfigurationResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code ResponseHandler} which handles responses for changing configuration.
 */
public class ChangeConfigurationResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ChangeConfigurationResponseHandler.class);

    private final ConfigurationItem configurationItem;

    /**
     * Creates a new {@code ChangeConfigurationResponseHandler}.
     *
     * @param configurationItem the configuration item which was changed with the request.
     * @param correlationToken  the correlation token.
     */
    public ChangeConfigurationResponseHandler(ConfigurationItem configurationItem, CorrelationToken correlationToken) {
        this.configurationItem = configurationItem;
        this.setCorrelationToken(correlationToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        ChangeconfigurationResponse response = gson.fromJson(wampMessage.getPayloadAsString(), ChangeconfigurationResponse.class);

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Configuration change of {} on {} has been accepted", configurationItem.getKey(), configurationItem.getValue());
                domainService.changeConfiguration(chargingStationId, configurationItem, getCorrelationToken(), addOnIdentity);
                break;
            case REJECTED:
                LOG.info("Configuration change of {} on {} was rejected", configurationItem.getKey(), configurationItem.getValue());
                break;
            case NOT_SUPPORTED:
                LOG.info("Configuration change of {} on {} was not supported", configurationItem.getKey(), configurationItem.getValue());
                break;
            default:
                throw new AssertionError("Configuration change returned unknown response status " + response.getStatus());
        }
    }
}
