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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.motown.domain.api.chargingstation.Day;
import io.motown.domain.api.chargingstation.OpeningTime;

import java.lang.reflect.Type;

public class OpeningTimeTypeAdapter implements TypeAdapter<OpeningTime> {
    @Override
    public Class<?> getAdaptedType() {
        return OpeningTime.class;
    }

    @Override
    public OpeningTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject obj;

        try {
            obj = jsonElement.getAsJsonObject();
            if (obj == null) {
                return null;
            }
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new JsonParseException("OpeningTime must be a valid JSON object", e);
        }

        Day day = Day.fromValue(obj.getAsJsonPrimitive("day").getAsInt());
        Integer timeStart = obj.getAsJsonPrimitive("timeStart").getAsInt();
        Integer timeStop = obj.getAsJsonPrimitive("timeStop").getAsInt();

        return new OpeningTime(day, timeStart, timeStop);
    }
}
