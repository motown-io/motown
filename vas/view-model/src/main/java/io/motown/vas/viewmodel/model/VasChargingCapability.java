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

import io.motown.domain.api.chargingstation.Connector;

/**
 * Charging Capability as per VAS spec
 */
public enum VasChargingCapability {

    UNSPECIFIED("Unspecified"),
    BATTERY_EXCHANGE("BatteryExchange"),
    V_120V_1_PHASE_10A("120V1Phase10A"),
    V_120V_1_PHASE_12A("120V1Phase12A"),
    V_120V_1_PHASE_16A("120V1Phase16A"),
    V_240V_1_PHASE_10A("240V1Phase10A"),
    V_240V_1_PHASE_12A("240V1Phase12A"),
    V_240V_1_PHASE_16A("240V1Phase16A"),
    V_240V_1_PHASE_32A("240V1Phase32A"),
    V_240V_3_PHASE_16A("240V3Phase16A"),
    V_240V_3_PHASE_32A("240V3Phase32A"),
    V_480V_3_PHASE_16A("480V3Phase16A"),
    V_480V_3_PHASE_32A("480V3Phase32A"),
    V_480V_3_PHASE_63A("480V3Phase63A"),
    DC_FAST_CHARGING("DcFastCharging");

    private final String value;

    private VasChargingCapability(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VasChargingCapability fromValue(String v) {
        for (VasChargingCapability c: values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    /**
     * Tries to map the information from the connector to a charging capability. Returns UNSPECIFIED if no mapping
     * can be made.
     * Note: BatteryExchange and DcFastCharging are not supported at the moment.
     *
     * @param connector connector with charging capability information.
     * @return charging capability if a mapping has been made, otherwise UNSPECIFIED.
     */
    public static VasChargingCapability fromConnector(Connector connector) {
        VasChargingCapability chargingCapability = UNSPECIFIED;

        try {
            chargingCapability = VasChargingCapability.fromValue(String.format("%dV%dPhase%dA", connector.getVoltage(), connector.getPhase(), connector.getMaxAmp()));
        } catch (IllegalArgumentException ex) {
            // cannot convert to valid VAS charging capability.
            //TODO log?
        }

        return chargingCapability;
    }

}