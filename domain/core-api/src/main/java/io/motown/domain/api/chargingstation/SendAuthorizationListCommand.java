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

import com.google.common.collect.ImmutableList;
import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code SendAuthorizationListCommand} is the command which is published in order to update the local authorization
 * list of the charging station.
 */
public final class SendAuthorizationListCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final List<IdentifyingToken> authorizationList;

    private final int authorizationListVersion;

    private final String authorizationListHash;

    private final AuthorizationListUpdateType updateType;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code SendAuthorizationListCommand}
     *
     * @param chargingStationId        the identifier of the charging station.
     * @param authorizationList        in case of a full update this contains the list of values that form the new local
     *                                 authorization list. In case of a differential update it contains the changes to be
     *                                 applied to the local authorization list in the charging station.
     * @param updateType               the type of update.
     * @param authorizationListVersion in case of a full update this is the version number of the full list. In case of
     *                                 a differential update it is the version number of the list after the update has been applied.
     * @param authorizationListHash    hash value calculated over the contents of the list.
     * @param identityContext          identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code authorizationList}, {@code authorizationListHash},
     *                                 {@code updateType} or {@code identityContext} is {@code null}.
     */
    public SendAuthorizationListCommand(ChargingStationId chargingStationId, List<IdentifyingToken> authorizationList, int authorizationListVersion,
                                        String authorizationListHash, AuthorizationListUpdateType updateType, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.authorizationList = ImmutableList.copyOf(checkNotNull(authorizationList));
        this.authorizationListVersion = authorizationListVersion;
        this.authorizationListHash = checkNotNull(authorizationListHash);
        this.updateType = checkNotNull(updateType);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * In case of a full update this contains the list of values that form the new local authorization list. In case of
     * a differential update it contains the changes to be applied to the local authorization list in the charging station.
     *
     * @return list of identifying tokens.
     */
    public List<IdentifyingToken> getAuthorizationList() {
        return authorizationList;
    }

    /**
     * In case of a full update this is the version number of the full list. In case of a differential update it is the
     * version number of the list after the update has been applied.
     *
     * @return list version.
     */
    public int getAuthorizationListVersion() {
        return authorizationListVersion;
    }

    /**
     * Hash value calculated over the contents of the list.
     *
     * @return hash value.
     */
    public String getAuthorizationListHash() {
        return authorizationListHash;
    }

    /**
     * Type of the update.
     *
     * @return update type.
     */
    public AuthorizationListUpdateType getUpdateType() {
        return updateType;
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
        return Objects.hash(chargingStationId, authorizationList, authorizationListVersion, authorizationListHash, updateType, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final SendAuthorizationListCommand other = (SendAuthorizationListCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.authorizationList, other.authorizationList) && Objects.equals(this.authorizationListVersion, other.authorizationListVersion) && Objects.equals(this.authorizationListHash, other.authorizationListHash) && Objects.equals(this.updateType, other.updateType) && Objects.equals(this.identityContext, other.identityContext);
    }
}
