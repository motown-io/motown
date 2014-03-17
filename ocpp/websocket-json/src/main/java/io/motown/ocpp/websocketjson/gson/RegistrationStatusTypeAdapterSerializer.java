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

import com.google.gson.*;
import io.motown.ocpp.websocketjson.response.centralsystem.RegistrationStatus;

import java.lang.reflect.Type;

public class RegistrationStatusTypeAdapterSerializer implements TypeAdapterSerializer<RegistrationStatus> {

    @Override
    public JsonElement serialize(RegistrationStatus registrationStatus, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(registrationStatus.value());
    }

    @Override
    public Class<?> getAdaptedType() {
        return RegistrationStatus.class;
    }

}
