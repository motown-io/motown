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
package io.motown.domain.api.chargingstation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AddressTest {

    @Test
    public void constructorSetsFields() {
        // not using constant from test utils as it uses the method that's being tested here.
        String line1 = "line1";
        String line2 = "line2";
        String postalCode = "postal";
        String city = "city";
        String region = "region";
        String country = "country";

        Address address = new Address(line1, line2, postalCode, city, region, country);

        assertEquals(line1, address.getAddressLine1());
        assertEquals(line2, address.getAddressLine2());
        assertEquals(postalCode, address.getPostalCode());
        assertEquals(city, address.getCity());
        assertEquals(region, address.getRegion());
        assertEquals(country, address.getCountry());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArguments() {
        new Address(null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionOnEmptyArguments() {
        new Address("", "", "", "", "", "");
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(Address.class).usingGetClass().verify();
    }
}
