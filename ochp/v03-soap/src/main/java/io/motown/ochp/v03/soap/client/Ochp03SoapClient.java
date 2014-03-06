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
import io.motown.ochp.viewmodel.ochp.Ochp03Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Ochp03SoapClient implements Ochp03Client {

    private static final Logger LOG = LoggerFactory.getLogger(Ochp03SoapClient.class);

    @Autowired
    private OchpProxyFactory ochpProxyFactory;

    @Override
    public String authenticate() {
        LOG.info("OCHP authenticate");

        Echs ochpClientService = this.createOchpClientService();

        //TODO: finish implementation - Ingo Pak, 03 Mar 2014
        AuthenticateResponse response = ochpClientService.authenticate(new AuthenticateRequest());

        return "fakeToken";
    }

    public void setOchpProxyFactory(OchpProxyFactory ochpProxyFactory) {
        this.ochpProxyFactory = ochpProxyFactory;
    }

    private Echs createOchpClientService() {

        //TODO: Retrieve the clearinghouse address from configuration - Ingo Pak, 03 Mar 2014
        String clearingHouseAddress = "http://localhost:8090/mockechsSOAP";
        return ochpProxyFactory.createOchpService(clearingHouseAddress);
    }

}
