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
package io.motown.ocpp.v15.soap.centralsystem;

import io.motown.domain.api.chargingstation.Measurand;

import javax.annotation.Nullable;

/**
 * Adapter which translates a {@code io.motown.ocpp.v15.soap.centralsystem.schema.Measurand} to a {@code Measurand}.
 */
class MeasurandTranslator implements Translator<Measurand> {

    private final io.motown.ocpp.v15.soap.centralsystem.schema.Measurand measurand;

    /**
     * Creates a {@code MeasurandTranslationAdapter}.
     *
     * @param measurand the measurand to translate.
     */
    public MeasurandTranslator(@Nullable io.motown.ocpp.v15.soap.centralsystem.schema.Measurand measurand) {
        this.measurand = measurand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurand translate() {
        if (this.measurand == null) {
            // In OCPP 1.5, IMPORTED_ACTIVE_ENERGY_REGISTER is the default value.
            return Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER;
        }

        switch (this.measurand) {
            case ENERGY_ACTIVE_EXPORT_REGISTER:
                return Measurand.EXPORTED_ACTIVE_ENERGY_REGISTER;
            case ENERGY_ACTIVE_IMPORT_REGISTER:
                return Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER;
            case ENERGY_REACTIVE_EXPORT_REGISTER:
                return Measurand.EXPORTED_REACTIVE_ENERGY_REGISTER;
            case ENERGY_REACTIVE_IMPORT_REGISTER:
                return Measurand.IMPORTED_REACTIVE_ENERGY_REGISTER;
            case ENERGY_ACTIVE_EXPORT_INTERVAL:
                return Measurand.EXPORTED_ACTIVE_ENERGY_INTERVAL;
            case ENERGY_ACTIVE_IMPORT_INTERVAL:
                return Measurand.IMPORTED_ACTIVE_ENERGY_INTERVAL;
            case ENERGY_REACTIVE_EXPORT_INTERVAL:
                return Measurand.EXPORTED_REACTIVE_ENERGY_INTERVAL;
            case ENERGY_REACTIVE_IMPORT_INTERVAL:
                return Measurand.IMPORTED_REACTIVE_ENERGY_INTERVAL;
            case POWER_ACTIVE_EXPORT:
                return Measurand.EXPORTED_ACTIVE_POWER;
            case POWER_ACTIVE_IMPORT:
                return Measurand.IMPORTED_ACTIVE_POWER;
            case POWER_REACTIVE_EXPORT:
                return Measurand.EXPORTED_REACTIVE_POWER;
            case POWER_REACTIVE_IMPORT:
                return Measurand.IMPORTED_REACTIVE_POWER;
            case CURRENT_EXPORT:
                return Measurand.EXPORTED_CURRENT;
            case CURRENT_IMPORT:
                return Measurand.IMPORTED_CURRENT;
            case VOLTAGE:
                return Measurand.VOLTAGE;
            case TEMPERATURE:
                return Measurand.TEMPERATURE;
            default:
                throw new AssertionError(String.format("Unknown value for Measurand: '%s'", measurand));
        }
    }

}
