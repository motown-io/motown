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
 * {@code ComponentStatus} holds the different statuses a charging station component can reside in.
 */
public enum ComponentStatus {
    UNKNOWN("Unknown"),
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    RESERVED("Reserved"),
    INOPERATIVE("Inoperative"),
    FAULTED("Faulted"),
    UNAVAILABLE("Unavailable");

    private String value;

    private ComponentStatus(String value) {
        this.value = value;
    }

    public boolean equalsValue(String otherComponentValue){
        return (otherComponentValue == null)? false: value.equals(otherComponentValue);
    }

    public static ComponentStatus fromValue(String value) {
        if(AVAILABLE.equalsValue(value)) {
            return AVAILABLE;
        } else if(OCCUPIED.equalsValue(value)) {
            return OCCUPIED;
        } else if(RESERVED.equalsValue(value)) {
            return RESERVED;
        } else if(INOPERATIVE.equalsValue(value)) {
            return INOPERATIVE;
        } else if(FAULTED.equalsValue(value)) {
            return FAULTED;
        } else if(UNAVAILABLE.equalsValue(value)) {
            return UNAVAILABLE;
        }

        return UNKNOWN;
    }

    @Override
    public String toString() {
        return value;
    }
}
