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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code Address} is used to interpret an address object sent to the core api.
 */
public final class Address {
    private final String addressline1;
    private String addressline2;
    private String postalCode;
    private final String city;
    private String region;
    private final String country;

    /**
     * Creates an {@code Address} instance
     *
     * @param addressline1 This usually denotes a street including a house number.
     * @param addressline2 This is used for e.g. a building number.
     * @param postalCode The postal code of the address.
     * @param city The city of the address.
     * @param region The region of the address.
     * @param country The country of the address.
     * @throws java.lang.NullPointerException if one of the parameters is {@code null}.
     */
    public Address(String addressline1, String addressline2, String postalCode, String city, String region, String country) {
        checkNotNull(addressline1);
        checkArgument(!addressline1.isEmpty());
        this.addressline1 = addressline1;
        this.addressline2 = checkNotNull(addressline2);
        this.postalCode = checkNotNull(postalCode);
        checkNotNull(city);
        checkArgument(!city.isEmpty());
        this.city = city;
        this.region = checkNotNull(region);
        checkNotNull(country);
        checkArgument(!country.isEmpty());
        this.country = country;
    }

    /**
     * Gets the first address line.
     * @return the first address line.
     */
    public String getAddressline1() {
        return addressline1;
    }

    /**
     * Gets the second address line.
     * @return the second address line.
     */
    public String getAddressline2() {
        return addressline2;
    }

    /**
     * Gets the postal code.
     * @return the postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets the city.
     * @return the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the region.
     * @return the region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the country.
     * @return the country.
     */
    public String getCountry() {
        return country;
    }
}
