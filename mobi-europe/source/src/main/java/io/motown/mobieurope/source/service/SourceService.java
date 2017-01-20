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
package io.motown.mobieurope.source.service;

import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.identificationauthorization.app.IdentificationAuthorizationService;
import io.motown.mobieurope.destination.soap.schema.ResponseError;
import io.motown.mobieurope.shared.persistence.entities.SessionInfo;
import io.motown.mobieurope.source.entities.*;
import io.motown.mobieurope.source.persistence.entities.DestinationEndpoint;
import io.motown.mobieurope.source.persistence.repository.DestinationEndpointRepository;
import io.motown.mobieurope.source.persistence.repository.SourceSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceService.class);

    private IdentificationAuthorizationService identificationAuthorizationService;

    private DestinationEndpointRepository destinationEndpointRepository;

    private SourceSessionRepository sourceSessionRepository;

    public void setIdentificationAuthorizationService(IdentificationAuthorizationService identificationAuthorizationService) {
        this.identificationAuthorizationService = identificationAuthorizationService;
    }

    public void setDestinationEndpointRepository(DestinationEndpointRepository destinationEndpointRepository) {
        this.destinationEndpointRepository = destinationEndpointRepository;
    }

    public void setSourceSessionRepository(SourceSessionRepository sourceSessionRepository) {
        this.sourceSessionRepository = sourceSessionRepository;
    }

    public SourceAuthorizeResponse authorize(SourceAuthorizeRequest sourceAuthorizeRequest) {
        LOG.trace("Authorize called");
        SourceAuthorizeResponse sourceAuthorizeResponse;

        if (identificationAuthorizationService.validate(new TextualToken(sourceAuthorizeRequest.getUserIdentifier())).isValid()) {
            DestinationEndpoint destinationEndpoint = destinationEndpointRepository.findDestinationEndpointByPmsIdentifier(sourceAuthorizeRequest.getServicePms());
            if (destinationEndpoint != null) {
                DestinationClient destinationClient = new DestinationClient(destinationEndpoint.getDestinationEndpointUrl());
                sourceAuthorizeResponse = destinationClient.authorize(sourceAuthorizeRequest);
                createSessionInfo(sourceAuthorizeRequest, sourceAuthorizeResponse);
            } else {
                ResponseError responseError = new ResponseError();
                responseError.setErrorMsg("PMS Unknown: " + sourceAuthorizeRequest.getServicePms());
                responseError.setErrorCode("400");
                sourceAuthorizeResponse = new SourceAuthorizeResponse(responseError);
            }
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorMsg("Invalid userIdentifier");
            responseError.setErrorCode("400");
            sourceAuthorizeResponse = new SourceAuthorizeResponse(responseError);
        }
        return sourceAuthorizeResponse;
    }

    public SourceRequestStartTransactionResponse requestStartTransaction(SourceRequestStartTransactionRequest sourceRequestStartTransactionRequest) {
        LOG.trace("requestStartTransaction called");
        SourceRequestStartTransactionResponse sourceRequestStartTransactionResponse;

        SessionInfo sessionInfo = sourceSessionRepository.findSessionInfoByAuthorizationId(sourceRequestStartTransactionRequest.getAuthorizationIdentifier());

        DestinationEndpoint destinationEndpoint = destinationEndpointRepository.findDestinationEndpointByPmsIdentifier(sessionInfo.getServicePms());
        DestinationClient destinationClient = new DestinationClient(destinationEndpoint.getDestinationEndpointUrl());
        sourceRequestStartTransactionResponse = destinationClient.requestStartTransaction(sourceRequestStartTransactionRequest);

        sessionInfo.setRequestIdentifier(sourceRequestStartTransactionRequest.getRequestIdentifier());
        sessionInfo.getSessionStateMachine().eventStartRequest();
        sourceSessionRepository.insertOrUpdateSessionInfo(sessionInfo);

        return sourceRequestStartTransactionResponse;
    }

    public SourceRequestStopTransactionResponse requestStopTransaction(SourceRequestStopTransactionRequest sourceRequestStopTransactionRequest) {
        LOG.trace("requestStopTransaction called");
        SourceRequestStopTransactionResponse sourceRequestStopTransactionResponse;

        SessionInfo sessionInfo = sourceSessionRepository.findSessionInfoByAuthorizationId(sourceRequestStopTransactionRequest.getAuthorizationIdentifier());

        DestinationEndpoint destinationEndpoint = destinationEndpointRepository.findDestinationEndpointByPmsIdentifier(sessionInfo.getServicePms());
        DestinationClient destinationClient = new DestinationClient(destinationEndpoint.getDestinationEndpointUrl());
        sourceRequestStopTransactionResponse = destinationClient.requestStopTransaction(sourceRequestStopTransactionRequest);

        sessionInfo.setRequestIdentifier(sourceRequestStopTransactionRequest.getRequestIdentifier());
        sessionInfo.getSessionStateMachine().eventStopRequest();
        sourceSessionRepository.insertOrUpdateSessionInfo(sessionInfo);

        return sourceRequestStopTransactionResponse;
    }

    private void createSessionInfo(SourceAuthorizeRequest sourceAuthorizeRequest, SourceAuthorizeResponse sourceAuthorizeResponse) {
        SessionInfo sessionInfo = new SessionInfo(new SessionStateMachineImpl());
        sessionInfo.setAuthorizationIdentifier(sourceAuthorizeResponse.getAuthorizationIdentifier());
        sessionInfo.setLocalServiceIdentifier(sourceAuthorizeRequest.getLocalServiceIdentifier());
        sessionInfo.setConnectorIdentifier(sourceAuthorizeRequest.getConnectorIdentifier());
        sessionInfo.setServicePms(sourceAuthorizeRequest.getServicePms());
        sessionInfo.setUserIdentifier(sourceAuthorizeRequest.getUserIdentifier());
        sessionInfo.setServiceTypeIdentifier(sourceAuthorizeRequest.getServiceTypeIdentifier());
        sessionInfo.setPmsIdentifier(sourceAuthorizeRequest.getPmsIdentifier());
        sourceSessionRepository.insertOrUpdateSessionInfo(sessionInfo);
    }
}
