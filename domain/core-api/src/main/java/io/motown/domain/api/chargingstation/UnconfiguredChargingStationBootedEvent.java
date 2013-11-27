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

import java.util.Map;

/**
 * {@code UnconfiguredChargingStationBootedEvent} is the event which is published when an unconfigured charging station
 * has booted.
 */
public final class UnconfiguredChargingStationBootedEvent extends ChargingStationBootedEvent {
    /**
     * Creates a {@code UnconfiguredChargingStationBootedEvent} with an identifier and a {@link java.util.Map} of
     * attributes.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information
     *                          provided by the charging station when it booted but which are not required by Motown.
     *                          Because {@link java.util.Map} implementations are potentially mutable a defensive copy
     *                          is made.
     * @throws NullPointerException if {@code chargingStationId} or {@code attributes} is {@code null}.
     */
    public UnconfiguredChargingStationBootedEvent(ChargingStationId chargingStationId, Map<String, String> attributes) {
        super(chargingStationId, attributes);
    }
}
