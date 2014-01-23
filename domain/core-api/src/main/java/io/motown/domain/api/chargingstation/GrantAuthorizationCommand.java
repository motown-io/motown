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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code GrantAuthorizationCommand} is the command which is published when a identifying token should be authorized
 * to continue processing.
 */
public final class GrantAuthorizationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final IdentifyingToken identifyingToken;

    /**
     * Creates a {@code GrantAuthorizationCommand} with an identifier and a identifying token.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param identifyingToken  the identification that has been authorized.
     * @throws NullPointerException if {@code chargingStationId} or {@code identifyingToken} is {@code null}.
     */
    public GrantAuthorizationCommand(ChargingStationId chargingStationId, IdentifyingToken identifyingToken) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.identifyingToken = checkNotNull(identifyingToken);
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
     * Gets the identification.
     *
     * @return the identification.
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrantAuthorizationCommand that = (GrantAuthorizationCommand) o;

        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (!identifyingToken.equals(that.identifyingToken)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + identifyingToken.hashCode();
        return result;
    }
}
