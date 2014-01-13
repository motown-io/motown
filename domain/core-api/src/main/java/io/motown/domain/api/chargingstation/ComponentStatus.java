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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The statuses of a charging station component.
 */
public enum ComponentStatus {
    /**
     * Unknown (e.g. not yet determined).
     */
    UNKNOWN("Unknown"),

    /**
     * Available for use, as none of the other states are applicable. This is the default, and non-active state.
     */
    AVAILABLE("Available"),

    /**
     * In use, physically or logically connected. This is an "active" state.
     */
    OCCUPIED("Occupied"),

    /**
     * Not available because a specific or aggregate reservation is in effect.
     */
    RESERVED("Reserved"),

    /**
     * Not available because it has been set to out-of-service.
     */
    INOPERATIVE("Inoperative"),

    /**
     * Unable to operate due to a fault condition.
     */
    FAULTED("Faulted");

    private String value;

    private ComponentStatus(String value) {
        this.value = value;
    }

    /**
     * Gets a {@code ComponentStatus} from a {@code String} value.
     *
     * @param value a {@code String} value representing one of the statuses.
     * @return the {@code ComponentStatus}.
     * @throws NullPointerException     if value is null.
     * @throws IllegalArgumentException if value is not one of the known statuses.
     */
    public static ComponentStatus fromValue(String value) {
        checkNotNull(value);

        if (value.equalsIgnoreCase(UNKNOWN.value)) {
            return UNKNOWN;
        } else if (value.equalsIgnoreCase(AVAILABLE.value)) {
            return AVAILABLE;
        } else if (value.equalsIgnoreCase(OCCUPIED.value)) {
            return OCCUPIED;
        } else if (value.equalsIgnoreCase(RESERVED.value)) {
            return RESERVED;
        } else if (value.equalsIgnoreCase(INOPERATIVE.value)) {
            return INOPERATIVE;
        } else if (value.equalsIgnoreCase(FAULTED.value)) {
            return FAULTED;
        } else {
            throw new IllegalArgumentException("Component status value must be one of the known statuses");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return value;
    }
}
