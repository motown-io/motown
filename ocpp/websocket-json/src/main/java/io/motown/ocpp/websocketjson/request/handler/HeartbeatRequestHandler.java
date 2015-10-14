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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.HeartbeatResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessageHandler;
import org.atmosphere.websocket.WebSocket;

import java.util.Date;

public class HeartbeatRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private final AddOnIdentity addOnIdentity;

    public HeartbeatRequestHandler(Gson gson, DomainService domainService, AddOnIdentity addOnIdentity, WampMessageHandler wampMessageHandler) {
        super(wampMessageHandler);
        this.gson = gson;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        domainService.heartbeat(chargingStationId, addOnIdentity);

        HeartbeatResponse response = new HeartbeatResponse();
        response.setCurrentTime(new Date());

        writeResponse(webSocket, chargingStationId, response, callId, gson);
    }
}
