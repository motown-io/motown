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
import io.motown.vas.v10.soap.schema.StatusChangeNotification;
import io.motown.vas.v10.soap.schema.VasSubscriberService;
import io.motown.vas.viewmodel.model.ComponentStatus;
import io.motown.vas.viewmodel.model.StatusChange;
import io.motown.vas.viewmodel.persistence.entities.Subscription;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.FIVE_MINUTES_AGO;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VasSubscriberSoapClientTest {

    private static final int CONNECTORS_FREE = 2;

    private static final String PUBLISHER_IDENTITY = "MOTOWN";

    private static final String SUBSCRIPTION_IDENTITY = "identity";

    private static final String SUBSCRIPTION_DELIVERY_ADDRESS = "http://localhost";

    @Test
    public void pushStatusChangeVerifyServiceCall() {
        VasSubscriberServiceProxyFactory proxyFactory = mock(VasSubscriberServiceProxyFactory.class);
        VasSubscriberService vasSubscriberService = mock(VasSubscriberService.class);
        when(proxyFactory.createVasSubscriberService(SUBSCRIPTION_DELIVERY_ADDRESS)).thenReturn(vasSubscriberService);
        VasSubscriberSoapClient client = new VasSubscriberSoapClient();
        client.setProxyFactory(proxyFactory);
        client.setPublisherIdentity(PUBLISHER_IDENTITY);
        Subscription subscription = new Subscription(SUBSCRIPTION_IDENTITY, SUBSCRIPTION_DELIVERY_ADDRESS);
        StatusChange statusChange = new StatusChange(CHARGING_STATION_ID.getId(), FIVE_MINUTES_AGO, ComponentStatus.AVAILABLE, CONNECTORS_FREE);
        ArgumentCaptor<StatusChangeNotification> argumentCaptor = ArgumentCaptor.forClass(StatusChangeNotification.class);

        client.pushStatusChange(subscription, statusChange);

        verify(vasSubscriberService).statusChange(argumentCaptor.capture(), eq(PUBLISHER_IDENTITY));
        assertEquals(subscription.getSubscriptionId(), argumentCaptor.getValue().getSubscriptionId());
        io.motown.vas.v10.soap.schema.StatusChange serviceStatusChange = argumentCaptor.getValue().getStatusChanges().get(0);
        assertEquals(CHARGING_STATION_ID.getId(), serviceStatusChange.getChargePoint());
        assertEquals(CONNECTORS_FREE, serviceStatusChange.getConnectorsFree());
        assertEquals(FIVE_MINUTES_AGO, serviceStatusChange.getTimestamp());
        assertEquals(ChargePointStatus.AVAILABLE, serviceStatusChange.getStatus());
    }

}
