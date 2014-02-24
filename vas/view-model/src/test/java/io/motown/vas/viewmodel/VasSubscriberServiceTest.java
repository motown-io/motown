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
package io.motown.vas.viewmodel;

import io.motown.vas.viewmodel.model.*;
import io.motown.vas.viewmodel.persistence.repostories.SubscriptionRepository;
import io.motown.vas.viewmodel.vas.SubscriptionUpdater;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.vas.viewmodel.VasViewModelTestUtils.SUBSCRIPTIONS;
import static io.motown.vas.viewmodel.VasViewModelTestUtils.getRegisteredAndConfiguredChargingStation;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class VasSubscriberServiceTest {

    private VasSubscriberService subscribeService;

    private SubscriptionRepository subscriptionRepository;

    private ExecutorService executorService;

    @Before
    public void setUp() {
        subscriptionRepository = mock(SubscriptionRepository.class);
        executorService = mock(ExecutorService.class);

        subscribeService = new VasSubscriberService();
        subscribeService.setSubscriptionRepository(subscriptionRepository);
        subscribeService.setExecutorService(executorService);
    }

    @Test
    public void updateSubscribersWithoutSubscribers() {
        ChargingStation chargingStation = getRegisteredAndConfiguredChargingStation();

        subscribeService.updateSubscribers(chargingStation, FIVE_MINUTES_AGO);

        verify(executorService, never()).execute(any(SubscriptionUpdater.class));
    }

    @Test
    public void updateSubscribersVerifyExecutorServiceCalls() {
        ChargingStation chargingStation = getRegisteredAndConfiguredChargingStation();
        when(subscriptionRepository.findAll()).thenReturn(SUBSCRIPTIONS);

        subscribeService.updateSubscribers(chargingStation, FIVE_MINUTES_AGO);

        verify(executorService, times(SUBSCRIPTIONS.size())).execute(any(SubscriptionUpdater.class));
    }

}
