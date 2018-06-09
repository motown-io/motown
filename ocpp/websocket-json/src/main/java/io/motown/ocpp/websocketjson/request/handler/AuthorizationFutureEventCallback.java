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

import io.motown.domain.api.chargingstation.AuthorizationResultEvent;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.ocpp.websocketjson.WebSocketWrapper;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;
import io.motown.ocpp.websocketjson.schema.generated.v15.AuthorizeResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.axonframework.domain.EventMessage;

public class AuthorizationFutureEventCallback extends FutureEventCallback<AuthorizeResponse> {

    private WebSocketWrapper webSocketWrapper;

    private String callId;

    AuthorizationFutureEventCallback(String callId, WebSocketWrapper webSocketWrapper) {
        this.webSocketWrapper = webSocketWrapper;
        this.callId = callId;
    }

    @Override
    public boolean onEvent(EventMessage<?> event) {
        AuthorizationResultEvent resultEvent;

        if (event.getPayload() instanceof AuthorizationResultEvent) {
            resultEvent = (AuthorizationResultEvent) event.getPayload();

            AuthorizeResponse response = new AuthorizeResponse();
            IdTagInfo idTagInfo = new IdTagInfo();
            idTagInfo.setStatus(IdTagInfo.Status.fromValue(resultEvent.getAuthenticationStatus().value()));
            response.setIdTagInfo(idTagInfo);

            this.setResult(response);

            this.countDownLatch();

            webSocketWrapper.sendResultMessage(new WampMessage(WampMessage.CALL_RESULT, callId, MessageProcUri.AUTHORIZE, response));

            return true;
        } else {
            // not the right type of event... not 'handled'
            return false;
        }
    }

}