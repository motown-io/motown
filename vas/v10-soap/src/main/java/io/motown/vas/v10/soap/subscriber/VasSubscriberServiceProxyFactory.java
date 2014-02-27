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
package io.motown.vas.v10.soap.subscriber;

import io.motown.vas.v10.soap.schema.VasSubscriberService;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

import javax.xml.ws.BindingProvider;

public class VasSubscriberServiceProxyFactory {

    /**
     * Creates a vas subscriber web service proxy based on the delivery address.
     *
     * @param deliveryAddress delivery address
     * @return subscriber web service proxy
     */
    public VasSubscriberService createVasSubscriberService(String deliveryAddress) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(VasSubscriberService.class);

        factory.setAddress(deliveryAddress);

        SoapBindingConfiguration conf = new SoapBindingConfiguration();
        conf.setVersion(Soap12.getInstance());
        factory.setBindingConfig(conf);
        factory.getFeatures().add(new WSAddressingFeature());
        VasSubscriberService vasSubscriberService = (VasSubscriberService) factory.create();

        //Force the use of the Async transport, even for synchronous calls
        ((BindingProvider) vasSubscriberService).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);

        return vasSubscriberService;
    }

}
