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
