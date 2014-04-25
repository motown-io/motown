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

import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ConfigurationItemsReceivedEvent} is the event which is published when Motown has received configuration
 * items from a charging station. This is the complete list of configuration items a charging station has so any locally
 * stored configuration items need to be completely overwritten.
 */
public class ConfigurationItemsReceivedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final Set<ConfigurationItem> configurationItems;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ConfigurationItemsReceivedEvent} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code identityContext} is {@code null}.
     */
    public ConfigurationItemsReceivedEvent(ChargingStationId chargingStationId, Set<ConfigurationItem> configurationItems, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.configurationItems = ImmutableSet.copyOf(checkNotNull(configurationItems));
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
     * Gets the configuration items received from the charging station.
     *
     * @return an immutable {@link java.util.Map} of configuration items.
     */
    public Set<ConfigurationItem> getConfigurationItems() {
        return configurationItems;
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
        return Objects.hash(chargingStationId, configurationItems, identityContext);
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
        final ConfigurationItemsReceivedEvent other = (ConfigurationItemsReceivedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.configurationItems, other.configurationItems) && Objects.equals(this.identityContext, other.identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("configurationItems", configurationItems)
                .add("identityContext", identityContext)
                .toString();
    }
}
