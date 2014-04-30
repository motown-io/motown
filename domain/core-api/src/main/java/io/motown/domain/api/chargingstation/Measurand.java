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
 * The measurand of a {@code MeterValue}.
 */
public enum Measurand {
    /**
     * Energy exported by EV (Wh or kWh).
     */
    EXPORTED_ACTIVE_ENERGY_REGISTER,
    /**
     * Energy imported by EV (Wh or kWh).
     */
    IMPORTED_ACTIVE_ENERGY_REGISTER,
    /**
     * Reactive energy exported by EV (varh or kvarh).
     */
    EXPORTED_REACTIVE_ENERGY_REGISTER,
    /**
     * Reactive energy imported by EV (varh or kvarh).
     */
    IMPORTED_REACTIVE_ENERGY_REGISTER,
    /**
     * Energy exported by EV (Wh or kWh).
     */
    EXPORTED_ACTIVE_ENERGY_INTERVAL,
    /**
     * Energy imported by EV (Wh or kWh).
     */
    IMPORTED_ACTIVE_ENERGY_INTERVAL,
    /**
     * Reactive energy exported by EV (varh or kvarh).
     */
    EXPORTED_REACTIVE_ENERGY_INTERVAL,
    /**
     * Reactive energy imported by EV (varh or kvarh).
     */
    IMPORTED_REACTIVE_ENERGY_INTERVAL,
    /**
     * Instantaneous active power exported by EV (W or kW).
     */
    EXPORTED_ACTIVE_POWER,
    /**
     * Instantaneous active power imported by EV (W or kW).
     */
    IMPORTED_ACTIVE_POWER,
    /**
     * Instantaneous reactive power exported by EV (var or kvar).
     */
    EXPORTED_REACTIVE_POWER,
    /**
     * Instantaneous reactive power imported by EV (var or kvar).
     */
    IMPORTED_REACTIVE_POWER,
    /**
     * Instantaneous current flow from EV.
     */
    EXPORTED_CURRENT,
    /**
     * Instantaneous current flow to EV.
     */
    IMPORTED_CURRENT,
    /**
     * AC RMS supply voltage.
     */
    VOLTAGE,
    /**
     * Temperature reading inside charge point.
     */
    TEMPERATURE
}
