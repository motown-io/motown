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
package io.motown.domain.chargingstation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChargingStationTestUtils {
    public static ChargingStationId getChargingStationId() {
        return new ChargingStationId("CS-001");
    }

    public static List<Object> getCreatedChargingStation(boolean defaultAccepted) {
        if (defaultAccepted) {
            return ImmutableList.<Object>builder()
                    .add(new ChargingStationCreatedEvent(getChargingStationId()))
                    .add(new ChargingStationAcceptedEvent(getChargingStationId()))
                    .build();
        } else {
            return ImmutableList.<Object>builder()
                    .add(new ChargingStationCreatedEvent(getChargingStationId()))
                    .build();
        }
    }

    public static List<Object> getRegisteredChargingStation() {
        return ImmutableList.<Object>builder()
                .addAll(getCreatedChargingStation(true))
                .add(new ChargingStationAcceptedEvent(getChargingStationId()))
                .build();
    }

    public static List<Object> getConfiguredChargingStation(boolean defaultAccepted) {
        return ImmutableList.<Object>builder()
                .addAll(getCreatedChargingStation(defaultAccepted))
                .add(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), getConfigurationItems()))
                .build();
    }

    public static List<Object> getChargingStation() {
        return ImmutableList.<Object>builder()
                .addAll(getCreatedChargingStation(true))
                .add(new ChargingStationAcceptedEvent(getChargingStationId()))
                .add(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), getConfigurationItems()))
                .build();
    }

    public static Set<Connector> getConnectors() {
        return ImmutableSet.<Connector>builder()
                .add(new Connector(1, "TYPE-1", 32))
                .add(new Connector(2, "TYPE-1", 32))
                .build();
    }

    public static Map<String, String> getConfigurationItems() {
        return ImmutableMap.<String, String>builder()
                .put("io.motown.sockets.amount", "2")
                .put("io.motown.random.config.item", "true")
                .put("io.motown.another.random.config.item", "12")
                .put("io.motown.yet.another.one", "blue")
                .build();
    }

    public static Map<String, String> getAttributes() {
        return ImmutableMap.<String, String>builder()
                .put("vendor", "VENDOR001")
                .put("model", "MODEL001")
                .build();
    }
}
