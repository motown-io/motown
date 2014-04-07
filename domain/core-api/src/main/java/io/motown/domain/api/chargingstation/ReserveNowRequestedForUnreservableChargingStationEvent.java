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

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ReserveNowRequestedForUnreservableChargingStationEvent} is thrown when an attempt is made to reserve a
 * charging station which is not reservable.
 */
public final class ReserveNowRequestedForUnreservableChargingStationEvent {

    private final ChargingStationId chargingStationId;

    private final EvseId evseId;

    private final IdentifyingToken identifyingToken;

    private final Date expiryDate;

    private final IdentifyingToken parentIdentifyingToken;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ReserveNowRequestedForUnreservableChargingStationEvent} with a charging station identifier, evse
     * identifier, identifying token, expiry date and parent identifying token.
     *
     * @param chargingStationId         charging station identifier.
     * @param evseId                    identifier of the EVSE.
     * @param identifyingToken          identifier of the token that would have reserved the charging station.
     * @param expiryDate                date at which the reservation would expire.
     * @param parentIdentifyingToken    parent identifier that would have reserved the charging station.
     * @param identityContext           the identity context.
     * @throws NullPointerException     if {@code chargingStationId}, {@code evseId}, {@code identifyingToken}, {@code expiryDate}
     *                                  or {@code identityContext} is null.
     */
    public ReserveNowRequestedForUnreservableChargingStationEvent(ChargingStationId chargingStationId, EvseId evseId,
                             IdentifyingToken identifyingToken, Date expiryDate, @Nullable IdentifyingToken parentIdentifyingToken, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.expiryDate = new Date(checkNotNull(expiryDate).getTime());
        this.parentIdentifyingToken = parentIdentifyingToken;
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * {@inheritDoc}
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Identifier of the EVSE.
     *
     * @return EVSE identifier.
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * Identifier of the token that has reserved the charging station.
     *
     * @return identifying token.
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    /**
     * Date at which the reservation expires.
     *
     * @return expiry date.
     */
    public Date getExpiryDate() {
        return new Date(expiryDate.getTime());
    }

    /**
     * Parent identifier that has reserved the charging station.
     *
     * @return parent identifying token.
     */
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
        final ReserveNowRequestedForUnreservableChargingStationEvent other = (ReserveNowRequestedForUnreservableChargingStationEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.evseId, other.evseId) && Objects.equals(this.identifyingToken, other.identifyingToken) && Objects.equals(this.expiryDate, other.expiryDate) && Objects.equals(this.parentIdentifyingToken, other.parentIdentifyingToken) && Objects.equals(this.identityContext, other.identityContext);
    }
}
