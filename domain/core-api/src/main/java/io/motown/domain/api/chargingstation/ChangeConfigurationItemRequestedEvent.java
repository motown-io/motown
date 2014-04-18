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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChangeConfigurationItemRequestedEvent} is the event which is published when a configuration change has been
 * requested. Protocol add-ons should respond to this event (if applicable) and request a charging station for its
 * configuration items.
 */
public final class ChangeConfigurationItemRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final ConfigurationItem configurationItem;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ChangeConfigurationItemRequestedEvent} with an identifier, vendor identifier, message identifier, free format data and identity context.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol identifier.
     * @param configurationItem the configuration item to change.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code configurationItem}, or
     * {@code identityContext} is {@code null}.
     */
    public ChangeConfigurationItemRequestedEvent(ChargingStationId chargingStationId, String protocol, ConfigurationItem configurationItem, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.configurationItem = checkNotNull(configurationItem);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the protocol identifier
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Gets the configuration item.
     *
     * @return the configuration item.
     */
    public ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, protocol, configurationItem, identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChangeConfigurationItemRequestedEvent other = (ChangeConfigurationItemRequestedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.configurationItem, other.configurationItem) && Objects.equals(this.identityContext, other.identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("protocol", protocol)
                .add("configurationItem", configurationItem)
                .add("identityContext", identityContext)
                .toString();
    }
}
