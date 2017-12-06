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
package io.motown.identificationauthorization.authorizationservice.persistence.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class ChargingStation {

    private static final String AUTHORIZATION_PROVIDER_SEPARATOR = ",";

    @Id
    private String id;

    // comma separated list of authorization plugins to check
    private String authorizationProviders;

    private ChargingStation() {
        // Private no-arg constructor for Hibernate.
    }

    public ChargingStation(String id) {
        this.id = id;
    }

    public ChargingStation(String id, String authorizationProviders) {
        this(id);
        this.authorizationProviders = authorizationProviders;
    }

    public String getId() {
        return id;
    }

    public String getAuthorizationProviders() {
        return authorizationProviders;
    }

    public List<String> getAuthorizationProvidersAsList() {
        if (authorizationProviders == null || authorizationProviders.isEmpty()) {
            return new ArrayList<>();
        }

        if (!authorizationProviders.contains(AUTHORIZATION_PROVIDER_SEPARATOR)) {
            //noinspection ArraysAsListWithZeroOrOneArgument
            return Arrays.asList(authorizationProviders);
        } else {
            return Arrays.asList(authorizationProviders.split(AUTHORIZATION_PROVIDER_SEPARATOR));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authorizationProviders);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ChargingStation other = (ChargingStation) obj;
        return Objects.equals(this.id, other.id) &&
                Objects.equals(this.authorizationProviders, other.authorizationProviders);
    }

}
