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
package io.motown.vas.viewmodel.persistence.repostories;

import io.motown.vas.viewmodel.persistence.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

    public Subscription findById(Long id);

    public Subscription findBySubscriptionId(String subscriptionId);

    public Subscription findBySubscriberIdentityAndDeliveryAddress(String subscriberIdentity, String deliveryAddress);

    public List<Subscription> findBySubscriberIdentity(String subscriberIdentity);

}
