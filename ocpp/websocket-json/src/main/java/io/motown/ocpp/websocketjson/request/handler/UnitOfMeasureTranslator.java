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

import io.motown.domain.api.chargingstation.UnitOfMeasure;

/**
 * Translator which translates a {@code String} to a {@code UnitOfMeasure}.
 */
public class UnitOfMeasureTranslator implements Translator<UnitOfMeasure> {

    private final String unitOfMeasure;

    /**
     * Creates a {@code UnitOfMeasureTranslator}.
     *
     * @param unitOfMeasure the unit of measure to translate.
     */
    public UnitOfMeasureTranslator(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnitOfMeasure translate() {
        if (this.unitOfMeasure == null || this.unitOfMeasure.isEmpty()) {
            return UnitOfMeasure.WATT_HOUR;
        }

        UnitOfMeasure result;
        switch (this.unitOfMeasure) {
            case "Wh":
                result = UnitOfMeasure.WATT_HOUR;
                break;
            case "kWh":
                result = UnitOfMeasure.KILOWATT_HOUR;
                break;
            case "varh":
                result = UnitOfMeasure.VAR_HOUR;
                break;
            case "kvarh":
                result = UnitOfMeasure.KILOVAR_HOUR;
                break;
            case "W":
                result = UnitOfMeasure.WATT;
                break;
            case "kW":
                result = UnitOfMeasure.KILOWATT;
                break;
            case "var":
                result = UnitOfMeasure.VAR;
                break;
            case "kvar":
                result = UnitOfMeasure.KILOVAR;
                break;
            case "Amp":
                result = UnitOfMeasure.AMPERES;
                break;
            case "Volt":
                result = UnitOfMeasure.VOLTAGE;
                break;
            case "Celsius":
                result = UnitOfMeasure.CELSIUS;
                break;
            default:
                throw new AssertionError(String.format("Unknown value for UnitOfMeasure: '%s'", this.unitOfMeasure));
        }

        return result;
    }
}
