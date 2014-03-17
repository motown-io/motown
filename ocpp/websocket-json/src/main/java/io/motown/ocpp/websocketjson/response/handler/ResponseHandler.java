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
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.wamp.WampMessage;

public abstract class ResponseHandler {

    private CorrelationToken correlationToken;

    abstract public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService);

    CorrelationToken getCorrelationToken() {
        return correlationToken;
    }

    void setCorrelationToken(CorrelationToken correlationToken) {
        this.correlationToken = correlationToken;
    }

}
