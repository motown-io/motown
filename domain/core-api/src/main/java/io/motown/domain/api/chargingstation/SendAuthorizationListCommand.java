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
     * @param chargingStationId the identifier of the charging station.
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SendAuthorizationListCommand that = (SendAuthorizationListCommand) o;

        if (authorizationListVersion != that.authorizationListVersion) return false;
        if (authorizationList != null ? !authorizationList.equals(that.authorizationList) : that.authorizationList != null)
            return false;
        if (authorizationListHash != null ? !authorizationListHash.equals(that.authorizationListHash) : that.authorizationListHash != null)
            return false;
        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (updateType != that.updateType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + (authorizationList != null ? authorizationList.hashCode() : 0);
        result = 31 * result + authorizationListVersion;
        result = 31 * result + (authorizationListHash != null ? authorizationListHash.hashCode() : 0);
        result = 31 * result + updateType.hashCode();
        return result;
    }
}
