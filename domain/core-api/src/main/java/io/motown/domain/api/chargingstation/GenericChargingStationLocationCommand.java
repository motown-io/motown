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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class GenericChargingStationLocationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final Coordinates coordinates;
    private final Address address;

    protected GenericChargingStationLocationCommand(ChargingStationId chargingStationId, Coordinates coordinates, Address address) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkArgument(coordinates != null || address != null);
        this.coordinates = coordinates;
        this.address = address;
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

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
        final GenericChargingStationLocationCommand other = (GenericChargingStationLocationCommand) obj;
        return Objects.equal(this.chargingStationId, other.chargingStationId) && Objects.equal(this.coordinates, other.coordinates) && Objects.equal(this.address, other.address);
    }
}
