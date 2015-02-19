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

import io.motown.mobieurope.destination.soap.schema.*;
import io.motown.mobieurope.source.entities.*;

import javax.xml.ws.BindingProvider;

public class DestinationClient {

    PmsServerPortType destinationWebService;
    private String endpoint;

    public DestinationClient(String endpoint) {
        this.endpoint = endpoint;
    }

    public SourceAuthorizeResponse authorize(SourceAuthorizeRequest sourceAuthorizeRequest) {
        AuthorizeRequest authorizeRequest = sourceAuthorizeRequest.getAuthorizeRequest();
        AuthorizeResponse authorizeResponse = getDestinationWebService().authorize(authorizeRequest);

        return new SourceAuthorizeResponse(authorizeResponse);
    }

    public SourceRequestStartTransactionResponse requestStartTransaction(SourceRequestStartTransactionRequest sourceRequestStartTransactionRequest) {
        RequestStartTransactionRequest requestStartTransactionRequest = sourceRequestStartTransactionRequest.getRequestStartTransactionRequest();
        RequestStartTransactionResponse requestStartTransactionResponse = getDestinationWebService().requestStartTransaction(requestStartTransactionRequest);

        return new SourceRequestStartTransactionResponse(requestStartTransactionResponse);
    }

    public SourceRequestStopTransactionResponse requestStopTransaction(SourceRequestStopTransactionRequest sourceRequestStopTransactionRequest) {
        RequestStopTransactionRequest requestStopTransactionRequest = sourceRequestStopTransactionRequest.getRequestStopTransactionRequest();
        RequestStopTransactionResponse requestStopTransactionResponse = getDestinationWebService().requestStopTransaction(requestStopTransactionRequest);

        return new SourceRequestStopTransactionResponse(requestStopTransactionResponse);
    }

    public SourceGetLocalServicesListResponse getLocalServicesList(SourceGetLocalServicesListRequest sourceGetLocalServicesListRequest) {
        GetLocalServicesListRequest getLocalServicesListRequest = sourceGetLocalServicesListRequest.getGetLocalServicesListRequest();
        GetLocalServicesListResponse getLocalServicesListResponse = getDestinationWebService().getLocalServicesList(getLocalServicesListRequest);

        return new SourceGetLocalServicesListResponse(getLocalServicesListResponse);
    }

    public SourceGetLocalServiceStatusResponse getLocalServicesList(SourceGetLocalServiceStatusRequest sourceGetLocalServiceStatusRequest) {
        GetLocalServiceStatusRequest getLocalServiceStatusRequest = sourceGetLocalServiceStatusRequest.getGetLocalServiceStatusRequest();
        GetLocalServiceStatusResponse getLocalServiceStatusResponse = getDestinationWebService().getLocalServiceStatus(getLocalServiceStatusRequest);

        return new SourceGetLocalServiceStatusResponse(getLocalServiceStatusResponse);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;

        if (destinationWebService != null) {
            ((BindingProvider) destinationWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpoint);
        }
    }

    private PmsServerPortType getDestinationWebService() {
        if (destinationWebService == null) {
            PmsServerPortType destinationWebService = new PmsServerService().getPmsServerBinding();
            ((BindingProvider) destinationWebService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
            this.destinationWebService = destinationWebService;
        }
        return destinationWebService;
    }

    public void setDestinationWebService(PmsServerPortType destinationWebService) {
        this.destinationWebService = destinationWebService;
    }

}
