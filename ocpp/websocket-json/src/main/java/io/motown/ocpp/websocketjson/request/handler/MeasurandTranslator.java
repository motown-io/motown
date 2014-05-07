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

        switch (this.measurand) {
            case "Energy.Active.Export.Register":
                return Measurand.EXPORTED_ACTIVE_ENERGY_REGISTER;
            case "Energy.Active.Import.Register":
                return Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER;
            case "Energy.Reactive.Export.Register":
                return Measurand.EXPORTED_REACTIVE_ENERGY_REGISTER;
            case "Energy.Reactive.Import.Register":
                return Measurand.IMPORTED_REACTIVE_ENERGY_REGISTER;
            case "Energy.Active.Export.Interval":
                return Measurand.EXPORTED_ACTIVE_ENERGY_INTERVAL;
            case "Energy.Active.Import.Interval":
                return Measurand.IMPORTED_ACTIVE_ENERGY_INTERVAL;
            case "Energy.Reactive.Export.Interval":
                return Measurand.EXPORTED_REACTIVE_ENERGY_INTERVAL;
            case "Energy.Reactive.Import.Interval":
                return Measurand.IMPORTED_REACTIVE_ENERGY_INTERVAL;
            case "Power.Active.Export":
                return Measurand.EXPORTED_ACTIVE_POWER;
            case "Power.Active.Import":
                return Measurand.IMPORTED_ACTIVE_POWER;
            case "Power.Reactive.Export":
                return Measurand.EXPORTED_REACTIVE_POWER;
            case "Power.Reactive.Import":
                return Measurand.IMPORTED_REACTIVE_POWER;
            case "Current.Export":
                return Measurand.EXPORTED_CURRENT;
            case "Current.Import":
                return Measurand.IMPORTED_CURRENT;
            case "Voltage":
                return Measurand.VOLTAGE;
            case "Temperature":
                return Measurand.TEMPERATURE;
            default:
                throw new AssertionError(String.format("Unknown value for Measurand: '%s'", this.measurand));
        }
    }

}
