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
