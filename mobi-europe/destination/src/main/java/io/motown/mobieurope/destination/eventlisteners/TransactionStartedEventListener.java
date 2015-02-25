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
package io.motown.mobieurope.destination.eventlisteners;

import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.mobieurope.destination.entities.DestinationNotifyRequestResultRequest;
import io.motown.mobieurope.destination.persistence.entities.SourceEndpoint;
import io.motown.mobieurope.destination.persistence.repository.DestinationSessionRepository;
import io.motown.mobieurope.destination.persistence.repository.SourceEndpointRepository;
import io.motown.mobieurope.destination.service.SourceClient;
import io.motown.mobieurope.shared.persistence.entities.SessionInfo;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionStartedEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionStartedEventListener.class);

    private DestinationSessionRepository destinationSessionRepository;

    private SourceEndpointRepository sourceEndpointRepository;

    @EventHandler
    protected void onEvent(TransactionStartedEvent event) {
        LOG.info("Sending notifyRequestResult for a TransactionStartedEvent to the source PMS");
        String authorizationIdentifier = event.getStartTransactionInfo().getIdentifyingToken().getToken();
        Integer transactionId = ((NumberedTransactionId) event.getTransactionId()).getNumber();

        SessionInfo sessionInfo = destinationSessionRepository.findSessionInfoByAuthorizationId(authorizationIdentifier);
        sessionInfo.setTransactionId(transactionId);
        sessionInfo.getSessionStateMachine().eventStartOk();
        sessionInfo.setMeterStart(event.getStartTransactionInfo().getMeterStart());
        sessionInfo.setStartTimestamp(event.getStartTransactionInfo().getTimestamp());

        destinationSessionRepository.insertOrUpdateSessionInfo(sessionInfo);

        String requestIdentifier = sessionInfo.getRequestIdentifier();

        SourceEndpoint sourceEndpoint = sourceEndpointRepository.findSourceEndpointByPmsIdentifier(sessionInfo.getPmsIdentifier());
        SourceClient sourceClient = new SourceClient(sourceEndpoint.getSourceEndpointUrl());

        DestinationNotifyRequestResultRequest destinationNotifyRequestResultRequest = new DestinationNotifyRequestResultRequest(requestIdentifier);
        sourceClient.notifyRequestResult(destinationNotifyRequestResultRequest);
        LOG.info("Received a notifyRequestResultResponse from the source PMS");
    }


    public void setDestinationSessionRepository(DestinationSessionRepository destinationSessionRepository) {
        this.destinationSessionRepository = destinationSessionRepository;
    }

    public void setSourceEndpointRepository(SourceEndpointRepository sourceEndpointRepository) {
        this.sourceEndpointRepository = sourceEndpointRepository;
    }
}
