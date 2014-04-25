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
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A charging station's configuration item. A configuration item consists of a key and value.
 */
public class ConfigurationItem {

    private final String key;

    private final String value;

    /**
     * Create a {@code ConfigurationItem}.
     *
     * @param key   the configuration item's key.
     * @param value the configuration item's value.
     */
    public ConfigurationItem(String key, String value) {
        checkNotNull(key);
        checkArgument(!key.isEmpty());
        this.key = key;
        checkNotNull(value);
        checkArgument(!value.isEmpty());
        this.value = value;
    }

    /**
     * Gets the configuration item's key.
     *
     * @return the configuration item's key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the configuration item's value.
     *
     * @return the configuration item's value.
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
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
        final ConfigurationItem other = (ConfigurationItem) obj;
        return Objects.equals(this.key, other.key) && Objects.equals(this.value, other.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("key", key)
                .add("value", value)
                .toString();
    }
}
