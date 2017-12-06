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
import java.util.List;
import java.util.Objects;

@Entity
public class LocalToken {

    @Id
    private String hiddenId;

    private String visualId;

    private boolean valid;

    @ManyToMany(mappedBy="localTokens")
    private List<LocalAuthChargingStation> chargingStations;

    private LocalToken() {
        // Private no-arg constructor for Hibernate.
    }

    public LocalToken(String hiddenId, String visualId, boolean valid) {
        this.hiddenId = hiddenId;
        this.visualId = visualId;
        this.valid = valid;
    }

    public String getHiddenId() {
        return hiddenId;
    }

    public String getVisualId() {
        return visualId;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hiddenId, visualId, valid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final LocalToken other = (LocalToken) obj;
        return Objects.equals(this.hiddenId, other.hiddenId) &&
                Objects.equals(this.visualId, other.visualId) &&
                Objects.equals(this.valid, other.valid);
    }
}
