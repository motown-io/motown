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
package io.motown.ocpp.websocketjson.request.handler;

import io.motown.domain.api.chargingstation.Measurand;

/**
 * Translator which translates a {@code String} to a {@code Measurand}.
 */
class MeasurandTranslator implements Translator<Measurand> {

    private final String measurand;

    /**
     * Creates a {@code MeasurandTranslator}.
     *
     * @param measurand the measurand to translate.
     */
    public MeasurandTranslator(String measurand) {
        this.measurand = measurand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurand translate() {
        if (this.measurand == null || this.measurand.isEmpty()) {
            return Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER;
        }

        Measurand result;
        switch (this.measurand) {
            case "Energy.Active.Export.Register":
                result = Measurand.EXPORTED_ACTIVE_ENERGY_REGISTER;
                break;
            case "Energy.Active.Import.Register":
                result = Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER;
                break;
            case "Energy.Reactive.Export.Register":
                result = Measurand.EXPORTED_REACTIVE_ENERGY_REGISTER;
                break;
            case "Energy.Reactive.Import.Register":
                result = Measurand.IMPORTED_REACTIVE_ENERGY_REGISTER;
                break;
            case "Energy.Active.Export.Interval":
                result = Measurand.EXPORTED_ACTIVE_ENERGY_INTERVAL;
                break;
            case "Energy.Active.Import.Interval":
                result = Measurand.IMPORTED_ACTIVE_ENERGY_INTERVAL;
                break;
            case "Energy.Reactive.Export.Interval":
                result = Measurand.EXPORTED_REACTIVE_ENERGY_INTERVAL;
                break;
            case "Energy.Reactive.Import.Interval":
                result = Measurand.IMPORTED_REACTIVE_ENERGY_INTERVAL;
                break;
            case "Power.Active.Export":
                result = Measurand.EXPORTED_ACTIVE_POWER;
                break;
            case "Power.Active.Import":
                result = Measurand.IMPORTED_ACTIVE_POWER;
                break;
            case "Power.Reactive.Export":
                result = Measurand.EXPORTED_REACTIVE_POWER;
                break;
            case "Power.Reactive.Import":
                result = Measurand.IMPORTED_REACTIVE_POWER;
                break;
            case "Current.Export":
                result = Measurand.EXPORTED_CURRENT;
                break;
            case "Current.Import":
                result = Measurand.IMPORTED_CURRENT;
                break;
            case "Voltage":
                result = Measurand.VOLTAGE;
                break;
            case "Temperature":
                result = Measurand.TEMPERATURE;
                break;
            default:
                throw new AssertionError(String.format("Unknown value for Measurand: '%s'", this.measurand));
        }

        return result;
    }

}
