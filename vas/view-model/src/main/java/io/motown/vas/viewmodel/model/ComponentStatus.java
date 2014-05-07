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

/**
 * Component status that can translate a domain API Component status to a VAS status. The values in this
 * enum are translatable to the external VAS representation.
 */
public enum ComponentStatus {

    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    UNAVAILABLE("Unavailable"),
    UNKNOWN("Unknown");

    private final String value;

    ComponentStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ComponentStatus fromValue(String v) {
        for (ComponentStatus c: ComponentStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static ComponentStatus fromApiComponentStatus(io.motown.domain.api.chargingstation.ComponentStatus status) {
        ComponentStatus componentStatus;

        switch (status) {
            case AVAILABLE:
                componentStatus = AVAILABLE;
                break;
            case FAULTED:
                componentStatus = UNAVAILABLE;
                break;
            case INOPERATIVE:
                componentStatus = UNAVAILABLE;
                break;
            case OCCUPIED:
                componentStatus = OCCUPIED;
                break;
            case RESERVED:
                //Translating Reserved to Occupied is the closest match
                componentStatus = OCCUPIED;
                break;
            case UNKNOWN:
                componentStatus = UNKNOWN;
                break;
            default:
                componentStatus = ComponentStatus.UNKNOWN;
                break;
        }

        return componentStatus;
    }

}
