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

import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.Address;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AddressTypeAdapterTest {

    @Test
    public void testAddressTypeAdapter() {
        AddressTypeAdapter adapter = new AddressTypeAdapter();

        assertEquals(adapter.getAdaptedType(), Address.class);

        JsonObject addressJson = new JsonObject();
        addressJson.addProperty("addressline1", "addressline1");
        addressJson.addProperty("addressline2", "addressline2");
        addressJson.addProperty("postalCode", "postalCode");
        addressJson.addProperty("city", "city");
        addressJson.addProperty("region", "region");
        addressJson.addProperty("country", "country");

        Address address = adapter.deserialize(addressJson, Address.class, null);

        assertEquals(addressJson.get("addressline1").getAsString(), address.getAddressline1());
        assertEquals(addressJson.get("addressline2").getAsString(), address.getAddressline2());
        assertEquals(addressJson.get("postalCode").getAsString(), address.getPostalCode());
        assertEquals(addressJson.get("city").getAsString(), address.getCity());
        assertEquals(addressJson.get("region").getAsString(), address.getRegion());
        assertEquals(addressJson.get("country").getAsString(), address.getCountry());
    }
}
