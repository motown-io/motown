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

import java.util.Date;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ReserveNowRequestedEvent} is the event which is published when a request has been made to reserve a charging
 * station. Protocol add-ons should respond to this event (if applicable) and request a charging station to reserve.
 */
public final class ReserveNowRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final EvseId evseId;

    private final IdentifyingToken identifyingToken;

    private final Date expiryDate;

    private final IdentifyingToken parentIdentifyingToken;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ReserveNowRequestedEvent}.
     *
     * @param chargingStationId      the charging station identifier.
     * @param protocol               the protocol identifier.
     * @param evseId                 identifier of the EVSE.
     * @param identifyingToken       identifier of the token that has reserved the charging station.
     * @param expiryDate             date at which the reservation expires.
     * @param parentIdentifyingToken parent identifier that has reserved the charging station.
     * @param identityContext        the identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code evseId}, {@code identifyingToken},
     *                              {@code expiryDate}, {@code parentIdentifyingToken} or {@code identityContext} is {@code null}.
     */
    public ReserveNowRequestedEvent(ChargingStationId chargingStationId, String protocol, EvseId evseId, IdentifyingToken identifyingToken,
                                    Date expiryDate, IdentifyingToken parentIdentifyingToken, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
        this.evseId = checkNotNull(evseId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.expiryDate = new Date(checkNotNull(expiryDate).getTime());
        this.parentIdentifyingToken = parentIdentifyingToken;
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station's identifier.
     *
     * @return the charging station's identifier.
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
        return Objects.hash(chargingStationId, protocol, evseId, identifyingToken, expiryDate, parentIdentifyingToken, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ReserveNowRequestedEvent other = (ReserveNowRequestedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.evseId, other.evseId) && Objects.equals(this.identifyingToken, other.identifyingToken) && Objects.equals(this.expiryDate, other.expiryDate) && Objects.equals(this.parentIdentifyingToken, other.parentIdentifyingToken) && Objects.equals(this.identityContext, other.identityContext);
    }
}
