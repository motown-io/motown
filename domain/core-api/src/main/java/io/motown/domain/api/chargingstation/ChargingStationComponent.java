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

/**
 * The components of a charging station.
 */
public enum ChargingStationComponent {
    /**
     * The connector of a charging station.
     * <p/>
     * A connector is an independently operated and managed electrical outlet on an EVSE. This corresponds to a single
     * physical outlet.
     */
    CONNECTOR("Connector"),

    /**
     * The EVSE of a charging station.
     * <p/>
     * EVSE stands for Electrical Vehicle Supply Equipment. It is the logical unit in a Charge Point that supplies
     * electric energy via a connector for recharging. An EVSE can have one or multiple connector(s).
     */
    EVSE("EVSE");

    private String value;

    /**
     * Creates a {@code ChargingStationComponent} with the textual representation of this value.
     *
     * @param value the textual representation of this value.
     */
    private ChargingStationComponent(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return value;
    }
}
