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

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Event which is published when a single configuration item has changed on the charging station. Listeners should not
 * overwrite all of their locally stored configuration item, only update or add the one enclosed in this event.
 */
public class ConfigurationItemChangedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final ConfigurationItem configurationItem;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ConfigurationItemChangedEvent}
     *
     * @param chargingStationId the charging station's id.
     * @param configurationItem the configuration item.
     */
    public ConfigurationItemChangedEvent(ChargingStationId chargingStationId, ConfigurationItem configurationItem, IdentityContext identityContext) {
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
        return Objects.hash(chargingStationId, configurationItem);
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
        final ConfigurationItemChangedEvent other = (ConfigurationItemChangedEvent) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.configurationItem, other.configurationItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("configurationItem", configurationItem)
                .toString();
    }
}
