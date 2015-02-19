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
package io.motown.mobieurope.source.entities;

public class Location {

    private long latitude;

    private long longitude;

    Location(io.motown.mobieurope.destination.soap.schema.Location location) {
        this.latitude = Long.parseLong(location.getLatitude());
        this.longitude = Long.parseLong(location.getLongitude());
    }

    public io.motown.mobieurope.destination.soap.schema.Location getLocation() {
        io.motown.mobieurope.destination.soap.schema.Location location = new io.motown.mobieurope.destination.soap.schema.Location();
        location.setLatitude(String.valueOf(this.latitude));
        location.setLongitude(String.valueOf(this.longitude));

        return location;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
