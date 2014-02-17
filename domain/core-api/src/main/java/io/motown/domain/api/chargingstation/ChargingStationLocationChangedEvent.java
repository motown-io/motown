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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic event that is triggered when the location of a charging station is changed.
 */
public abstract class ChargingStationLocationChangedEvent {
    private final ChargingStationId chargingStationId;
    private final Coordinates coordinates;
    private final Address address;

    /**
     * Creates a new {@code ChargingStationLocationChangedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param coordinates the coordinates of the charging station.
     * @param address the address of the charging station.
     */
    protected ChargingStationLocationChangedEvent(ChargingStationId chargingStationId, Coordinates coordinates, Address address) {
        this.chargingStationId = checkNotNull(chargingStationId);
        if (coordinates == null && address == null) {
            throw new NullPointerException("Either coordinates or address parameter must be non-null");
        }
        this.coordinates = coordinates;
        this.address = address;
    }

    /**
     * Gets the identifier of the charging station.
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the coordinates of the charging station.
     * @return the coordinates.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the address of the charging station.
     * @return the address.
     */
    public Address getAddress() {
        return address;
    }
}
