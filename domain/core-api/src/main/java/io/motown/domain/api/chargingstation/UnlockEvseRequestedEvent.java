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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Published when the unlock of a EVSE is requested. Protocol add-ons should respond to this event (if applicable) and
 * request a charging station to unlock an EVSE.
 */
public final class UnlockEvseRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final EvseId evseId;

    private final IdentityContext identityContext;

    /**
     * Constructs a {@code UnlockEvseRequestedEvent} with charging station identifier, protocol, Evse and identity context.
     *
     * @param chargingStationId charging station's identifier.
     * @param protocol          protocol identifier
     * @param evseId            evse's identifier or position.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code evseId} or {@code identityContext} is {@code null}.
     */
    public UnlockEvseRequestedEvent(ChargingStationId chargingStationId, String protocol, EvseId evseId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
        this.evseId = checkNotNull(evseId);
        this.identityContext = checkNotNull(identityContext);
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
     * Gets the evse's identifier or position.
     *
     * @return the evse's identifier or position.
     */
    public EvseId getEvseId() {
        return evseId;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }
}
