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
package io.motown.ocpp.websocketjson.gson.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.motown.ocpp.websocketjson.schema.generated.v15.Sendlocallist;

import java.lang.reflect.Type;

public class SendLocalListRequestUpdateTypeAdapterSerializer implements TypeAdapterSerializer<Sendlocallist.UpdateType> {

    @Override
    public JsonElement serialize(Sendlocallist.UpdateType updateType, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(updateType.toString());
    }

    @Override
    public Class<?> getAdaptedType() {
        return Sendlocallist.UpdateType.class;
    }

}
