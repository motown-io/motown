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
package io.motown.source.service;

import io.motown.destination.soap.schema.*;
import io.motown.source.enums.ServiceTypeIdentifier;

import javax.xml.ws.BindingProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mmz on 16/01/15.
 */
public class DestinationClient {

    private String endpoint;

    DestinationClient(String endpoint) {
        this.endpoint = endpoint;
    }

    PmsServerPortType getDestination() {
        PmsServerPortType destination = new PmsServerService().getPmsServerBinding();
        ((BindingProvider) destination).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpoint);
        return destination;
    }

    Map<String, Object> authorize(String pmsIdentifier, String userIdentifier, ServiceTypeIdentifier serviceTypeIdentifier, String localServiceIdentifier, String connectorIdentifier) {
        Map<String, Object> result = new HashMap<>();

        PmsServerPortType endpoint = getDestination();

        AuthorizeRequest authorizeRequest = new AuthorizeRequest();
        authorizeRequest.setPmsIdentifier(pmsIdentifier);
        authorizeRequest.setUserIdentifier(userIdentifier);
        authorizeRequest.setServiceTypeIdentifier(ServiceType.fromValue(serviceTypeIdentifier.value()));
        authorizeRequest.setLocalServiceIdentifier(localServiceIdentifier);
        authorizeRequest.setConnectorIdentifier(connectorIdentifier);

        AuthorizeResponse authorizeResponse = endpoint.authorize(authorizeRequest);

        ResponseError responseError = authorizeResponse.getResponseError();

        if (responseError == null) {
            result.put("authorizationIdentifier", authorizeResponse.getAuthorizationIdentifier());
        } else {
            result.put("error", authorizeResponse.getResponseError());
        }

        return result;
    }
}
