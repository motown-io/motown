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

import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class CommandTypeAdapter<C> implements TypeAdapter<C> {

    @Override
    public C deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        checkArgument(json.isJsonObject(), "API command must be a JSON object");
        JsonObject commandObject = json.getAsJsonObject();
        return deserialize(commandObject, context);
    }

    public abstract C deserialize(JsonObject commandObject, JsonDeserializationContext context) throws JsonParseException;

    public abstract Class<?> getAdaptedType();
}
