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
import java.util.UUID;

import static com.google.common.base.Objects.toStringHelper;

/**
 * A {@code String} based identifying token used to correlate commands and events to each other.
 */
public final class CorrelationToken {

    public static final String KEY = "correlationToken";

    private final String token;

    /**
     * Create a {@code CorrelationToken} with a {@code UUID} based token.
     */
    public CorrelationToken() {
        this.token = UUID.randomUUID().toString();
    }

    /**
     * Gets the token.
     *
     * @return the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(token);
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
        final CorrelationToken other = (CorrelationToken) obj;
        return Objects.equals(this.token, other.token);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("token", token)
                .toString();
    }
}
