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

import io.motown.mobieurope.destination.soap.schema.LocalService;

public class Service {

    private String localServiceIdentifier;

    private String localServiceStatus;

    private Location location;

    public Service(String localServiceIdentifier, String localServiceStatus, Location location) {
        this.localServiceIdentifier = localServiceIdentifier;
        this.localServiceStatus = localServiceStatus;
        this.location = location;
    }

    public Service(LocalService localService) {
        this.localServiceIdentifier = localService.getLocalServiceIdentifier();
        this.localServiceStatus = localService.getLocalServiceStatus().value();
        this.location = new Location(localService.getLocation());
    }

    public String getLocalServiceIdentifier() {
        return localServiceIdentifier;
    }

    public void setLocalServiceIdentifier(String localServiceIdentifier) {
        this.localServiceIdentifier = localServiceIdentifier;
    }

    public String getLocalServiceStatus() {
        return localServiceStatus;
    }

    public void setLocalServiceStatus(String localServiceStatus) {
        this.localServiceStatus = localServiceStatus;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
