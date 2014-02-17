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

import com.google.common.base.Objects;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic class for all commands that change the location ({@link Coordinates} or {@link Address}).
 */
public abstract class ChangeChargingStationLocationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final Coordinates coordinates;
    private final Address address;

    /**
     * Creates a command object that changes the location of a charging station.
     *
     * @param chargingStationId The identifier of the charging station.
     * @param coordinates The coordinates (latitude/longitude) of the charging station.
     * @param address The address of the charging station.
     * @throws java.lang.NullPointerException if either {@code coordinates} or {@code address} is {@code null}.
     */
    protected ChangeChargingStationLocationCommand(ChargingStationId chargingStationId, Coordinates coordinates, Address address) {
        this.chargingStationId = checkNotNull(chargingStationId);
        if (coordinates == null && address == null) {
            throw new NullPointerException("Either coordinates or address parameter must be non-null");
        }
        this.coordinates = coordinates;
        this.address = address;
    }

    /**
     * Gets the charging station identifier.
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

    @Override
    public int hashCode() {
        return Objects.hashCode(chargingStationId, coordinates, address);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChangeChargingStationLocationCommand other = (ChangeChargingStationLocationCommand) obj;
        return Objects.equal(this.chargingStationId, other.chargingStationId) && Objects.equal(this.coordinates, other.coordinates) && Objects.equal(this.address, other.address);
    }
}
