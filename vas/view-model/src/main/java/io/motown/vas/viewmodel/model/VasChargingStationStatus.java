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

import io.motown.domain.api.chargingstation.ComponentStatus;

public enum VasChargingStationStatus {

    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    UNAVAILABLE("Unavailable"),
    UNKNOWN("Unknown");

    private final String value;

    VasChargingStationStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VasChargingStationStatus fromValue(String v) {
        for (VasChargingStationStatus c: VasChargingStationStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static VasChargingStationStatus fromComponentStatus(ComponentStatus status) {
        VasChargingStationStatus vasChargingStationStatus;

        switch (status) {
            case AVAILABLE:
                vasChargingStationStatus = AVAILABLE;
                break;
            case FAULTED:
                vasChargingStationStatus = UNAVAILABLE;
                break;
            case INOPERATIVE:
                vasChargingStationStatus = UNAVAILABLE;
                break;
            case OCCUPIED:
                vasChargingStationStatus = OCCUPIED;
                break;
            case RESERVED:
                //TODO is this the right status for VAS? Probably there is no car charging yet... - Mark van den Bergh, Februari 13th 2014
                vasChargingStationStatus = OCCUPIED;
                break;
            case UNKNOWN:
                vasChargingStationStatus = UNKNOWN;
                break;
            default:
                vasChargingStationStatus = VasChargingStationStatus.UNKNOWN;
        }

        return vasChargingStationStatus;
    }

}
