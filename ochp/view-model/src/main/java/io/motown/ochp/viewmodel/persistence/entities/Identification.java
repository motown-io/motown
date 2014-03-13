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
package io.motown.ochp.viewmodel.persistence.entities;

import io.motown.domain.api.chargingstation.AuthorizationResultStatus;
import io.motown.ochp.viewmodel.persistence.TransactionStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Identification {

    @Id
    private String identificationId;

    @Enumerated(EnumType.STRING)
    private AuthorizationResultStatus authorizationStatus;

    private Identification() {
        // Private no-arg constructor for Hibernate.
    }

    public Identification(String identificationId, AuthorizationResultStatus authorizationStatus) {
        this.identificationId = identificationId;
        this.authorizationStatus = authorizationStatus;
    }

    public String getIdentificationId() {
        return identificationId;
    }

    public AuthorizationResultStatus getAuthorizationStatus() {
        return authorizationStatus;
    }

    public void setAuthorizationStatus(AuthorizationResultStatus authorizationStatus) {
        this.authorizationStatus = authorizationStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificationId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Identification other = (Identification) obj;
        return Objects.equals(this.identificationId, other.identificationId);
    }
}
