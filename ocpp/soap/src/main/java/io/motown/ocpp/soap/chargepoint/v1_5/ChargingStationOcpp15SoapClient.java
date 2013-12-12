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

package io.motown.ocpp.soap.chargepoint.v1_5;

import com.google.common.collect.Maps;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.soap.chargepoint.v1_5.schema.*;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class ChargingStationOcpp15SoapClient implements ChargingStationOcpp15Client {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationOcpp15SoapClient.class);

    @Autowired
    private DomainService domainService;

    public void getConfiguration(ChargingStationId id) {
        log.info("Retrieving configuration");

        ChargePointService chargePointService = this.createChargePointService(id);

        //TODO: Make the chargepoint client calls async - Ingo Pak, 27 nov 2013
        GetConfigurationResponse response =  chargePointService.getConfiguration(new GetConfigurationRequest(), id.getId());

        HashMap<String, String> configurationItems = Maps.newHashMap();
        for (KeyValue keyValue : response.getConfigurationKey()){
            configurationItems.put(keyValue.getKey(), keyValue.getValue());
        }

        domainService.configureChargingStation(id, configurationItems);
    }

    public void stopTransaction(ChargingStationId id, int transactionId) {
        log.info("Stopping transaction");

        ChargePointService chargePointService = this.createChargePointService(id);

        //TODO: Make the chargepoint client calls async - Ingo Pak, 2 dec 2013
        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionId);
        RemoteStopTransactionResponse response =  chargePointService.remoteStopTransaction(request, id.getId());

        log.info("Stop transaction request has been {}", response.getStatus().value());
    }

    protected ChargePointService createChargePointService(ChargingStationId id){
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ChargePointService.class);

        factory.setAddress(domainService.retrieveChargingStationAddress(id));

        SoapBindingConfiguration conf = new SoapBindingConfiguration();
        conf.setVersion(Soap12.getInstance());
        factory.setBindingConfig(conf);
        factory.getFeatures().add(new WSAddressingFeature());

        return (ChargePointService) factory.create();
    }

}
