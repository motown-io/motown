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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AttributeMapTest {

    @Test
    public void putValueShouldStoreKeyAndValue() {
        AttributeMap<String, String> attributeMap = new AttributeMap<>();
        String key = "key";
        String value = "value";

        attributeMap.putIfValueNotNull(key, value);

        assertEquals(value, attributeMap.get(key));
    }

    @Test
    public void putNullValueShouldNotStoreKey() {
        AttributeMap<String, String> attributeMap = new AttributeMap<>();
        String key = "key";

        attributeMap.putIfValueNotNull(key, null);

        assertFalse(attributeMap.keySet().contains(key));
    }

    @Test
    public void testChaining() {
        AttributeMap<String, String> attributeMap = new AttributeMap<>();
        String key = "key";
        String key2 = "key2";
        String value = "value";
        String value2 = "value2";

        attributeMap.putIfValueNotNull(key, value)
                    .putIfValueNotNull(key2, value2);

        assertEquals(value, attributeMap.get(key));
        assertEquals(value2, attributeMap.get(key2));
    }

}
