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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.vas.v10.soap.VasSoapTestUtils.*;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:vas-soap-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class MotownVasPublisherServiceTest {

    private MotownVasPublisherService service;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private VasConversionService vasConversionService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setup() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.clear();
        deleteFromDatabase(entityManager, ChargingStation.class);
        deleteFromDatabase(entityManager, Subscription.class);

        service = new MotownVasPublisherService();

        service.setChargingStationRepository(chargingStationRepository);
        service.setSubscriptionRepository(subscriptionRepository);
        service.setVasConversionService(vasConversionService);
    }

    @Test
    public void subscribeAcceptedValidateResponse() {
        SubscribeRequest request = new SubscribeRequest();
        request.setDeliveryAddress(DELIVERY_ADDRESS);

        SubscribeResponse response = service.subscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.ACCEPTED, response.getStatus());
        assertNotNull(response.getSubscriptionId());
    }

    @Test
    public void subscribeDuplicateIgnoredValidateResponse() {
        SubscribeRequest request = new SubscribeRequest();
        request.setDeliveryAddress(DELIVERY_ADDRESS);
        service.subscribe(request, SUBSCRIBER_IDENTITY);

        SubscribeResponse response = service.subscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.DUPLICATE_IGNORED, response.getStatus());
        assertNull(response.getSubscriptionId());
    }

    @Test
    public void subscribeDuplicateIdentityValidateAcceptedResponse() {
        SubscribeRequest request = new SubscribeRequest();
        request.setDeliveryAddress(DELIVERY_ADDRESS);
        service.subscribe(request, SUBSCRIBER_IDENTITY);

        request.setDeliveryAddress(OTHER_DELIVERY_ADDRESS);
        SubscribeResponse response = service.subscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.ACCEPTED, response.getStatus());
    }

    @Test
    public void subscribeExceptionValidateRejectedResponse() {
        // force exception by setting subscriptionRepository to null
        service.setSubscriptionRepository(null);
        SubscribeRequest request = new SubscribeRequest();
        request.setDeliveryAddress(DELIVERY_ADDRESS);

        SubscribeResponse response = service.subscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.REJECTED, response.getStatus());
    }

    @Test
    public void subscribeValidateSavedSubscription() {
        SubscribeRequest request = new SubscribeRequest();
        request.setDeliveryAddress(DELIVERY_ADDRESS);

        service.subscribe(request, SUBSCRIBER_IDENTITY);

        assertNotNull(subscriptionRepository.findBySubscriberIdentityAndDeliveryAddress(SUBSCRIBER_IDENTITY, DELIVERY_ADDRESS));
    }

    @Test
    public void getChargePointInfoNoSubscriptionValidateEmptyResponse() {
        GetChargePointInfoRequest request = new GetChargePointInfoRequest();

        GetChargePointInfoResponse response = service.getChargePointInfo(request, SUBSCRIBER_IDENTITY);

        assertEquals(0, response.getChargePoints().size());
    }

    @Test
    public void getChargePointInfoSubscriptionNoChargePointsValidateEmptyResponse() {
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setDeliveryAddress(DELIVERY_ADDRESS);
        service.subscribe(subscribeRequest, SUBSCRIBER_IDENTITY);
        GetChargePointInfoRequest request = new GetChargePointInfoRequest();

        GetChargePointInfoResponse response = service.getChargePointInfo(request, SUBSCRIBER_IDENTITY);

        assertEquals(0, response.getChargePoints().size());
    }

    @Test
    public void getChargePointInfoSubscriptionValidateResponse() {
        subscribeTestSubscriber();
        chargingStationRepository.createOrUpdate(new ChargingStation(CHARGING_STATION_ID.getId()));
        chargingStationRepository.createOrUpdate(new ChargingStation("SECOND_CS"));
        GetChargePointInfoRequest request = new GetChargePointInfoRequest();

        GetChargePointInfoResponse response = service.getChargePointInfo(request, SUBSCRIBER_IDENTITY);

        assertEquals(2, response.getChargePoints().size());
    }

    @Test
    public void unsubscribeKnownSubscriptionValidateResponse() {
        String subscriptionId = subscribeTestSubscriber();
        UnsubscribeRequest request = new UnsubscribeRequest();
        request.setSubscriptionId(subscriptionId);

        UnsubscribeResponse response = service.unsubscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.ACCEPTED, response.getStatus());
    }

    @Test
    public void unsubscribeUnknownSubscriptionValidateResponse() {
        String unknownSubscriptionId = "unknown";
        UnsubscribeRequest request = new UnsubscribeRequest();
        request.setSubscriptionId(unknownSubscriptionId);

        UnsubscribeResponse response = service.unsubscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.REJECTED, response.getStatus());
    }

    @Test
    public void unsubscribeExceptionThrownValidateResponse() {
        String subscriptionId = subscribeTestSubscriber();
        UnsubscribeRequest request = new UnsubscribeRequest();
        request.setSubscriptionId(subscriptionId);
        // forcing exception
        service.setSubscriptionRepository(null);

        UnsubscribeResponse response = service.unsubscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.REJECTED, response.getStatus());
    }

    @Test
    public void unsubscribeKnownSubscriptionTwiceValidateResponse() {
        String subscriptionId = subscribeTestSubscriber();
        UnsubscribeRequest request = new UnsubscribeRequest();
        request.setSubscriptionId(subscriptionId);
        service.unsubscribe(request, SUBSCRIBER_IDENTITY);

        UnsubscribeResponse response = service.unsubscribe(request, SUBSCRIBER_IDENTITY);

        assertEquals(SubscribeStatus.REJECTED, response.getStatus());
    }


    @Test
    public void unsubscribeKnownSubscriptioValidateRepository() {
        String subscriptionId = subscribeTestSubscriber();
        UnsubscribeRequest request = new UnsubscribeRequest();
        request.setSubscriptionId(subscriptionId);

        service.unsubscribe(request, SUBSCRIBER_IDENTITY);

        assertNull(subscriptionRepository.findBySubscriberIdentityAndDeliveryAddress(SUBSCRIBER_IDENTITY, DELIVERY_ADDRESS));
    }

    private String subscribeTestSubscriber() {
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setDeliveryAddress(DELIVERY_ADDRESS);
        return service.subscribe(subscribeRequest, SUBSCRIBER_IDENTITY).getSubscriptionId();
    }
}
