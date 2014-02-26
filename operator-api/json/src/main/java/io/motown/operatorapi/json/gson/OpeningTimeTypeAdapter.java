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
import io.motown.domain.api.chargingstation.TimeOfDay;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Type adapter for the {@code OpeningTime} JSON object.
 * <pre>
 * {@code
 * {
 *  day:        "int",      // 1-7 representing monday - sunday
 *  timeStart:  "string",   // valid 24-hour time (00:00 - 23:59)
 *  timeStop:   "string",   // same sa timeStart
 * }
 * }
 * </pre>
 */
public class OpeningTimeTypeAdapter implements TypeAdapter<OpeningTime> {
    private static final Pattern TIME_OF_DAY = Pattern.compile("^([01]?[0-9]|2[0-3]):([0-5][0-9])$");
    private static final int HOUR_GROUP = 1;
    private static final int MINUTES_GROUP = 2;

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
        } catch (ClassCastException | IllegalStateException e) {
            throw new JsonParseException("OpeningTime must be a valid JSON object", e);
        }

        Day day = Day.fromValue(obj.getAsJsonPrimitive("day").getAsInt());
        String timeStartStr = obj.getAsJsonPrimitive("timeStart").getAsString();
        String timeStopStr = obj.getAsJsonPrimitive("timeStop").getAsString();

        TimeOfDay timeStart;
        TimeOfDay timeStop;

        Matcher start = TIME_OF_DAY.matcher(timeStartStr);
        Matcher stop = TIME_OF_DAY.matcher(timeStopStr);
        if (start.matches() && stop.matches()) {
            timeStart = new TimeOfDay(Integer.parseInt(start.group(HOUR_GROUP)), Integer.parseInt(start.group(MINUTES_GROUP)));
            timeStop = new TimeOfDay(Integer.parseInt(stop.group(HOUR_GROUP)), Integer.parseInt(stop.group(MINUTES_GROUP)));
        } else {
            throw new JsonParseException("timeStart and timeStop must be a valid 24-hour time (00:00 - 23:59)");
        }

        return new OpeningTime(day, timeStart, timeStop);
    }
}
