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

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.websocketjson.WebSocketWrapper;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo__;
import io.motown.ocpp.websocketjson.schema.generated.v15.StarttransactionResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.axonframework.domain.EventMessage;

/**
 * Future event callback for start transaction. Part of the flow of handling a start transaction is authorizing the
 * identification that started the transaction. This class handles the {@code AuthorizationResult} and uses that
 * to construct the transaction.
 */
public class StartTransactionFutureEventCallback extends FutureEventCallback<AuthorizationResult> {

    private WebSocketWrapper webSocketWrapper;

    private String callId;

    private ChargingStationId chargingStationId;

    private String protocolIdentifier;

    private StartTransactionInfo startTransactionInfo;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public StartTransactionFutureEventCallback(String callId, WebSocketWrapper webSocketWrapper, ChargingStationId chargingStationId,
                                               String protocolIdentifier, StartTransactionInfo startTransactionInfo, DomainService domainService,
                                               AddOnIdentity addOnIdentity) {
        this.webSocketWrapper = webSocketWrapper;
        this.callId = callId;
        this.chargingStationId = chargingStationId;
        this.protocolIdentifier = protocolIdentifier;
        this.startTransactionInfo = startTransactionInfo;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
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
        IdentifyingToken identifyingToken = resultEvent.getIdentifyingToken();
        StartTransactionInfo extendedStartTransactionInfo = new StartTransactionInfo(startTransactionInfo.getEvseId(), startTransactionInfo.getMeterStart(), startTransactionInfo.getTimestamp(), identifyingToken, startTransactionInfo.getAttributes());
        
        domainService.startTransactionNoAuthorize(chargingStationId, transactionId, extendedStartTransactionInfo, addOnIdentity);

        IdTagInfo__ idTagInfo = new IdTagInfo__();
        idTagInfo.setStatus(convert(resultEvent.getAuthenticationStatus()));

        StarttransactionResponse response = new StarttransactionResponse();
        response.setTransactionId(transactionId.getNumber());
        response.setIdTagInfo(idTagInfo);

        writeResult(response);

        return true;
    }

    private void writeResult(StarttransactionResponse result) {
        webSocketWrapper.sendResultMessage(new WampMessage(WampMessage.CALL_RESULT, callId, MessageProcUri.START_TRANSACTION, result));
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
