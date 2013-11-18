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
package io.motown.domain.api.chargingstation;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class ChargingStationBootedEvent {

    private final ChargingStationId chargingStationId;
    private final Map<String, String> attributes;

    public ChargingStationBootedEvent(ChargingStationId chargingStationId, Map<String, String> attributes) {
        this.chargingStationId = chargingStationId;
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    public Map<String, String> getAttributes() {
        return ImmutableMap.copyOf(attributes);
    }
}
