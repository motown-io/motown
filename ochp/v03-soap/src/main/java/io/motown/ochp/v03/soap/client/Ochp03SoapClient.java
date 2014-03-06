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

import io.motown.ochp.v03.soap.schema.*;
import io.motown.ochp.viewmodel.ochp.Ochp03Client;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Ochp03SoapClient implements Ochp03Client {

    private static final Logger LOG = LoggerFactory.getLogger(Ochp03SoapClient.class);

    private static final int ACCEPTED = 0;

    @Autowired
    private OchpProxyFactory ochpProxyFactory;

    @Value("${io.motown.ochp.server.address}")
    private String serverAddress;

    @Value("${io.motown.ochp.server.address}")
    private String username;

    @Value("${io.motown.ochp.server.address}")
    private String password;

    /** In memory cache to store the authentication token that is required in all webservice calls */
    private String cachedAuthenticationToken;

    /**
     * Performs authentication in case we do not have an authentication token
     */
    private void forceAuthentication() {
        if(cachedAuthenticationToken == null){
            LOG.info("Not authenticated yet, performing OCHP authenticate");

            Echs ochpClientService = this.createOchpClientService();

            AuthenticateRequest request = new AuthenticateRequest();
            request.setUserId(username);
            request.setPassword(password);
            AuthenticateResponse response = ochpClientService.authenticate(new AuthenticateRequest());

            if(response.getResultCode() != ACCEPTED) {
                LOG.error("Authentication of {} failed", username);
            } else {
                LOG.info("Authentication was successfull");
                cachedAuthenticationToken = response.getAuthToken();
            }
        }
    }

    @Override
    public void addChargeDetailRecords(List<Transaction> transactionList) {
        LOG.info("OCHP addCDRs");
        forceAuthentication();

        AddCDRsRequest request = new AddCDRsRequest();
        List<CDRInfo> cdrInfoList = request.getCdrInfoArray();
        for(Transaction transaction : transactionList){
            CDRInfo cdrInfo = new CDRInfo();
            cdrInfo.setEvseId(transaction.getEvseId());
            //TODO: Convert the rest of the transaction/chargingstation information into CDRInfo - Ingo Pak, 06 Mar 2014

            cdrInfoList.add(cdrInfo);
        }

        AddCDRsResponse response = this.createOchpClientService().addCDRs(request, cachedAuthenticationToken);

        if(response.getResult() == null || response.getResult().getResultCode() != ACCEPTED) {
            LOG.error("Failed to add the CDR's");
        }
    }

    @Override
    public List getChargePointList() {
        LOG.info("OCHP getChargePointList");
        forceAuthentication();

        Echs ochpClientService = this.createOchpClientService();

        GetChargepointListResponse response = ochpClientService.getChargepointList(new GetChargepointListRequest(), cachedAuthenticationToken);

        return response.getChargepointInfoArray();
    }

    public void setOchpProxyFactory(OchpProxyFactory ochpProxyFactory) {
        this.ochpProxyFactory = ochpProxyFactory;
    }

    private Echs createOchpClientService() {
        return ochpProxyFactory.createOchpService(this.serverAddress);
    }

}
