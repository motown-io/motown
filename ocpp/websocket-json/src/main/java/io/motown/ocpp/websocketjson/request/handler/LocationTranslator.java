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

import io.motown.domain.api.chargingstation.Location;

/**
 * Translator which translates a {@code String} to a {@code Location}.
 */
class LocationTranslator implements Translator<Location> {

    private final String location;

    /**
     * Creates a {@code LocationTranslator}.
     *
     * @param valueFormat the location to translate.
     */
    public LocationTranslator(String location) {
        this.location = location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location translate() {
        if (this.location == null || this.location.isEmpty()) {
            return Location.OUTLET;
        }

        switch (this.location) {
            case "Inlet":
                return Location.INLET;
            case "Outlet":
                return Location.OUTLET;
            case "Body":
                return Location.BODY;
            default:
                throw new AssertionError(String.format("Unknown value for Location: '%s'", this.location));
        }
    }
}
