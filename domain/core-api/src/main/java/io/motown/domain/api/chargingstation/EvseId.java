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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The unique identifier of a evse.
 */
public final class EvseId implements ComponentId {

    private final int numberedId;

    private final String id;

    /**
     * Creates a evse identifier.
     *
     * @param id the unique identifier of a evse.
     * @throws IllegalArgumentException if {@code id} is negative.
     */
    public EvseId(int id) {
        checkArgument(id >= 0);
        this.numberedId = id;
        this.id = Integer.toString(id);
    }

    /**
     * Gets the evse's unique identifier.
     *
     * @return the unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the evse's unique identifier as a number.
     *
     * @return the unique identifier as a number.
     */
    public int getNumberedId() {
        return numberedId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberedId, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final EvseId other = (EvseId) obj;
        return Objects.equals(this.numberedId, other.numberedId) && Objects.equals(this.id, other.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
