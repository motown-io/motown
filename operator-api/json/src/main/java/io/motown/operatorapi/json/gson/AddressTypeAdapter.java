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
import io.motown.domain.api.chargingstation.Address;

import java.lang.reflect.Type;

/**
 * Type adapter for the {@code Address} JSON object.
 * <pre>
 * {@code
 * {
 *  addressline1:    "string",
 *  addressline2:    "string",
 *  postalCode:      "string",
 *  city:            "string",
 *  region:          "string",
 *  country:         "string"
 * }
 * }
 * </pre>
 */
public class AddressTypeAdapter implements TypeAdapter<Address> {
    @Override
    public Class<?> getAdaptedType() {
        return Address.class;
    }

    @Override
    public Address deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject obj;

        try {
            obj = jsonElement.getAsJsonObject();
            if (obj == null) {
                return null;
            }
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new JsonParseException("Address must be a JSON object", e);
        }

        String addressline1 = obj.getAsJsonPrimitive("addressline1").getAsString();
        String city = obj.getAsJsonPrimitive("city").getAsString();
        String country = obj.getAsJsonPrimitive("country").getAsString();

        JsonPrimitive addressline2Obj = obj.getAsJsonPrimitive("addressline2"), postalCodeObj = obj.getAsJsonPrimitive("postalCode"), regionObj = obj.getAsJsonPrimitive("region");

        String addressline2 = addressline2Obj != null ? addressline2Obj.getAsString() : "";
        String postalCode = postalCodeObj != null ? postalCodeObj.getAsString() : "";
        String region = regionObj != null ? regionObj.getAsString() : "";

        return new Address(addressline1, addressline2, postalCode, city, region, country);
    }
}
