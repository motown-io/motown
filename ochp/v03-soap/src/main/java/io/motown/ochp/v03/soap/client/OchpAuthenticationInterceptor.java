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
package io.motown.ochp.v03.soap.client;

import io.motown.ochp.v03.soap.schema.AuthenticateRequest;
import io.motown.ochp.v03.soap.schema.AuthenticateResponse;
import io.motown.ochp.v03.soap.schema.Echs;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;

/**
 * Interceptor that adds the UserIdentityHeader as a soap header. The userIdentityHeader contains the authenticationtoken that
 * is obtained by calling the authenticate webservice method.
 * The authenticationtoken is cached in memory, when it is not present yet an authentication call is made.
 */
@Component
public class OchpAuthenticationInterceptor extends AbstractSoapInterceptor {

    private static final int ACCEPTED = 0;
    private static final int DENIED = 1;

    @Autowired
    private OchpProxyFactory ochpProxyFactory;

    @Value("${io.motown.ochp.server.address}")
    private String serverAddress;

    @Value("${io.motown.ochp.server.address}")
    private String username;

    @Value("${io.motown.ochp.server.address}")
    private String password;

    private String cachedAuthenticationToken;

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        if(cachedAuthenticationToken == null) {
            Echs ochpClientService = ochpProxyFactory.createOchpService(serverAddress, OchpProxyFactory.NO_AUTHENTICATION);

            AuthenticateRequest authenticateRequest = new AuthenticateRequest();
            authenticateRequest.setUserId(username);
            authenticateRequest.setPassword(password);

            AuthenticateResponse response = ochpClientService.authenticate(authenticateRequest);

            if(response.getResultCode() == ACCEPTED){
                //Store the received authentication token
                cachedAuthenticationToken = response.getAuthToken();
            } else {
                throw new Fault(new Exception("Authentication failed " + response.getResultDescription()));
            }
        }

        Header userIdentityHeader = message.getHeader(new QName("UserIdentityHeader"));
        userIdentityHeader.setObject(cachedAuthenticationToken);
    }
}
