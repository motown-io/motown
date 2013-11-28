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
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Collections;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ConfigureChargingStationCommand} is the command which is published when a charging station should be
 * configured.
 */
public class ConfigureChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final int numberOfConnectors;

    private final Map<String, String> configurationItems;

    /**
     * Creates a {@code ConfigureChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param numberOfConnectors the number of connectors for this charging station.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, int numberOfConnectors) {
        this(chargingStationId, numberOfConnectors, Collections.<String, String>emptyMap());
    }

    /**
     * Creates a {@code ConfigureChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param configurationItems the configurationItems with which the charging station should be configured.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, Map<String, String> configurationItems) {
        this(chargingStationId, 0, configurationItems);
    }

    /**
     * Creates a {@code ConfigureChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param numberOfConnectors the number of connectors for this charging station.
     * @param configurationItems the configurationItems with which the charging station should be configured.
     * @throws NullPointerException if {@code chargingStationId}, {@code connectors}, or {@code configurationItems} is
     * {@code null}.
     */
    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, int numberOfConnectors, Map<String, String> configurationItems) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.numberOfConnectors = numberOfConnectors;
        this.configurationItems = ImmutableMap.copyOf(checkNotNull(configurationItems));
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
     * Gets the number of connectors with which the charging station should be configured.
     *
     * @return the number of connectors
     */
    public int getNumberOfConnectors() {
        return this.numberOfConnectors;
    }

    /**
     * Gets the configuration items with which the charging station should be configured.
     *
     * These configuration items are additional information provided with which the charging station should be
     * configured but which are not required by Motown.
     *
     * @return an immutable {@link java.util.Map} of configuration items.
     */
    public Map<String, String> getConfigurationItems() {
        return configurationItems;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("chargingStationId", chargingStationId)
                .add("connectors", numberOfConnectors)
                .add("configurationItems", configurationItems)
                .toString();
    }
}
