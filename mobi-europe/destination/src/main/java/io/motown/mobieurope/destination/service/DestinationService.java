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
package io.motown.mobieurope.destination.service;

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.SimpleUserIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.domain.utils.axon.EventWaitingGateway;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.mobieurope.destination.entities.DestinationAuthorizeRequest;
import io.motown.mobieurope.destination.entities.DestinationRequestStartTransactionRequest;
import io.motown.mobieurope.destination.entities.DestinationRequestStopTransactionRequest;
import io.motown.mobieurope.destination.persistence.repository.DestinationSessionRepository;
import io.motown.mobieurope.destination.util.AuthorizationIdentifier;
import io.motown.mobieurope.shared.persistence.entities.SessionInfo;
import org.axonframework.domain.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestinationService {

    private static final Logger LOG = LoggerFactory.getLogger(DestinationService.class);

    private DestinationSessionRepository destinationSessionRepository;

    private EventWaitingGateway eventWaitingGateway;

    public void setDestinationSessionRepository(DestinationSessionRepository destinationSessionRepository) {
        this.destinationSessionRepository = destinationSessionRepository;
    }

    public void setEventWaitingGateway(EventWaitingGateway eventWaitingGateway) {
        this.eventWaitingGateway = eventWaitingGateway;
    }

    public String authorize(DestinationAuthorizeRequest destinationAuthorizeRequest) {
        // TODO: Check if all prerequisites within the infrastructure are met before generating an authorizationIdentifier
        String authorizationIdentifier = new AuthorizationIdentifier().getValue();
        createAndPersistSession(destinationAuthorizeRequest, authorizationIdentifier);
        return authorizationIdentifier;
    }

    public void requestStartTransaction(DestinationRequestStartTransactionRequest startTransactionRequest) {
        LOG.trace("requestStartTransaction called");
        final SessionInfo sessionInfo = destinationSessionRepository.findSessionInfoByAuthorizationId(startTransactionRequest.getAuthorizationIdentifier());
        sessionInfo.setRequestIdentifier(startTransactionRequest.getRequestIdentifier());
        sessionInfo.getSessionStateMachine().eventStartRequest();

        ChargingStationId chargingStationId = new ChargingStationId(sessionInfo.getLocalServiceIdentifier());
        IdentifyingToken identifyingToken = new TextualToken(sessionInfo.getAuthorizationIdentifier());
        EvseId evseId = new EvseId(Integer.parseInt(sessionInfo.getConnectorIdentifier()));

        IdentityContext identityContext = new IdentityContext(new TypeBasedAddOnIdentity("MobiEurope", "1"), new SimpleUserIdentity("root"));
        RequestStartTransactionCommand command = new RequestStartTransactionCommand(chargingStationId, identifyingToken, evseId, identityContext);

        FutureEventCallback<RequestStartTransactionResultEvent> futureEventCallback = new FutureEventCallback<RequestStartTransactionResultEvent>() {
            @Override
            public boolean onEvent(EventMessage<?> event) {
                if (event.getPayload() instanceof RequestStartTransactionAcceptedEvent) {
                    RequestStartTransactionAcceptedEvent requestStartTransactionAcceptedEvent = (RequestStartTransactionAcceptedEvent) event.getPayload();
                    LOG.info("RequestStartTransactionAcceptedEvent successfully received " + requestStartTransactionAcceptedEvent);
                    return true;
                }
                if (event.getPayload() instanceof RequestStartTransactionRejectedEvent) {
                    RequestStartTransactionRejectedEvent requestStartTransactionRejectedEvent = (RequestStartTransactionRejectedEvent) event.getPayload();
                    LOG.info("RequestStartTransactionRejectedEvent successfully received " + requestStartTransactionRejectedEvent);
                    return true;
                }
                return false;
            }
        };

        Long timeout = 10000L;

        try {
            eventWaitingGateway.sendAndWaitForEvent(command, futureEventCallback, timeout);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            destinationSessionRepository.insertOrUpdateSessionInfo(sessionInfo);
        }
    }

    public void requestStopTransaction(DestinationRequestStopTransactionRequest stopTransactionRequest) {
        LOG.trace("requestStopTransaction called");
        final SessionInfo sessionInfo = destinationSessionRepository.findSessionInfoByAuthorizationId(stopTransactionRequest.getAuthorizationIdentifier());
        sessionInfo.setRequestIdentifier(stopTransactionRequest.getRequestIdentifier());
        sessionInfo.getSessionStateMachine().eventStopRequest();

        ChargingStationId chargingStationId = new ChargingStationId(sessionInfo.getLocalServiceIdentifier());

        IdentityContext identityContext = new IdentityContext(new TypeBasedAddOnIdentity("MobiEurope", "1"), new SimpleUserIdentity("root"));

        TransactionId transactionId = new NumberedTransactionId(chargingStationId, "OCPP15", sessionInfo.getTransactionId());
        Object command = new RequestStopTransactionCommand(chargingStationId, transactionId, identityContext);

        FutureEventCallback<RequestStopTransactionResultEvent> futureEventCallback = new FutureEventCallback<RequestStopTransactionResultEvent>() {
            @Override
            public boolean onEvent(EventMessage<?> event) {
                if (event.getPayload() instanceof RequestStopTransactionAcceptedEvent) {
                    RequestStopTransactionAcceptedEvent requestStopTransactionAcceptedEvent = (RequestStopTransactionAcceptedEvent) event.getPayload();
                    LOG.info("RequestStopTransactionAcceptedEvent successfully received " + requestStopTransactionAcceptedEvent);
                    return true;
                }
                if (event.getPayload() instanceof RequestStopTransactionRejectedEvent) {
                    RequestStopTransactionRejectedEvent requestStopTransactionRejectedEvent = (RequestStopTransactionRejectedEvent) event.getPayload();
                    LOG.info("RequestStopTransactionRejectedEvent successfully received " + requestStopTransactionRejectedEvent);
                    return true;
                }
                return false;
            }
        };

        Long timeout = 10000L;

        try {
            eventWaitingGateway.sendAndWaitForEvent(command, futureEventCallback, timeout);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            destinationSessionRepository.insertOrUpdateSessionInfo(sessionInfo);
        }
    }

    private void createAndPersistSession(DestinationAuthorizeRequest destinationAuthorizeRequest, String authorizationId) {
        SessionInfo sessionInfo = new SessionInfo(new SessionStateMachineImpl());
        sessionInfo.setAuthorizationIdentifier(authorizationId);
        sessionInfo.setUserIdentifier(destinationAuthorizeRequest.getUserIdentifier());
        sessionInfo.setServiceTypeIdentifier(destinationAuthorizeRequest.getServiceTypeIdentifier());
        sessionInfo.setPmsIdentifier(destinationAuthorizeRequest.getPmsIdentifier());
        sessionInfo.setLocalServiceIdentifier(destinationAuthorizeRequest.getLocalServiceIdentifier());
        sessionInfo.setConnectorIdentifier(destinationAuthorizeRequest.getConnectorIdentifier());
        destinationSessionRepository.insertOrUpdateSessionInfo(sessionInfo);
    }

}
