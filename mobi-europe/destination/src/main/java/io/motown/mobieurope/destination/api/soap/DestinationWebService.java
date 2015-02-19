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
package io.motown.mobieurope.destination.api.soap;

import io.motown.mobieurope.destination.entities.DestinationAuthorizeRequest;
import io.motown.mobieurope.destination.entities.DestinationRequestStartTransactionRequest;
import io.motown.mobieurope.destination.entities.DestinationRequestStopTransactionRequest;
import io.motown.mobieurope.destination.service.DestinationService;
import io.motown.mobieurope.destination.soap.schema.*;

import javax.jws.WebService;

@WebService(
        serviceName = "PmsServerService",
        portName = "PmsServerBinding",
        targetNamespace = "http://webservice.pms.mobieurope.com",
        endpointInterface = "io.motown.mobieurope.destination.soap.schema.PmsServerPortType",
        wsdlLocation = "wsdl/DestinationPMS.wsdl"
)
public class DestinationWebService implements PmsServerPortType {

    private DestinationService destinationService;

    public void setDestinationService(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @Override
    public AuthorizeResponse authorize(AuthorizeRequest authorizeRequest) {
        DestinationAuthorizeRequest destinationAuthorizeRequest = new DestinationAuthorizeRequest(authorizeRequest);
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        if (destinationAuthorizeRequest.isValid()) {
            String authorizationId = destinationService.authorize(destinationAuthorizeRequest);
            authorizeResponse.setAuthorizationIdentifier(authorizationId);
            return authorizeResponse;
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode("400");
            responseError.setErrorMsg("Unable to validate authorize request");
            authorizeResponse.setResponseError(responseError);
            return authorizeResponse;
        }
    }

    @Override
    public RequestStartTransactionResponse requestStartTransaction(RequestStartTransactionRequest requestStartTransactionRequest) {
        DestinationRequestStartTransactionRequest destinationRequestStartTransactionRequest = new DestinationRequestStartTransactionRequest(requestStartTransactionRequest);
        RequestStartTransactionResponse requestStartTransactionResponse = new RequestStartTransactionResponse();
        if (destinationRequestStartTransactionRequest.isValid()) {
            destinationService.requestStartTransaction(destinationRequestStartTransactionRequest);
            return requestStartTransactionResponse;
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode("400");
            responseError.setErrorMsg("Unable to validate request start transaction request");
            requestStartTransactionResponse.setResponseError(responseError);
            return requestStartTransactionResponse;
        }
    }

    @Override
    public RequestStopTransactionResponse requestStopTransaction(RequestStopTransactionRequest requestStopTransactionRequest) {
        DestinationRequestStopTransactionRequest destinationRequestStopTransactionRequest = new DestinationRequestStopTransactionRequest(requestStopTransactionRequest);
        RequestStopTransactionResponse requestStopTransactionResponse = new RequestStopTransactionResponse();

        if (destinationRequestStopTransactionRequest.isValid()) {
            destinationService.requestStopTransaction(destinationRequestStopTransactionRequest);
            return requestStopTransactionResponse;
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode("400");
            responseError.setErrorMsg("Unable to validate request start transaction request");
            requestStopTransactionResponse.setResponseError(responseError);
            return requestStopTransactionResponse;
        }
    }

    @Override
    public GetLocalServiceStatusResponse getLocalServiceStatus(GetLocalServiceStatusRequest parameters) {
        ResponseError responseError = new ResponseError();
        responseError.setErrorMsg("Not implemented yet");
        responseError.setErrorCode("500");

        GetLocalServiceStatusResponse getLocalServiceStatusResponse = new GetLocalServiceStatusResponse();
        getLocalServiceStatusResponse.setResponseError(responseError);
        return getLocalServiceStatusResponse;
    }

    @Override
    public GetLocalServicesListResponse getLocalServicesList(GetLocalServicesListRequest parameters) {
        ResponseError responseError = new ResponseError();
        responseError.setErrorMsg("Not implemented yet");
        responseError.setErrorCode("500");

        GetLocalServicesListResponse getLocalServicesListResponse = new GetLocalServicesListResponse();
        getLocalServicesListResponse.setResponseError(responseError);
        return getLocalServicesListResponse;
    }

}
