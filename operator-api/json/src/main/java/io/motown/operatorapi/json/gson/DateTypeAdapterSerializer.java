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

import com.google.gson.*;
import io.motown.utils.rest.json.gson.TypeAdapterSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Type adapter for Date. By default "UTC" is used with date format "yyyy-MM-dd'T'HH:mm:ss'Z'".
 */
public class DateTypeAdapterSerializer implements TypeAdapterSerializer<Date> {

    private DateFormat iso8601Format;

    public DateTypeAdapterSerializer() {
        this("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("UTC"));
    }

    public DateTypeAdapterSerializer(String dateFormat, TimeZone timeZone) {
        iso8601Format = new SimpleDateFormat(dateFormat);
        iso8601Format.setTimeZone(timeZone);
    }

    @Override
    public Class<?> getAdaptedType() {
        return Date.class;
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(iso8601Format.format(date));
    }

}
