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

import io.motown.domain.api.security.IdentityContext;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic event that is triggered when the location of a charging station is changed.
 */
public abstract class ChargingStationLocationChangedEvent {
    private final ChargingStationId chargingStationId;
    private final Coordinates coordinates;
    private final Address address;
    private final Accessibility accessibility;
    private final IdentityContext identityContext;

    /**
     * Creates a new {@code ChargingStationLocationChangedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param coordinates       the coordinates of the charging station.
     * @param address           the address of the charging station.
     * @param accessibility     the accessibility of the charging station.
     * @param identityContext   the identity context.
     * @throws java.lang.NullPointerException if {@code coordinates} and {@code address} is {@code null} or any of the other parameters is {@code null}.
     */
    protected ChargingStationLocationChangedEvent(ChargingStationId chargingStationId, Coordinates coordinates, Address address, Accessibility accessibility, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        if (coordinates == null && address == null) {
            throw new NullPointerException("Either coordinates or address parameter must be non-null");
        }
        this.coordinates = coordinates;
        this.address = address;
        this.accessibility = checkNotNull(accessibility);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the identifier of the charging station.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the coordinates of the charging station.
     *
     * @return the coordinates.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets the address of the charging station.
     *
     * @return the address.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Gets the accessibility of the charging station.
     *
     * @return the accesibility.
     */
    public Accessibility getAccessibility() {
        return accessibility;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, coordinates, address, accessibility, identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChargingStationLocationChangedEvent other = (ChargingStationLocationChangedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.coordinates, other.coordinates) && Objects.equals(this.address, other.address) && Objects.equals(this.accessibility, other.accessibility) && Objects.equals(this.identityContext, other.identityContext);
    }
}
