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
package io.motown.ocpp.viewmodel.domain;

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.ChargingStationId;

import java.util.Map;
import java.util.UUID;

public class DomainServiceTestUtils {

    public static ChargingStationId getChargingStationId() {
        return new ChargingStationId("CS-001");
    }

    public static String getRandomString() {
        return UUID.randomUUID().toString();
    }

    public static String getIdTag() {
        return "ID-TAG";
    }

    public static String getChargingStationAddress() {
        return "127.0.0.1";
    }

    public static String getVendor() {
        return "Mowotn";
    }

    public static String getModel() {
        return "ChargingStation";
    }

    public static Map<String, String> getConfigurationItems() {
        return ImmutableMap.<String, String>builder()
                .put("io.motown.sockets.amount", "2")
                .put("io.motown.random.config.item", "true")
                .put("io.motown.another.random.config.item", "12")
                .put("io.motown.yet.another.one", "blue")
                .build();
    }


}
