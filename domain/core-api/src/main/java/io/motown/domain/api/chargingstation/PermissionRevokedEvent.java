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

import io.motown.domain.api.chargingstation.identity.IdentityContext;
import io.motown.domain.api.chargingstation.identity.UserIdentity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code PermissionRevokedEvent} is the event which is published when permission to a command is revoked for a
 * userIdentity.
 */
public final class PermissionRevokedEvent {

    private final ChargingStationId chargingStationId;

    private final UserIdentity userIdentity;

    private final Class commandClass;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code PermissionRevokedEvent} with an identifier, userIdentity, commandClass and identityContext.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param userIdentity the identifier of the user that is granted permission.
     * @param commandClass the command for which permission is granted.
     * @param identityContext the identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code userIdentity}, {@code commandClass} or {@code identityContext} is {@code null}.
     */
    public PermissionRevokedEvent(ChargingStationId chargingStationId, UserIdentity userIdentity, Class commandClass, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.userIdentity = checkNotNull(userIdentity);
        this.commandClass = checkNotNull(commandClass);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * Gets the user identity.
     *
     * @return the user identity.
     */
    public UserIdentity getUserIdentity() {
        return userIdentity;
    }

    /**
     * Gets the command class.
     *
     * @return the command class.
     */
    public Class getCommandClass() {
        return commandClass;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }
}
