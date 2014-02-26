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
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import io.motown.domain.api.chargingstation.EvseId;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EvseIdTypeAdapterTest {
    private final EvseIdTypeAdapter adapter = new EvseIdTypeAdapter();

    @Test
    public void testEvseIdTypeAdapter() {
        assertEquals(adapter.getAdaptedType(), EvseId.class);

        JsonPrimitive evseIdJson = new JsonPrimitive(1);

        EvseId evseId = adapter.deserialize(evseIdJson, EvseId.class, null);

        assertEquals(evseIdJson.getAsInt(), evseId.getNumberedId());
    }

    @Test(expected = JsonParseException.class)
    public void testEvseIdAsObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("evseId", 1);
        adapter.deserialize(jsonObject, EvseId.class, null);
    }

    @Test(expected = JsonParseException.class)
    public void testEvseIdAsUnparsableString() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("1x");
        adapter.deserialize(jsonPrimitive, EvseId.class, null);
    }
}
