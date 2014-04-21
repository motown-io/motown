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

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ConfigurationItem;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.ConfigurationKey;
import io.motown.ocpp.websocketjson.schema.generated.v15.GetconfigurationResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;

import java.util.List;
import java.util.Set;

public class GetConfigurationResponseHandler extends ResponseHandler {

    public GetConfigurationResponseHandler(CorrelationToken correlationToken) {
        this.setCorrelationToken(correlationToken);
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        GetconfigurationResponse response = gson.fromJson(wampMessage.getPayloadAsString(), GetconfigurationResponse.class);

        domainService.receiveConfigurationItems(chargingStationId, toConfigurationItems(response.getConfigurationKey()), addOnIdentity);
    }

    /**
     * Converts a {@code List} of {@code ConfigurationKey}s to a {@code Set} of {@code ConfigurationItem}s.
     *
     * @param configurationItemMap a {@code List} of {@code ConfigurationKey}s.
     * @return a {@code Set} of {@code ConfigurationItem}s.
     */
    private Set<ConfigurationItem> toConfigurationItems(List<ConfigurationKey> configurationItemMap) {
        ImmutableSet.Builder<ConfigurationItem> configurationItemsBuilder = ImmutableSet.builder();

        for (ConfigurationKey key : configurationItemMap) {
            configurationItemsBuilder.add(new ConfigurationItem(key.getKey(), key.getValue()));
        }

        return configurationItemsBuilder.build();
    }
}
