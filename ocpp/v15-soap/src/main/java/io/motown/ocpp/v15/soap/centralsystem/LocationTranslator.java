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

import io.motown.domain.api.chargingstation.Location;

import javax.annotation.Nullable;

/**
 * Adapter which translates a {@code io.motown.ocpp.v15.soap.centralsystem.schema.Location} to a {@code Location}.
 */
class LocationTranslator implements Translator<Location> {

    private final io.motown.ocpp.v15.soap.centralsystem.schema.Location location;

    /**
     * Creates a {@code LocationTranslationAdapter}.
     *
     * @param location the location to translate.
     */
    public LocationTranslator(@Nullable io.motown.ocpp.v15.soap.centralsystem.schema.Location location) {
        this.location = location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location translate() {
        if (this.location == null) {
            // In OCPP 1.5, OUTLET is the default value.
            return Location.OUTLET;
        }

        switch (this.location) {
            case INLET:
                return Location.INLET;
            case OUTLET:
                return Location.OUTLET;
            case BODY:
                return Location.BODY;
            default:
                throw new AssertionError(String.format("Unknown value for Location: '%s'", location));
        }
    }

}
