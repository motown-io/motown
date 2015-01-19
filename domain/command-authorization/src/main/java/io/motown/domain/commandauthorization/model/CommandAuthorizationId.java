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
package io.motown.domain.commandauthorization.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Identifier for {@code CommandAuthorization} containing all relevant info: charging station id, user identity and
 * command class.
 */
@Embeddable
public class CommandAuthorizationId implements Serializable {

    /**
     * Charging station to which this authorization is applicable.
     */
    private String chargingStationId;

    /**
     * User identity (name).
     */
    private String userIdentity;

    /**
     * Command class the user identity has authorization for.
     */
    private Class commandClass;

    /**
     * Defined because it's required for objects used as {@code javax.persistence.EmbeddedId}.
     */
    public CommandAuthorizationId() {
    }

    public CommandAuthorizationId(String chargingStationId, String userIdentity, Class commandClass) {
        this.chargingStationId = chargingStationId;
        this.userIdentity = userIdentity;
        this.commandClass = commandClass;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public void setChargingStationId(String chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public Class getCommandClass() {
        return commandClass;
    }

    public void setCommandClass(Class commandClass) {
        this.commandClass = commandClass;
    }

    /**
     * Defined because it's required for objects used as {@code javax.persistence.EmbeddedId}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, userIdentity, commandClass);
    }

    /**
     * Defined because it's required for objects used as {@code javax.persistence.EmbeddedId}.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CommandAuthorizationId other = (CommandAuthorizationId) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.userIdentity, other.userIdentity) && Objects.equals(this.commandClass, other.commandClass);
    }
}
