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
package io.motown.operatorapi.json.gson;

import com.google.gson.JsonPrimitive;
import io.motown.domain.api.chargingstation.Accessibility;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class AccessibilityTypeAdapterTest {

    @Test
    public void testAccessibility() {
        AccessibilityTypeAdapter adapter = new AccessibilityTypeAdapter();

        assertEquals(adapter.getAdaptedType(), Accessibility.class);

        JsonPrimitive accessibilityJson = new JsonPrimitive("public");

        Accessibility accessibility = adapter.deserialize(accessibilityJson, Accessibility.class, null);

        assertTrue(accessibilityJson.getAsString().equalsIgnoreCase(accessibility.toString()));
    }
}
