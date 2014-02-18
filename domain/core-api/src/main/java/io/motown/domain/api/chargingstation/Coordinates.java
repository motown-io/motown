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
 * The {@code Coordinates} object denotes the physical location in latitude/longitude of a charging station.
 * @see <a href="http://en.wikipedia.org/wiki/World_Geodetic_System">http://en.wikipedia.org/wiki/World_Geodetic_System</a>
 */
public final class Coordinates {
    private final double latitude;
    private final double longitude;

    /**
     * Constructs a {@code Coordinates} object using a latitude and longitude.
     *
     * @param latitude the latitude of the coordinates.
     * @param longitude the longitude of the coordinates.
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the coordinates.
     * @return the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the coordinates.
     * @return the longitude.
     */
    public double getLongitude() {
        return longitude;
    }
}
