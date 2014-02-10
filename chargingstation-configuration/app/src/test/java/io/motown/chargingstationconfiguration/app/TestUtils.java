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
package io.motown.chargingstationconfiguration.app;

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.*;

import java.util.*;

public class TestUtils {

    public static ChargingStationId getChargingStationId() {
        return new ChargingStationId("CS-001");
    }

    public static String getProtocol() {
        return "PROTOCOL";
    }

    public static Map<String, String> getAttributesMap(String vendor, String model) {
        return ImmutableMap.<String, String>builder()
                .put("vendor", vendor)
                .put("model", model)
                .build();
    }

    public static Set<Evse> getEvses(int numberOfEvses) {
        Set<Evse> evses = new HashSet<>();
        for(int i = 0; i < numberOfEvses; i++) {
            evses.add(getEvse(i));
        }
        return evses;
    }

    public static Evse getEvse(int identifier) {
        return new Evse(new EvseId(identifier), getConnectors(3));
    }

    public static List<Connector> getConnectors(int numberOfConnectors) {
        List<Connector> connectors = new ArrayList<>(numberOfConnectors);
        for (int i = 0; i < numberOfConnectors; i++) {
            connectors.add(getConnector());
        }
        return connectors;
    }

    public static Connector getConnector() {
        return new Connector(32, 3, 230, ChargingProtocol.MODE3, Current.AC, ConnectorType.TESLA);
    }

}
