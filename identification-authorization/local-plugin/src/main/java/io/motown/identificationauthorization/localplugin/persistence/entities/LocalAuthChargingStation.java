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
package io.motown.identificationauthorization.localplugin.persistence.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class LocalAuthChargingStation {

    @Id
    private String id;

    @ManyToMany
    private Set<LocalToken> localTokens = new HashSet<>();

    private LocalAuthChargingStation() {
        // Private no-arg constructor for Hibernate.
    }

    public LocalAuthChargingStation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public Set<LocalToken> getLocalTokens() {
        return localTokens;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final LocalAuthChargingStation other = (LocalAuthChargingStation) obj;
        return Objects.equals(this.id, other.id);
    }
}
