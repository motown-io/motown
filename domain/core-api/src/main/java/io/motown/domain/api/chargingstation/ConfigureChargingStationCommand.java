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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ConfigureChargingStationCommand} is the command which is published when a charging station should be
 * configured.
 */
public class ConfigureChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final Set<Connector> connectors;

    private final Map<String, String> settings;

    /**
     * Creates a {@code ConfigureChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param connectors        the connectors with which the charging station should be configured.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, Set<Connector> connectors) {
        this(chargingStationId, connectors, Collections.<String, String>emptyMap());
    }

    /**
     * Creates a {@code ConfigureChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param settings the settings with which the charging station should be configured.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, Map<String, String> settings) {
        this(chargingStationId, Collections.<Connector>emptySet(), settings);
    }

    /**
     * Creates a {@code ConfigureChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param connectors the connectors with which the charging station should be configured.
     * @param settings the settings with which the charging station should be configured.
     * @throws NullPointerException if {@code chargingStationId}, {@code connectors}, or {@code settings} is
     * {@code null}.
     */
    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, Set<Connector> connectors, Map<String, String> settings) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.connectors = ImmutableSet.copyOf(checkNotNull(connectors));
        this.settings = ImmutableMap.copyOf(checkNotNull(settings));
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
     * Gets the connectors with which the charging station should be configured.
     *
     * @return an immutable {@link java.util.Set} of connectors.
     */
    public Set<Connector> getConnectors() {
        return connectors;
    }

    /**
     * Gets the configuration items with which the charging station should be configured.
     *
     * These configuration items are additional information provided with which the charging station should be
     * configured but which are not required by Motown.
     *
     * @return an immutable {@link java.util.Map} of configuration items.
     */
    public Map<String, String> getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("chargingStationId", chargingStationId)
                .add("connectors", connectors)
                .add("settings", settings)
                .toString();
    }
}
