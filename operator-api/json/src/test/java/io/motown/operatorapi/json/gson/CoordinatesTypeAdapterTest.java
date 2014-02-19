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
import io.motown.domain.api.chargingstation.Coordinates;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CoordinatesTypeAdapterTest {

    @Test
    public void testCoordinatesTypeAdapter() {
        CoordinatesTypeAdapter adapter = new CoordinatesTypeAdapter();

        assertEquals(adapter.getAdaptedType(), Coordinates.class);

        JsonObject coordinatesJson = new JsonObject();
        coordinatesJson.addProperty("latitude", 0.0);
        coordinatesJson.addProperty("longitude", 0.0);

        Coordinates coordinates = adapter.deserialize(coordinatesJson, Coordinates.class, null);

        assertEquals(coordinatesJson.get("latitude").getAsDouble(), coordinates.getLatitude());
        assertEquals(coordinatesJson.get("longitude").getAsDouble(), coordinates.getLongitude());
    }
}
