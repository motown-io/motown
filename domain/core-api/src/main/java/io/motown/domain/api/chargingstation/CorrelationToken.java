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
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A {@code String} based identifying token used to correlate commands and events to each other.
 */
public final class CorrelationToken {

    public static final String KEY = "correlationToken";

    private final String token;

    /**
     * Create a {@code CorrelationToken} with a {@UUID} based token.
     */
    public CorrelationToken(){
        this(UUID.randomUUID().toString());
    }

    /**
     * Create a {@code CorrelationToken} with a {@String} based token.
     *
     * @param token the token.
     * @throws NullPointerException     if {@token} is null.
     * @throws IllegalArgumentException if {@token} is empty.
     */
    public CorrelationToken(String token) {
        checkNotNull(token);
        checkArgument(!token.isEmpty());

        this.token = token;
    }

    /**
     * {@inheritDoc}
     */
    public String getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

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
