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
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageHandler;
import org.atmosphere.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    public abstract void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket websocket);

    protected WampMessageHandler wampMessageHandler;

    public RequestHandler(WampMessageHandler wampMessageHandler) {
        this.wampMessageHandler = wampMessageHandler;
    }

    protected void writeResponse(WebSocket webSocket, ChargingStationId chargingStationId, Object response, String callId, Gson gson) {
        if (response != null) {
            String wampMessageRaw = new WampMessage(WampMessage.CALL_RESULT, callId, response).toJson(gson);

            if (wampMessageRaw != null) {
                try {
                    webSocket.write(wampMessageRaw);
                    if (this.wampMessageHandler != null) {
                        this.wampMessageHandler.handleWampCallResult(chargingStationId.getId(), wampMessageRaw, callId);
                    }
                } catch (IOException e) {
                    LOG.error("IOException while writing to web socket.", e);
                }
            }
        }
    }

}
