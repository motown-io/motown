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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestChangeConfigurationItemCommand} is the command which is published when a single configuration parameter
 * is to be changed on the charging station. This command will lead to communication with the charging station.
 */
public final class RequestChangeConfigurationItemCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final ConfigurationItem configurationItem;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestChangeConfigurationItemCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param configurationItem the configuration item to change.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code configurationItem}, or
     *                              {@code identityContext} is {@code null}.
     */
    public RequestChangeConfigurationItemCommand(ChargingStationId chargingStationId, ConfigurationItem configurationItem, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.configurationItem = checkNotNull(configurationItem);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station's id.
     *
     * @return the charging station's id.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the configuration item.
     *
     * @return the configuration item to change.
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
        return Objects.hash(chargingStationId, configurationItem, identityContext);
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
        final RequestChangeConfigurationItemCommand other = (RequestChangeConfigurationItemCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.configurationItem, other.configurationItem) && Objects.equals(this.identityContext, other.identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("configurationItem", configurationItem)
                .add("identityContext", identityContext)
                .toString();
    }
}
