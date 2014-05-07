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
package io.motown.operatorapi.json.gson.deserializer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import io.motown.domain.api.chargingstation.Address;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AddressTypeAdapterDeserializerTest {
    private final AddressTypeAdapterDeserializer adapter = new AddressTypeAdapterDeserializer();

    @Test
    public void testAddressTypeAdapter() {
        assertEquals(adapter.getAdaptedType(), Address.class);

        JsonObject addressJson = new JsonObject();
        addressJson.addProperty("addressLine1", "addressLine1");
        addressJson.addProperty("addressLine2", "addressLine2");
        addressJson.addProperty("postalCode", "postalCode");
        addressJson.addProperty("city", "city");
        addressJson.addProperty("region", "region");
        addressJson.addProperty("country", "country");

        Address address = adapter.deserialize(addressJson, Address.class, null);

        assertEquals(addressJson.get("addressLine1").getAsString(), address.getAddressLine1());
        assertEquals(addressJson.get("addressLine2").getAsString(), address.getAddressLine2());
        assertEquals(addressJson.get("postalCode").getAsString(), address.getPostalCode());
        assertEquals(addressJson.get("city").getAsString(), address.getCity());
        assertEquals(addressJson.get("region").getAsString(), address.getRegion());
        assertEquals(addressJson.get("country").getAsString(), address.getCountry());
    }

    @Test(expected = JsonParseException.class)
    public void testAddressAsPrimitive() {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("addressLine1");
        adapter.deserialize(jsonPrimitive, Address.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyMandatoryFields() {
        JsonObject addressJson = new JsonObject();
        addressJson.addProperty("addressLine1", "");
        addressJson.addProperty("addressLine2", "");
        addressJson.addProperty("postalCode", "");
        addressJson.addProperty("city", "");
        addressJson.addProperty("region", "");
        addressJson.addProperty("country", "");

        adapter.deserialize(addressJson, Address.class, null);
    }
}
