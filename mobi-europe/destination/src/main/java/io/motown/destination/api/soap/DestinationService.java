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
package io.motown.destination.api.soap;

import io.motown.destination.soap.schema.*;

import javax.jws.WebService;

/**
 * Created by mmz on 16/01/15.
 */
@WebService(
        serviceName = "PmsServerService",
        portName = "PmsServerBinding",
        targetNamespace = "http://webservice.pms.mobieurope.com",
        endpointInterface = "io.motown.destination.soap.schema.PmsServerPortType",
        wsdlLocation = "wsdl/DestinationPMS.wsdl"
)
public class DestinationService implements PmsServerPortType {

    @Override
    public RequestStartTransactionResponse requestStartTransaction(RequestStartTransactionRequest parameters) {
        return null;
    }

    @Override
    public AuthorizeResponse authorize(AuthorizeRequest parameters) {
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        authorizeResponse.setAuthorizationIdentifier("MMz was here");
        return authorizeResponse;
    }

    @Override
    public GetLocalServicesListResponse getLocalServicesList(GetLocalServicesListRequest parameters) {
        return null;
    }

    @Override
    public RequestStopTransactionResponse requestStopTransaction(RequestStopTransactionRequest parameters) {
        return null;
    }

    @Override
    public GetLocalServiceStatusResponse getLocalServiceStatus(GetLocalServiceStatusRequest parameters) {
        return null;
    }
}
