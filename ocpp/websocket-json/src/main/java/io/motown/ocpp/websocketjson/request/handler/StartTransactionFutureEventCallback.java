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
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo__;
import io.motown.ocpp.websocketjson.schema.generated.v15.StarttransactionResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageHandler;
import org.atmosphere.websocket.WebSocket;
import org.axonframework.domain.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Future event callback for start transaction. Part of the flow of handling a start transaction is authorizing the
 * identification that started the transaction. This class handles the {@code AuthorizationResult} and uses that
 * to construct the transaction.
 */
public class StartTransactionFutureEventCallback extends FutureEventCallback<AuthorizationResult> {

    private static final Logger LOG = LoggerFactory.getLogger(StartTransactionFutureEventCallback.class);

    private WebSocket webSocket;

    private String callId;

    private Gson gson;

    private ChargingStationId chargingStationId;

    private String protocolIdentifier;

    private StartTransactionInfo startTransactionInfo;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    private WampMessageHandler wampMessageHandler;

    public StartTransactionFutureEventCallback(String callId, WebSocket webSocket, Gson gson, ChargingStationId chargingStationId,
                                               String protocolIdentifier, StartTransactionInfo startTransactionInfo, DomainService domainService,
                                               AddOnIdentity addOnIdentity, WampMessageHandler wampMessageHandler) {
        this.webSocket = webSocket;
        this.callId = callId;
        this.gson = gson;
        this.chargingStationId = chargingStationId;
        this.protocolIdentifier = protocolIdentifier;
        this.startTransactionInfo = startTransactionInfo;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
        this.wampMessageHandler = wampMessageHandler;
    }

    /**
     * Handles the {@code AuthorizationResultEvent} (other events will directly result in 'false' return value). In the
     * flow of handling a start transaction message the first step is to authorize the identification used to start
     * the transaction. This method handles that event and uses the domain service to start the transaction and write
     * a response on the web socket.
     *
     * @param event Authorizaton result event.
     * @return true if the event has been handled, false if the event was the wrong type.
     */
    @Override
    public boolean onEvent(EventMessage<?> event) {
        AuthorizationResultEvent resultEvent;

        if (!(event.getPayload() instanceof AuthorizationResultEvent)) {
            // not the right type of event... not 'handled'
            return false;
        }

        resultEvent = (AuthorizationResultEvent) event.getPayload();

        Transaction transaction = domainService.createTransaction(startTransactionInfo.getEvseId());
        NumberedTransactionId transactionId = new NumberedTransactionId(chargingStationId, protocolIdentifier, transaction.getId().intValue());

        domainService.startTransactionNoAuthorize(chargingStationId, transactionId, startTransactionInfo, addOnIdentity);

        IdTagInfo__ idTagInfo = new IdTagInfo__();
        idTagInfo.setStatus(convert(resultEvent.getAuthenticationStatus()));

        StarttransactionResponse response = new StarttransactionResponse();
        response.setTransactionId(transactionId.getNumber());
        response.setIdTagInfo(idTagInfo);

        writeResult(response);

        return true;
    }

    private void writeResult(StarttransactionResponse result) {
        try {
            String wampMessageRaw = new WampMessage(WampMessage.CALL_RESULT, callId, result).toJson(gson);
            webSocket.write(wampMessageRaw);
            if (this.wampMessageHandler != null) {
                this.wampMessageHandler.handleWampCallResult(this.chargingStationId.getId(), wampMessageRaw);
            }
        } catch (IOException e) {
            LOG.error("IOException while writing to web socket.", e);
        }
    }

    /**
     * Converts a {@code AuthorizationResultStatus} to a {@code IdTagInfo__.Status}. Throws an assertion error is the
     * status is unknown.
     *
     * @param status status to convert.
     * @return converted status.
     */
    private static IdTagInfo__.Status convert(AuthorizationResultStatus status) {
        IdTagInfo__.Status result;

        switch (status) {
            case ACCEPTED:
                result = IdTagInfo__.Status.ACCEPTED;
                break;
            case BLOCKED:
                result = IdTagInfo__.Status.BLOCKED;
                break;
            case EXPIRED:
                result = IdTagInfo__.Status.EXPIRED;
                break;
            case INVALID:
                result = IdTagInfo__.Status.INVALID;
                break;
            default:
                throw new AssertionError("AuthorizationResultStatus has unknown status: " + status);
        }

        return result;
    }
}
