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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The unique identifier of a connector.
 */
public final class ConnectorId implements ComponentId {

    private final int numberedId;

    private final String id;

    /**
     * Creates a connector identifier.
     *
     * @param id the unique identifier of a connector.
     * @throws IllegalArgumentException if {@code id} is negative.
     */
    public ConnectorId(int id) {
        checkArgument(id >= 0);
        this.numberedId = id;
        this.id = Integer.toString(id);
    }

    /**
     * Gets the connector's unique identifier.
     *
     * @return the unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the connector's unique identifier as a number.
     *
     * @return the unique identifier as a number.
     */
    public int getNumberedId() {
        return numberedId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConnectorId that = (ConnectorId) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
