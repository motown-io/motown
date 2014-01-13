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

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code SendAuthorisationListCommand} is the command which is published in order to update the local authorisation
 * list of the charging station.
 */
public final class SendAuthorisationListCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final List<IdentifyingToken> authorisationList;

    private final int authorisationListVersion;

    private final String authorisationListHash;

    private final AuthorisationListUpdateType updateType;

    /**
     * Creates a {@code SendAuthorisationListCommand}
     *
     * @param chargingStationId the identifier of the charging station.
     * @param authorisationList
     * @param authorisationListVersion
     * @param authorisationListHash
     * @param updateType
     * @throws NullPointerException if {@code chargingStationId} or {@code updateType} is {@code null}.
     */
    public SendAuthorisationListCommand(ChargingStationId chargingStationId, @Nullable List<IdentifyingToken> authorisationList, int authorisationListVersion, @Nullable String authorisationListHash, AuthorisationListUpdateType updateType) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.authorisationList = authorisationList;
        this.authorisationListVersion = authorisationListVersion;
        this.authorisationListHash = authorisationListHash;
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

    @Nullable
    public List<IdentifyingToken> getAuthorisationList() {
        return authorisationList;
    }

    public int getAuthorisationListVersion() {
        return authorisationListVersion;
    }

    @Nullable
    public String getAuthorisationListHash() {
        return authorisationListHash;
    }

    public AuthorisationListUpdateType getUpdateType() {
        return updateType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SendAuthorisationListCommand that = (SendAuthorisationListCommand) o;

        if (authorisationListVersion != that.authorisationListVersion) return false;
        if (authorisationList != null ? !authorisationList.equals(that.authorisationList) : that.authorisationList != null)
            return false;
        if (authorisationListHash != null ? !authorisationListHash.equals(that.authorisationListHash) : that.authorisationListHash != null)
            return false;
        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (updateType != that.updateType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + (authorisationList != null ? authorisationList.hashCode() : 0);
        result = 31 * result + authorisationListVersion;
        result = 31 * result + (authorisationListHash != null ? authorisationListHash.hashCode() : 0);
        result = 31 * result + updateType.hashCode();
        return result;
    }
}
