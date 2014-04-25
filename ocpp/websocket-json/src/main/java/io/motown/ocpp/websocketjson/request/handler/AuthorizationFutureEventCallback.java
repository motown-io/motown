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
import io.motown.domain.api.chargingstation.AuthorizationResultEvent;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.FutureEventCallback;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.atmosphere.websocket.WebSocket;
import org.axonframework.domain.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthorizationFutureEventCallback extends FutureEventCallback<AuthorizationResult> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFutureEventCallback.class);

    private WebSocket webSocket;

    private String callId;

    private Gson gson;

    public AuthorizationFutureEventCallback(String callId, WebSocket webSocket, Gson gson) {
        this.webSocket = webSocket;
        this.callId = callId;
        this.gson = gson;
    }

    @Override
    public boolean onEvent(EventMessage<?> event) {
        AuthorizationResultEvent resultEvent;

        if (event.getPayload() instanceof AuthorizationResultEvent) {
            resultEvent = (AuthorizationResultEvent) event.getPayload();

            AuthorizationResult result = new AuthorizationResult(resultEvent.getIdentifyingToken().getToken(), resultEvent.getAuthenticationStatus());

            this.setResult(result);

            this.countDownLatch();

            this.writeResult(result);

            return true;
        } else {
            // not the right type of event... not 'handled'
            return false;
        }
    }

    private void writeResult(AuthorizationResult result) {
        try {
            webSocket.write(new WampMessage(WampMessage.CALL_RESULT, callId, result).toJson(gson));
        } catch (IOException e) {
            LOG.error("IOException while writing to web socket.", e);
        }
    }
}
