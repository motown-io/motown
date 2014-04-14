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

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestConfigurationCommand} is the event which is published when a charging station's configuration should
 * be requested from the charging station.
 */
public final class ConfigurationRequestedEvent implements CommunicationWithChargingStationRequestedEvent{

    private final ChargingStationId chargingStationId;

    private final List<String> keys;

    private final String protocol;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ConfigurationRequestedEvent} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param keys              an optional list of keys to retrieve, or all keys in case this list is empty
     * @param protocol protocol identifier.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol} or {@code identityContext} is {@code null}.
     */
    public ConfigurationRequestedEvent(ChargingStationId chargingStationId, List<String> keys, String protocol, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.keys = checkNotNull(keys);
        this.protocol = checkNotNull(protocol);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * The optional list of keys to be retrieved
     *
     * @return optional list of keys
     */
    public List<String> getKeys() {
        return keys;
    }

    /**
     * Gets the protocol identifier.
     *
     * @return the protocol identifier.
     */
    @Override
    public String getProtocol() {
        return protocol;
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
