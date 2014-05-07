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
package io.motown.vas.viewmodel.vas;

import io.motown.vas.viewmodel.model.ComponentStatus;
import io.motown.vas.viewmodel.model.StatusChange;
import io.motown.vas.viewmodel.persistence.entities.Subscription;
import org.junit.Test;

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SubscriptionUpdaterTest {

    private static final String SUBSCRIPTION_IDENTITY = "identity";

    private static final String SUBSCRIPTION_DELIVERY_ADDRESS = "http://localhost";

    @Test
    public void runUpdaterVerifyClientCall() {
        SubscriberClient client = mock(SubscriberClient.class);
        Subscription subscription = new Subscription(SUBSCRIPTION_IDENTITY, SUBSCRIPTION_DELIVERY_ADDRESS);
        StatusChange statusChange = new StatusChange(CHARGING_STATION_ID.getId(), new Date(), ComponentStatus.AVAILABLE, 2);
        SubscriptionUpdater subscriptionUpdater = new SubscriptionUpdater(client, subscription, statusChange);

        subscriptionUpdater.run();

        verify(client).pushStatusChange(subscription, statusChange);
    }

}
