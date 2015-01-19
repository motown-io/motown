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
package io.motown.domain.utils;

import java.util.HashMap;

/**
 * Map implementation for request/command attributes. Information that's not required for commands but should not be
 * lost should be put in a AttributeMap.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class AttributeMap<K, V> extends HashMap<K, V> {

    /**
     * Puts the key and value in the map if the value is not {@code null}. If the value is {@code null} the key and
     * value are removed.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the map so calls can be chained
     */
    public AttributeMap<K, V> putIfValueNotNull(K key, V value) {
        if (value != null) {
            super.put(key, value);
        } else {
            super.remove(key);
        }
        return this;
    }
}
