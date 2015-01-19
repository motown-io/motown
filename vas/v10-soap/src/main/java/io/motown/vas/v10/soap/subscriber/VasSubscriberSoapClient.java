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
import io.motown.vas.viewmodel.persistence.entities.Subscription;
import io.motown.vas.viewmodel.vas.SubscriberClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VasSubscriberSoapClient implements SubscriberClient {

    private static final Logger LOG = LoggerFactory.getLogger(VasSubscriberSoapClient.class);

    private String publisherIdentity;

    private VasSubscriberServiceProxyFactory proxyFactory;

    @Override
    public void pushStatusChange(Subscription subscription, io.motown.vas.viewmodel.model.StatusChange statusChange) {
        LOG.info("Pushing status change to subscription");

        StatusChangeNotification notification = new StatusChangeNotification();
        notification.setSubscriptionId(subscription.getSubscriptionId());

        StatusChange statusChangeWs = new StatusChange();
        statusChangeWs.setChargePoint(statusChange.getChargingStationId());
        statusChangeWs.setConnectorsFree(statusChange.getConnectorsFree());
        statusChangeWs.setStatus(ChargePointStatus.fromValue(statusChange.getStatus().value()));
        statusChangeWs.setTimestamp(statusChange.getTimestamp());

        List<StatusChange> statusChanges = notification.getStatusChanges();
        statusChanges.add(statusChangeWs);

        VasSubscriberService vasSubscriberService = proxyFactory.createVasSubscriberService(subscription.getDeliveryAddress());
        vasSubscriberService.statusChange(notification, publisherIdentity);
    }

    public void setProxyFactory(VasSubscriberServiceProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public void setPublisherIdentity(String publisherIdentity) {
        this.publisherIdentity = publisherIdentity;
    }
}
