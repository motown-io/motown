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

import io.motown.vas.viewmodel.model.StatusChange;
import io.motown.vas.viewmodel.model.Subscription;
import io.motown.vas.viewmodel.persistence.repostories.SubscriptionRepository;
import io.motown.vas.viewmodel.vas.SubscriberClient;
import io.motown.vas.viewmodel.vas.SubscriptionUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class VasSubscriberService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private SubscriberClient subscriberClient;

    public void updateSubscribers(StatusChange change) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            executorService.execute(new SubscriptionUpdater(subscriberClient, subscription, change));
        }
    }

    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
