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
package io.motown.vas.v10.soap.publisher;

import io.motown.vas.v10.soap.VasConversionService;
import io.motown.vas.v10.soap.schema.*;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;
import io.motown.vas.viewmodel.persistence.entities.Subscription;
import io.motown.vas.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.vas.viewmodel.persistence.repostories.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.jws.WebParam;
import java.util.List;

@javax.jws.WebService(
        serviceName = "VasPublisherService",
        portName = "VasPublisherServiceSoap12",
        targetNamespace = "urn://Vas/Cs/2010/12/",
        wsdlLocation = "WEB-INF/wsdl/VasPublisherService.wsdl",
        endpointInterface = "io.motown.vas.v10.soap.schema.VasPublisherService")
public class VasPublisherService implements io.motown.vas.v10.soap.schema.VasPublisherService {

    private static final Logger LOG = LoggerFactory.getLogger(VasPublisherService.class);

    /**
     * Timeout in milliseconds for the continuation suspend functionality
     */
    @Value("${io.motown.vas.v10.soap.cxf.continuation.timeout}")
    private int CONTINUATION_TIMEOUT;

    /**
     * Heartbeat interval which will be returned to the client if the handling failed
     */
    @Value("${io.motown.vas.v10.soap.heartbeat.interval.fallback}")
    private int HEARTBEAT_INTERVAL_FALLBACK;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private VasConversionService vasConversionService;

    @Override
    public SubscribeResponse subscribe(@WebParam(partName = "parameters", name = "subscribeRequest", targetNamespace = "urn://Vas/Cs/2010/12/") SubscribeRequest parameters, @WebParam(partName = "SubscriberIdentity", name = "subscriberIdentity", targetNamespace = "urn://Vas/Cs/2010/12/", header = true) String subscriberIdentity) {
        LOG.info("Subscribing {}", subscriberIdentity);

        SubscribeResponse subscribeResponse = new SubscribeResponse();
        try {
            String deliveryAddress = parameters.getDeliveryAddress();

            if (subscriptionRepository.findBySubscriberIdentityAndDeliveryAddress(subscriberIdentity, deliveryAddress) == null) {
                Subscription subscription = new Subscription(subscriberIdentity, deliveryAddress);
                subscriptionRepository.saveAndFlush(subscription);

                subscribeResponse.setSubscriptionId(subscription.getSubscriptionId());
                subscribeResponse.setStatus(SubscribeStatus.ACCEPTED);
            } else {
                subscribeResponse.setStatus(SubscribeStatus.DUPLICATE_IGNORED);
            }
        } catch (Exception e) {
            LOG.error("Subscription failed", e);
            subscribeResponse.setStatus(SubscribeStatus.REJECTED);
        }

        return subscribeResponse;
    }

    @Override
    public GetChargePointInfoResponse getChargePointInfo(@WebParam(partName = "parameters", name = "getChargePointInfoRequest", targetNamespace = "urn://Vas/Cs/2010/12/") GetChargePointInfoRequest parameters, @WebParam(partName = "SubscriberIdentity", name = "subscriberIdentity", targetNamespace = "urn://Vas/Cs/2010/12/", header = true) String subscriberIdentity) {
        LOG.info("GetChargePointInfo {}", subscriberIdentity);

        //TODO: Right now we return info on all chargingstations known in the system (might have to exclude certain chargingstation 'pools') - Ingo Pak, 24 Jan 2014
        GetChargePointInfoResponse response = new GetChargePointInfoResponse();
        if (subscriptionRepository.findBySubscriberIdentity(subscriberIdentity).size() > 0) {
            List<ChargePoint> chargePoints = response.getChargePoints();
            for (ChargingStation chargingStation : chargingStationRepository.findAll()) {
                chargePoints.add(vasConversionService.getVasRepresentation(chargingStation));
            }
        }
        return response;
    }

    @Override
    public UnsubscribeResponse unsubscribe(@WebParam(partName = "parameters", name = "unsubscribeRequest", targetNamespace = "urn://Vas/Cs/2010/12/") UnsubscribeRequest parameters, @WebParam(partName = "SubscriberIdentity", name = "subscriberIdentity", targetNamespace = "urn://Vas/Cs/2010/12/", header = true) String subscriberIdentity) {
        LOG.info("Unsubscribing {}", subscriberIdentity);

        UnsubscribeResponse unsubscribeResponse = new UnsubscribeResponse();
        try {
            Subscription subscription = subscriptionRepository.findBySubscriptionId(parameters.getSubscriptionId());

            if (subscription != null) {
                subscriptionRepository.delete(subscription);
                unsubscribeResponse.setStatus(SubscribeStatus.ACCEPTED);
            } else {
                unsubscribeResponse.setStatus(SubscribeStatus.DUPLICATE_IGNORED);
            }
        } catch (Exception e) {
            LOG.error("Unsubscription of {} failed", subscriberIdentity, e);
            unsubscribeResponse.setStatus(SubscribeStatus.REJECTED);
        }

        return unsubscribeResponse;
    }
}
