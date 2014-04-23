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

import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An {@code ChangeAuthorizationListCommand} is published when the local authorization list on the charging station has
 * been changed.
 */
public final class ChangeAuthorizationListCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final int version;

    private final AuthorizationListUpdateType updateType;

    private final List<IdentifyingToken> identifyingTokens;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ChangeAuthorizationListCommand}.
     *
     * @param chargingStationId the charging station's identifier.
     * @param version           the new version of the authorization list
     * @param updateType        the type of update that has been performed.
     * @param identifyingTokens the identifying tokens that have been changed.
     * @param identityContext   the identity context.
     * @throws NullPointerException     if {@code chargingStationId}, {@code evseId} or {@code identityContext} is {@code null}.
     */
    public ChangeAuthorizationListCommand(ChargingStationId chargingStationId, int version, AuthorizationListUpdateType updateType, List<IdentifyingToken> identifyingTokens, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.version = version;
        this.updateType = checkNotNull(updateType);
        this.identifyingTokens = checkNotNull(identifyingTokens);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station's identifier.
     *
     * @return the charging station's identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the version of the authorization list
     * @return the version of the authorization list
     */
    public int getVersion() {
        return version;
    }

    /**
     * Gets the update type that has been performed.
     * @return update type
     */
    public AuthorizationListUpdateType getUpdateType() {
        return updateType;
    }

    /**
     * Gets the list of identifying tokens that have been changed.
     * @return list of identifying tokens
     */
    public List<IdentifyingToken> getIdentifyingTokens() {
        return identifyingTokens;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, version, updateType, identifyingTokens, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChangeAuthorizationListCommand other = (ChangeAuthorizationListCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.version, other.version) && Objects.equals(this.updateType, other.updateType) && Objects.equals(this.identifyingTokens, other.identifyingTokens) && Objects.equals(this.identityContext, other.identityContext);
    }
}
