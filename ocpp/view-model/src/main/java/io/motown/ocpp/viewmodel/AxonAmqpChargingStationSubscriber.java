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
package io.motown.ocpp.viewmodel;

import io.motown.domain.api.chargingstation.ChargingStationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component @Qualifier("axonAmqpChargingStationSubscriber")
public class AxonAmqpChargingStationSubscriber implements ChargingStationSubscriber {
    @Autowired
    private ChargingStationConfigurer amqpConfigurer;
    @Resource(name = "clusterChargingStationSubscriber")
    private ChargingStationSubscriber chargingStationSubscriber;
    @Resource(name = "boundQueueChargingStationConfigurer")
    private ChargingStationConfigurer chargingStationConfigurer;

    @Override
    public void subscribe(ChargingStationId chargingStationId) {
        chargingStationConfigurer.configure(chargingStationId);
        chargingStationSubscriber.subscribe(chargingStationId);
    }

    public ChargingStationConfigurer getAmqpConfigurer() {
        return amqpConfigurer;
    }

    public void setAmqpConfigurer(ChargingStationConfigurer amqpConfigurer) {
        this.amqpConfigurer = amqpConfigurer;
    }

    public ChargingStationSubscriber getChargingStationSubscriber() {
        return chargingStationSubscriber;
    }

    public void setChargingStationSubscriber(ChargingStationSubscriber chargingStationSubscriber) {
        this.chargingStationSubscriber = chargingStationSubscriber;
    }

    public ChargingStationConfigurer getChargingStationConfigurer() {
        return chargingStationConfigurer;
    }

    public void setChargingStationConfigurer(ChargingStationConfigurer chargingStationConfigurer) {
        this.chargingStationConfigurer = chargingStationConfigurer;
    }
}
