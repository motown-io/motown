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
package io.motown.ocpp.v12.soap.chargepoint;

import io.motown.ocpp.v12.soap.chargepoint.schema.ChargePointService;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.springframework.stereotype.Component;

import javax.xml.ws.BindingProvider;

@Component
public class ChargingStationProxyFactory {

    /**
     * Creates a charging station web service proxy.
     *
     * @param chargingStationAddress address of the charging station.
     * @return charging station web service proxy
     */
    public ChargePointService createChargingStationService(String chargingStationAddress) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ChargePointService.class);

        factory.setAddress(chargingStationAddress);

        SoapBindingConfiguration conf = new SoapBindingConfiguration();
        conf.setVersion(Soap12.getInstance());
        factory.setBindingConfig(conf);
        factory.getFeatures().add(new WSAddressingFeature());
        ChargePointService chargePointService = (ChargePointService) factory.create();

        //Force the use of the Async transport, even for synchronous calls
        ((BindingProvider) chargePointService).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);

        return chargePointService;
    }

}
