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

    /**
     * Creates a {@code SendAuthorizationListCommand}
     *
     * @param chargingStationId        the identifier of the charging station.
     * @param authorizationList
     * @param authorizationListVersion
     * @param authorizationListHash
     * @param updateType
     * @throws NullPointerException if {@code chargingStationId}, {@code authorizationList}, {@code authorizationListHash} or {@code updateType} is {@code null}.
     */
    public SendAuthorizationListCommand(ChargingStationId chargingStationId, List<IdentifyingToken> authorizationList, int authorizationListVersion, String authorizationListHash, AuthorizationListUpdateType updateType) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.authorizationList = checkNotNull(authorizationList);
        this.authorizationListVersion = authorizationListVersion;
        this.authorizationListHash = checkNotNull(authorizationListHash);
        this.updateType = checkNotNull(updateType);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public List<IdentifyingToken> getAuthorizationList() {
        return authorizationList;
    }

    public int getAuthorizationListVersion() {
        return authorizationListVersion;
    }

    public String getAuthorizationListHash() {
        return authorizationListHash;
    }

    public AuthorizationListUpdateType getUpdateType() {
        return updateType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, authorizationList, authorizationListVersion, authorizationListHash, updateType);
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
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.authorizationList, other.authorizationList) && Objects.equals(this.authorizationListVersion, other.authorizationListVersion) && Objects.equals(this.authorizationListHash, other.authorizationListHash) && Objects.equals(this.updateType, other.updateType);
    }
}
