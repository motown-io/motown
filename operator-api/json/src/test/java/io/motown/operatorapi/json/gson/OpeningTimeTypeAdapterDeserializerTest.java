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
import io.motown.domain.api.chargingstation.OpeningTime;
import io.motown.operatorapi.json.gson.OpeningTimeTypeAdapterDeserializer;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class OpeningTimeTypeAdapterDeserializerTest {
    private final OpeningTimeTypeAdapterDeserializer adapter = new OpeningTimeTypeAdapterDeserializer();

    @Test
    public void testOpeningTimeTypeAdapter() {
        assertEquals(adapter.getAdaptedType(), OpeningTime.class);

        JsonObject openingTimeJson = new JsonObject();
        openingTimeJson.addProperty("day", "Monday");
        openingTimeJson.addProperty("timeStart", "12:00");
        openingTimeJson.addProperty("timeStop", "15:00");

        OpeningTime openingTime = adapter.deserialize(openingTimeJson, OpeningTime.class, null);

        assertEquals(openingTimeJson.get("day").getAsString(), openingTime.getDay().value());
        assertEquals(openingTimeJson.get("timeStart").getAsString(), String.format("%02d:%02d", openingTime.getTimeStart().getHourOfDay(), openingTime.getTimeStart().getMinutesInHour()));
        assertEquals(openingTimeJson.get("timeStop").getAsString(), String.format("%02d:%02d", openingTime.getTimeStop().getHourOfDay(), openingTime.getTimeStop().getMinutesInHour()));
    }

    @Test(expected = JsonParseException.class)
    public void testOpeningTimeAsJsonPrimitive() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("12:00");
        adapter.deserialize(jsonPrimitive, OpeningTime.class, null);
    }

    @Test(expected = JsonParseException.class)
    public void testUnparsableTimeFormat() {
        JsonObject openingTimeJson = new JsonObject();
        openingTimeJson.addProperty("day", "Monday");
        openingTimeJson.addProperty("timeStart", "12.00");
        openingTimeJson.addProperty("timeStop", "15.00");
        adapter.deserialize(openingTimeJson, OpeningTime.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDay() {
        JsonObject openingTimeJson = new JsonObject();
        openingTimeJson.addProperty("day", 8);
        openingTimeJson.addProperty("timeStart", "12:00");
        openingTimeJson.addProperty("timeStop", "15:00");
        adapter.deserialize(openingTimeJson, OpeningTime.class, null);
    }

}
