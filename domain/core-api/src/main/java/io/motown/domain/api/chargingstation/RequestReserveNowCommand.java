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

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestReserveNowCommand} is the command which is published when a evse should be
 * reserved.
 */
public final class RequestReserveNowCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final EvseId evseId;

    private final IdentifyingToken identifyingToken;

    private final Date expiryDate;

    private final IdentifyingToken parentIdentifyingToken;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestReserveNowCommand} with an identifier.
     *
     * @param chargingStationId      the identifier of the charging station.
     * @param evseId                 the identifier of the EVSE.
     * @param identifyingToken       the identifying token that will fulfill the reservation.
     * @param expiryDate             date at which the reservation should expire.
     * @param parentIdentifyingToken group of the identifying token.
     * @param identityContext        identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code evseId}, {@code identifyingToken}, {@code expiryDate}
     *                               or {@code identityContext} is {@code null}.
     */
    public RequestReserveNowCommand(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, Date expiryDate,
                                    @Nullable IdentifyingToken parentIdentifyingToken, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.expiryDate = new Date(checkNotNull(expiryDate).getTime());
        this.parentIdentifyingToken = parentIdentifyingToken;
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
     * The identifier of the EVSE.
     *
     * @return EVSE identifier.
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * Identifying token which will fulfill the reservation.
     *
     * @return identifying token.
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    /**
     * Date at which the reservation will expire.
     *
     * @return expiration date.
     */
    public Date getExpiryDate() {
        return new Date(expiryDate.getTime());
    }

    /**
     * Group of the identifying token.
     *
     * @return parent identifying token.
     */
    @Nullable
    public IdentifyingToken getParentIdentifyingToken() {
        return parentIdentifyingToken;
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
        return Objects.hash(chargingStationId, evseId, identifyingToken, expiryDate, parentIdentifyingToken, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RequestReserveNowCommand other = (RequestReserveNowCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.evseId, other.evseId) && Objects.equals(this.identifyingToken, other.identifyingToken) && Objects.equals(this.expiryDate, other.expiryDate) && Objects.equals(this.parentIdentifyingToken, other.parentIdentifyingToken) && Objects.equals(this.identityContext, other.identityContext);
    }
}
