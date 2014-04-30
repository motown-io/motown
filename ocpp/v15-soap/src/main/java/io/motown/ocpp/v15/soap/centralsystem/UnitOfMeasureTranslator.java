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

import io.motown.domain.api.chargingstation.UnitOfMeasure;

import javax.annotation.Nullable;

/**
 * Adapter which translates a {@code io.motown.ocpp.v15.soap.centralsystem.schema.UnitOfMeasure} to a {@code UnitOfMeasure}.
 */
class UnitOfMeasureTranslator implements Translator<UnitOfMeasure> {

    private final io.motown.ocpp.v15.soap.centralsystem.schema.UnitOfMeasure unitOfMeasure;

    /**
     * Creates a {@code UnitOfMeasureTranslationAdapter}.
     *
     * @param unitOfMeasure the unit of measure to translate.
     */
    public UnitOfMeasureTranslator(@Nullable io.motown.ocpp.v15.soap.centralsystem.schema.UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnitOfMeasure translate() {
        if (this.unitOfMeasure == null) {
            // In OCPP 1.5, WATT_HOUR is the default value.
            return UnitOfMeasure.WATT_HOUR;
        }

        switch (this.unitOfMeasure) {
            case WH:
                return UnitOfMeasure.WATT_HOUR;
            case K_WH:
                return UnitOfMeasure.KILOWATT_HOUR;
            case VARH:
                return UnitOfMeasure.VAR_HOUR;
            case KVARH:
                return UnitOfMeasure.KILOVAR_HOUR;
            case W:
                return UnitOfMeasure.WATT;
            case K_W:
                return UnitOfMeasure.KILOWATT;
            case VAR:
                return UnitOfMeasure.VAR;
            case KVAR:
                return UnitOfMeasure.KILOVAR;
            case AMP:
                return UnitOfMeasure.AMPERES;
            case VOLT:
                return UnitOfMeasure.VOLTAGE;
            case CELSIUS:
                return UnitOfMeasure.CELSIUS;
            default:
                throw new AssertionError(String.format("Unknown value for unit of measure: '%s'", unitOfMeasure));
        }
    }

}
