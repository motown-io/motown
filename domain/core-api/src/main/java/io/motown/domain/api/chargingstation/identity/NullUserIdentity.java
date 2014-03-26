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

/**
 * Used where a user identity is required but no user is involved in the identity context,
 */
public final class NullUserIdentity implements UserIdentity {

    private static final String ANONYMOUS_IDENTITY = "";

    private final String id;

    public NullUserIdentity() {
        this.id = ANONYMOUS_IDENTITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final NullUserIdentity other = (NullUserIdentity) obj;
        return Objects.equals(this.id, other.id);
    }
}
