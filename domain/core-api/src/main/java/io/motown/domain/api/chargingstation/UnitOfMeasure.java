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
 * Unit of measure of a {@code MeterValue}.
 */
public enum UnitOfMeasure {
    /**
     * Watt-hours (energy).
     */
    WATT_HOUR,
    /**
     * kiloWatt-hours (energy).
     */
    KILOWATT_HOUR,
    /**
     * Var-hours (reactive energy).
     */
    VAR_HOUR,
    /**
     * kilovar-hours (reactive energy).
     */
    KILOVAR_HOUR,
    /**
     * Watts (power).
     */
    WATT,
    /**
     * kilowatts (power).
     */
    KILOWATT,
    /**
     * Vars (reactive power).
     */
    VAR,
    /**
     * kilovars (reactive power).
     */
    KILOVAR,
    /**
     * Amperes (current).
     */
    AMPERES,
    /**
     * Voltage (r.m.s. AC).
     */
    VOLTAGE,
    /**
     * Degrees (temperature).
     */
    CELSIUS
}
