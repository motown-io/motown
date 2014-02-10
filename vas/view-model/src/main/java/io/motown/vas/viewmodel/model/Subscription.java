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
package io.motown.vas.viewmodel.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"subscriberIdentity", "deliveryAddress"}))
public class Subscription {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subscriberIdentity;

    private String subscriptionId;

    private String deliveryAddress;

    private Subscription(){
        //Private no-arg constructor for Hibernate
    }

    public Subscription(String subscriberIdentity, String deliveryAddress) {
        this.subscriberIdentity = subscriberIdentity;
        this.subscriptionId = UUID.randomUUID().toString();
        this.deliveryAddress = deliveryAddress;
    }

    Long getId() {
        return id;
    }

    public String getSubscriberIdentity() {
        return subscriberIdentity;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

}
