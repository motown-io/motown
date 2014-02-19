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

import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.TextualToken;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TextualTokenTypeAdapterTest {

    private static TextualTokenTypeAdapter adapter;

    @BeforeClass
    public static void setUp() {
        adapter = new TextualTokenTypeAdapter();
        assertEquals(adapter.getAdaptedType(), TextualToken.class);
    }

    @Test
    public void testTextualTokenTypeAdapter() {
        JsonObject textualTokenJson = new JsonObject();
        textualTokenJson.addProperty("token", "1");

        TextualToken textualToken = adapter.deserialize(textualTokenJson, TextualToken.class, null);

        assertEquals(textualTokenJson.get("token").getAsString(), textualToken.getToken());
    }

    @Test
    public void testTextualTokenTypeAdapterWithStatus() {
        JsonObject textualTokenJson = new JsonObject();
        textualTokenJson.addProperty("token", "1");
        textualTokenJson.addProperty("status", "ACCEPTED");

        TextualToken textualToken = adapter.deserialize(textualTokenJson, TextualToken.class, null);

        assertEquals(textualTokenJson.get("token").getAsString(), textualToken.getToken());
        assertEquals(textualTokenJson.get("status").getAsString(), textualToken.getAuthenticationStatus().toString());
    }
}
