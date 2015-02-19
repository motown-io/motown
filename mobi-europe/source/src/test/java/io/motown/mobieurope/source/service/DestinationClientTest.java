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

import io.motown.mobieurope.destination.soap.schema.AuthorizeRequest;
import io.motown.mobieurope.destination.soap.schema.AuthorizeResponse;
import io.motown.mobieurope.destination.soap.schema.PmsServerPortType;
import io.motown.mobieurope.destination.soap.schema.ResponseError;
import io.motown.mobieurope.shared.enums.ServiceTypeIdentifier;
import io.motown.mobieurope.source.entities.SourceAuthorizeRequest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class DestinationClientTest {

    private DestinationClient destinationClient;

    private PmsServerPortType pmsServerPortTypeSuccess;

    private PmsServerPortType pmsServerPortTypeError;

    @Before
    public void setup() {
        destinationClient = new DestinationClient("");
        pmsServerPortTypeSuccess = mock(PmsServerPortType.class);
        pmsServerPortTypeError = mock(PmsServerPortType.class);

        when(pmsServerPortTypeSuccess.authorize(any(AuthorizeRequest.class))).thenReturn(getAuthorizeSuccess());
        when(pmsServerPortTypeError.authorize(any(AuthorizeRequest.class))).thenReturn(getAuthorizeError());
    }

    @Test
    public void shouldCallPmsServerPortTypeOnce() {
//        destinationClient.setDestinationWebService(pmsServerPortType);
//
//        destinationClient.authorize(getClientAuthorizeRequest());
//        verify(pmsServerPortType, times(1)).authorize(any(AuthorizeRequest.class));
    }

    @Test
    public void testSuccess() {

//        Map<String, Object> result = destinationClient.authorize(getClientAuthorizeRequest());

    }

    @Test
    public void testFail() {
//        Map<String, Object> result = destinationClient.authorize(getClientAuthorizeRequest());

    }

    private AuthorizeResponse getAuthorizeSuccess() {
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        authorizeResponse.setAuthorizationIdentifier("authorizeIdentifier");
        return authorizeResponse;
    }

    private AuthorizeResponse getAuthorizeError() {
        AuthorizeResponse authorizeResponse = new AuthorizeResponse();
        ResponseError responseError = new ResponseError();
        responseError.setErrorCode("100");
        responseError.setErrorMsg("Error");
        authorizeResponse.setResponseError(responseError);
        return authorizeResponse;
    }

    private SourceAuthorizeRequest getClientAuthorizeRequest() {
        SourceAuthorizeRequest sourceAuthorizeRequest = new SourceAuthorizeRequest();
        sourceAuthorizeRequest.setPmsIdentifier("NL-TST");
        sourceAuthorizeRequest.setServicePms("NL-TST");
        sourceAuthorizeRequest.setUserIdentifier("UUID-TST");
        sourceAuthorizeRequest.setLocalServiceIdentifier("TST-CPT");
        sourceAuthorizeRequest.setConnectorIdentifier("1");
        sourceAuthorizeRequest.setServiceTypeIdentifier(ServiceTypeIdentifier.EV_CHARGING);
        return sourceAuthorizeRequest;
    }
}
