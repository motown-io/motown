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

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code SendAuthorisationListRequestedEvent} is the event which is published when a new authorisation list (or update
 * of the list) should be synchronised towards the charging station.
 */
public final class SendAuthorisationListRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final List<IdentifyingToken> authorisationList;

    private final int authorisationListVersion;

    private final String authorisationListHash;

    private final AuthorisationListUpdateType updateType;

    /**
     * Creates a {@code SendAuthorisationListRequestedEvent}.
     * @param chargingStationId         the charging station identifier
     * @param protocol                  the protocol identifier
     * @param authorisationList         the list of IdentifyingTokens to be updated/added
     * @param authorisationListVersion  the authorisation list version identifier
     * @param authorisationListHash     the optional hash calculated over the elements in the list
     * @param updateType                the update type
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code updateType} is {@code null}.
     * @throws IllegalArgumentException if {@code protocol} is empty.
     */
    public SendAuthorisationListRequestedEvent(ChargingStationId chargingStationId, String protocol, List<IdentifyingToken> authorisationList, int authorisationListVersion, String authorisationListHash, AuthorisationListUpdateType updateType) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.authorisationList = authorisationList;
        this.authorisationListVersion = authorisationListVersion;
        this.authorisationListHash = authorisationListHash;
        this.updateType = checkNotNull(updateType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the authorisation list that is to be applied
     */
    public List<IdentifyingToken> getAuthorisationList() {
        return authorisationList;
    }

    /**
     * @return the list of IdentifyingTokens to be updated/added
     */
    public AuthorisationListUpdateType getUpdateType() {
        return updateType;
    }

    /**
     * @return the authorisation list version identifier
     */
    public int getAuthorisationListVersion() {
        return authorisationListVersion;
    }

    /**
     * @return the optional hash calculated over the elements in the list
     */
    public String getAuthorisationListHash() {
        return authorisationListHash;
    }
}
