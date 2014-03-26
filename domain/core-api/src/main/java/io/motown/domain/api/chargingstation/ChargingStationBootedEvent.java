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

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.identity.IdentityContext;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChargingStationBootedEvent} is the event which is published when a charging station has booted.
 */
public abstract class ChargingStationBootedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final Map<String, String> attributes;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ChargingStationBootedEvent} with an identifier and a {@link java.util.Map} of attributes.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol protocol identifier.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information provided by
     *                          the charging station when it booted but which are not required by Motown. Because
     *                          {@link java.util.Map} implementations are potentially mutable a defensive copy is made.
     * @param identityContext the identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@protocol} or {@code attributes} is {@code null}.
     */
    public ChargingStationBootedEvent(ChargingStationId chargingStationId, String protocol, Map<String, String> attributes, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * Gets the protocol identifier.
     *
     * @return the protocol identifier.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the attributes associated with the boot.
     * <p/>
     * These attributes are additional information provided by the charging station when it booted but which are not
     * required by Motown.
     *
     * @return an immutable {@link java.util.Map} of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
}
