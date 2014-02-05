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
import com.google.gson.JsonParseException;
import io.motown.domain.api.chargingstation.ConnectorId;

import java.lang.reflect.Type;

public class ConnectorIdTypeAdapter implements TypeAdapter<ConnectorId> {

    @Override
    public ConnectorId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive()) {
            throw new JsonParseException("ConnectorId must be a JSON primitive");
        }

        int connectorId;

        try {
            connectorId = json.getAsInt();
        } catch (ClassCastException | IllegalStateException e) {
            throw new JsonParseException("ConnectorId must be a JSON integer", e);
        }

        return new ConnectorId(connectorId);
    }

    @Override
    public Class<?> getAdaptedType() {
        return ConnectorId.class;
    }
}
