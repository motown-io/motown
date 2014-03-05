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

import io.motown.ochp.v03.soap.schema.Echs;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.springframework.stereotype.Component;

import javax.xml.ws.BindingProvider;

@Component
public class OchpProxyFactory {

    /**
     * Creates a charging station web service proxy.
     *
     * @param eClearingServerAddress address of the e-ClearingHouse server.
     * @return charging station web service proxy
     */
    public Echs createOchpService(String eClearingServerAddress) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(Echs.class);

        factory.setAddress(eClearingServerAddress);

        SoapBindingConfiguration conf = new SoapBindingConfiguration();
        conf.setVersion(Soap12.getInstance());
        factory.setBindingConfig(conf);
        factory.getFeatures().add(new WSAddressingFeature());
        Echs eClearingService = (Echs) factory.create();

        //Force the use of the Async transport, even for synchronous calls
        ((BindingProvider) eClearingService).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);

        return eClearingService;
    }

}
