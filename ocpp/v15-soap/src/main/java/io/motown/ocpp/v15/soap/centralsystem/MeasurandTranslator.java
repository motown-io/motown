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

        Measurand result;

        switch (this.measurand) {
            case ENERGY_ACTIVE_EXPORT_REGISTER:
                result = Measurand.EXPORTED_ACTIVE_ENERGY_REGISTER;
                break;
            case ENERGY_ACTIVE_IMPORT_REGISTER:
                result = Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER;
                break;
            case ENERGY_REACTIVE_EXPORT_REGISTER:
                result = Measurand.EXPORTED_REACTIVE_ENERGY_REGISTER;
                break;
            case ENERGY_REACTIVE_IMPORT_REGISTER:
                result = Measurand.IMPORTED_REACTIVE_ENERGY_REGISTER;
                break;
            case ENERGY_ACTIVE_EXPORT_INTERVAL:
                result = Measurand.EXPORTED_ACTIVE_ENERGY_INTERVAL;
                break;
            case ENERGY_ACTIVE_IMPORT_INTERVAL:
                result = Measurand.IMPORTED_ACTIVE_ENERGY_INTERVAL;
                break;
            case ENERGY_REACTIVE_EXPORT_INTERVAL:
                result = Measurand.EXPORTED_REACTIVE_ENERGY_INTERVAL;
                break;
            case ENERGY_REACTIVE_IMPORT_INTERVAL:
                result = Measurand.IMPORTED_REACTIVE_ENERGY_INTERVAL;
                break;
            case POWER_ACTIVE_EXPORT:
                result = Measurand.EXPORTED_ACTIVE_POWER;
                break;
            case POWER_ACTIVE_IMPORT:
                result = Measurand.IMPORTED_ACTIVE_POWER;
                break;
            case POWER_REACTIVE_EXPORT:
                result = Measurand.EXPORTED_REACTIVE_POWER;
                break;
            case POWER_REACTIVE_IMPORT:
                result =Measurand.IMPORTED_REACTIVE_POWER;
                break;
            case CURRENT_EXPORT:
                result = Measurand.EXPORTED_CURRENT;
                break;
            case CURRENT_IMPORT:
                result = Measurand.IMPORTED_CURRENT;
                break;
            case VOLTAGE:
                result = Measurand.VOLTAGE;
                break;
            case TEMPERATURE:
                result = Measurand.TEMPERATURE;
                break;
            default:
                throw new AssertionError(String.format("Unknown value for Measurand: '%s'", measurand));
        }

        return result;
    }

}
