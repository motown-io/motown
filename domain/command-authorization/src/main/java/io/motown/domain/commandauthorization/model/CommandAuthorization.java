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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

/**
 * Entity containing command authorization. A user identity can have access to certain commands on a charging station.
 * One entity instance stands for one user identity that has access to one command on one charging station.
 */
@Entity
public class CommandAuthorization {

    @EmbeddedId
    private CommandAuthorizationId commandAuthorizationId;

    public CommandAuthorization() {
    }

    public CommandAuthorization(CommandAuthorizationId commandAuthorizationId) {
        this.commandAuthorizationId = commandAuthorizationId;
    }

    public CommandAuthorizationId getCommandAuthorizationId() {
        return commandAuthorizationId;
    }

    public void setCommandAuthorizationId(CommandAuthorizationId commandAuthorizationId) {
        this.commandAuthorizationId = commandAuthorizationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandAuthorizationId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CommandAuthorization other = (CommandAuthorization) obj;
        return Objects.equals(this.commandAuthorizationId, other.commandAuthorizationId);
    }
}
