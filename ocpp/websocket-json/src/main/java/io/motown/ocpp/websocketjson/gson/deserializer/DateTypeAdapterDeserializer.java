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
package io.motown.ocpp.websocketjson.gson.deserializer;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Type adapter for Date. By default "GMT" timezone is used with date format "yyyy-MM-dd'T'HH:mm:ss'Z'".
 */
public class DateTypeAdapterDeserializer implements TypeAdapterDeserializer<Date> {

    private List<DateFormat> dateFormats = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(DateTypeAdapterDeserializer.class);

    public DateTypeAdapterDeserializer() {
        this("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("GMT"));
    }

    public DateTypeAdapterDeserializer(String dateFormat, TimeZone timeZone) {
        this(Arrays.asList(dateFormat), timeZone);
    }

    public DateTypeAdapterDeserializer(List<String> supportedDateFormats, TimeZone timeZone) {
        for(String dateFormat:supportedDateFormats) {
            DateFormat iso8601Format = new SimpleDateFormat(dateFormat);
            iso8601Format.setTimeZone(timeZone);

            dateFormats.add(iso8601Format);
        }
    }

    @Override
    public Class<?> getAdaptedType() {
        return Date.class;
    }

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        for(DateFormat dateFormat:dateFormats) {
            try {
                return dateFormat.parse(jsonElement.getAsString());
            } catch (ParseException e) {
                LOG.trace("Could not parse: [{}] with date format [{}]", jsonElement.getAsString(), dateFormat);
            }
        }
        LOG.warn("Was not able to deserialize date formatted [{}] with available date formatters. If this date format should be supported configure a date formatter for this format.", jsonElement.getAsString());
        throw new JsonParseException(String.format("Cannot deserialize: %s", jsonElement.getAsString()));
    }

}
