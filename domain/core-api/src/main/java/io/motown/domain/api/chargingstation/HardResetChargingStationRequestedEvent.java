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
 * {@code HardResetChargingStationRequestedEvent} is the event which is published when a request has been made to hard
 * reset a charging station. Protocol add-ons should respond to this event (if applicable) and request a charging
 * station to hard reset.
 */
public final class HardResetChargingStationRequestedEvent implements ResetChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code HardResetChargingStationRequestedEvent} with an identifier and a protocol.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol} or {@code identityContext} is {@code null}.
     */
    public HardResetChargingStationRequestedEvent(ChargingStationId chargingStationId, String protocol, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProtocol() {
        return this.protocol;
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
