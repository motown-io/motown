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

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code SendAuthorizationListRequestedEvent} is the event which is published when a new authorization list (or update
 * of the list) should be synchronised towards the charging station.
 */
public final class SendAuthorizationListRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final List<IdentifyingToken> authorizationList;

    private final int authorizationListVersion;

    private final String authorizationListHash;

    private final AuthorizationListUpdateType updateType;

    /**
     * Creates a {@code SendAuthorizationListRequestedEvent}.
     * @param chargingStationId         the charging station identifier
     * @param protocol                  the protocol identifier
     * @param authorizationList         the list of IdentifyingTokens to be updated/added
     * @param authorizationListVersion  the authorization list version identifier
     * @param authorizationListHash     the optional hash calculated over the elements in the list
     * @param updateType                the update type
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code updateType} is {@code null}.
     * @throws IllegalArgumentException if {@code protocol} is empty.
     */
    public SendAuthorizationListRequestedEvent(ChargingStationId chargingStationId, String protocol, List<IdentifyingToken> authorizationList, int authorizationListVersion, String authorizationListHash, AuthorizationListUpdateType updateType) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.authorizationList = ImmutableList.copyOf(checkNotNull(authorizationList));
        this.authorizationListVersion = checkNotNull(authorizationListVersion);
        this.authorizationListHash = checkNotNull(authorizationListHash);
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
     * @return the authorization list that is to be applied
     */
    public List<IdentifyingToken> getAuthorizationList() {
        return authorizationList;
    }

    /**
     * @return the list of IdentifyingTokens to be updated/added
     */
    public AuthorizationListUpdateType getUpdateType() {
        return updateType;
    }

    /**
     * @return the authorization list version identifier
     */
    public int getAuthorizationListVersion() {
        return authorizationListVersion;
    }

    /**
     * @return the optional hash calculated over the elements in the list
     */
    public String getAuthorizationListHash() {
        return authorizationListHash;
    }
}
