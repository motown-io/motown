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
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.domain.FutureEventCallback;
import io.motown.ocpp.websocketjson.request.chargingstation.AuthorizeRequest;
import org.atmosphere.websocket.WebSocket;

public class AuthorizeRequestHandler extends RequestHandler {

    public static final String PROC_URI = "authorize";

    private Gson gson;

    private DomainService domainService;

    public AuthorizeRequestHandler(Gson gson, DomainService domainService) {
        this.gson = gson;
        this.domainService = domainService;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        FutureEventCallback futureEventCallback = new AuthorizationFutureEventCallback(callId, webSocket, gson);

        AuthorizeRequest request = gson.fromJson(payload, AuthorizeRequest.class);

        // futureEventCallback will handle authorize result
        domainService.authorize(chargingStationId, request.getIdTag(), futureEventCallback);
    }
}
