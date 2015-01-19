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
package io.motown.ocpp.websocketjson.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.motown.ocpp.websocketjson.gson.deserializer.TypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.serializer.TypeAdapterSerializer;

import java.util.Set;

public class GsonFactoryBean {

    private String dateFormat;

    private Set<TypeAdapterSerializer<?>> typeAdapterSerializers;

    private Set<TypeAdapterDeserializer<?>> typeAdapterDeserializers;

    public Gson createGson() {
        GsonBuilder builder = new GsonBuilder();

        if (dateFormat != null && !dateFormat.isEmpty()) {
            builder.setDateFormat(dateFormat);
        }

        for (TypeAdapterSerializer<?> typeAdapter : typeAdapterSerializers) {
            builder.registerTypeAdapter(typeAdapter.getAdaptedType(), typeAdapter);
        }

        for (TypeAdapterDeserializer<?> typeAdapter : typeAdapterDeserializers) {
            builder.registerTypeAdapter(typeAdapter.getAdaptedType(), typeAdapter);
        }

        return builder.create();
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setTypeAdapterSerializers(Set<TypeAdapterSerializer<?>> typeAdapterSerializers) {
        this.typeAdapterSerializers = typeAdapterSerializers;
    }

    public void setTypeAdapterDeserializers(Set<TypeAdapterDeserializer<?>> typeAdapterDeserializers) {
        this.typeAdapterDeserializers = typeAdapterDeserializers;
    }
}
