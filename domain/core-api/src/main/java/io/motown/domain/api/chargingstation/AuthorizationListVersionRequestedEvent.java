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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code AuthorizationListVersionRequestedEvent} is the event which is published when a request has been made to
 * retrieve the version of the charging stations authorization list. Protocol add-ons should respond to this event (if
 * applicable) and request a charging station for its authorization list version.
 */
public final class AuthorizationListVersionRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code AuthorizationListVersionRequestedEvent} with an identifier and a protocol.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol} or {@code identityContext} is {@code null}.
     */
    public AuthorizationListVersionRequestedEvent(ChargingStationId chargingStationId, String protocol, IdentityContext identityContext) {
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

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, protocol, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AuthorizationListVersionRequestedEvent other = (AuthorizationListVersionRequestedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.identityContext, other.identityContext);
    }
}
