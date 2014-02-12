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

public final class Address {
    private final String addressline1;
    private String addressline2;
    private String postalCode;
    private final String city;
    private String region;
    private final String country;

    public Address(String addressline1, String addressline2, String postalCode, String city, String region, String country) {
        checkNotNull(addressline1);
        checkArgument(!addressline1.isEmpty());
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
        this.postalCode = postalCode;
        checkNotNull(city);
        checkArgument(!city.isEmpty());
        this.city = city;
        this.region = region;
        checkNotNull(country);
        checkArgument(!country.isEmpty());
        this.country = country;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }
}
