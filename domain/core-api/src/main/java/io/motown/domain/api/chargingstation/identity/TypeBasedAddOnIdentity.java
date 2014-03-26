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
package io.motown.domain.api.chargingstation.identity;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Identity used by add-ons.
 */
public class TypeBasedAddOnIdentity implements AddOnIdentity {

    /**
     * Identifier of the add-on type.
     */
    private String addOnType;

    /**
     * Identifier of the add-on. This can be used to differentiate instances of the same add-on.
     */
    private String addOnId;

    /**
     * The format of the id which is a combination of addOnType and addOnId.
     */
    private static final String STRING_ID_FORMAT = "%s-%s";

    /**
     * Creates a TypeBasedAddOnIdentity with a type and id.
     *
     * @param addOnType the add-on type.
     * @param addOnId the add-on id.
     * @throws NullPointerException if addOnType or addOnId is null.
     * @throws IllegalArgumentException if addOnType or addOnId is empty.
     */
    public TypeBasedAddOnIdentity(String addOnType, String addOnId) {
        this.addOnType = checkNotNull(addOnType);
        this.addOnId = checkNotNull(addOnId);
        checkArgument(!addOnType.isEmpty());
        checkArgument(!addOnId.isEmpty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return String.format(STRING_ID_FORMAT, addOnType, addOnId);
    }

    /**
     * Gets the add-on type.
     *
     * @return add-on type.
     */
    public String getAddOnType() {
        return addOnType;
    }

    /**
     * Gets the add-on id. This can be used to differentiate instances of the same add-on.
     *
     * @return add-on id.
     */
    public String getAddOnId() {
        return addOnId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addOnType, addOnId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TypeBasedAddOnIdentity other = (TypeBasedAddOnIdentity) obj;
        return Objects.equals(this.addOnType, other.addOnType) && Objects.equals(this.addOnId, other.addOnId);
    }
}
