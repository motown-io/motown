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

import io.motown.vas.v10.soap.schema.ChargePointStatus;
import io.motown.vas.v10.soap.schema.StatusChange;
import io.motown.vas.v10.soap.schema.StatusChangeNotification;
import io.motown.vas.v10.soap.schema.VasSubscriberService;
import io.motown.vas.viewmodel.model.Subscription;
import io.motown.vas.viewmodel.persistence.repostories.SubscriptionRepository;
import io.motown.vas.viewmodel.vas.Vas10Client;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.ws.BindingProvider;
import java.util.Date;
import java.util.List;

@Component
public class VasSubscriberSoapClient implements Vas10Client {

    private static final Logger LOG = LoggerFactory.getLogger(VasSubscriberSoapClient.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Value("${io.motown.vas.v10.soap.publisher.identity}")
    private String publisherIdentity;

    @Override
    public void pushStatusChange() {
        LOG.info("Pushing status change to listeners");

        List<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
           subscription.getDeliveryAddress();

            StatusChangeNotification notification = new StatusChangeNotification();
            notification.setSubscriptionId(subscription.getSubscriptionId());
            List<StatusChange> statusChanges = notification.getStatusChanges();

            //TODO: The storage of statuschanges is not implemented yet - Ingo Pak, 20 Jan 2014
            //TODO: Retrieve the status changes, and add them to the statusChanges list - Ingo Pak, 20 Jan 2014
            StatusChange statusChange = new StatusChange();
            statusChange.setChargePoint("CS-001");
            statusChange.setConnectorsFree(2);
            statusChange.setStatus(ChargePointStatus.AVAILABLE);
            statusChange.setTimestamp(new Date());
            statusChanges.add(statusChange);

            VasSubscriberService vasSubscriberService = this.createVasSubscriberService(subscription);
            vasSubscriberService.statusChange(notification, publisherIdentity);
        }
    }

    /**
     * Creates a vas subscriber web service proxy based on the address that has been stored for this charging station identifier.
     *
     * @param subscription subscriber
     * @return subscriber web service proxy
     */
    protected VasSubscriberService createVasSubscriberService(Subscription subscription) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(VasSubscriberService.class);

        factory.setAddress(subscription.getDeliveryAddress());

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
