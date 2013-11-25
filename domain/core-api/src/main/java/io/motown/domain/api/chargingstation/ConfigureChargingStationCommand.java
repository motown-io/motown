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
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Configuration command holds the configuration information of a chargepoint.
 */
public class ConfigureChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final Map<String, String> configurationItems;

    public ConfigureChargingStationCommand(ChargingStationId chargingStationId, Map<String, String> configurationItems) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.configurationItems = ImmutableMap.copyOf(checkNotNull(configurationItems));
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    public Map<String, String> getConfigurationItems() {
        return configurationItems;
    }

    @Override
    public String toString() {
        return "ConfigureChargingStationCommand(chargingStationId=" + chargingStationId.toString() + ", configurationItems=" + configurationItems.toString() + ")";
    }
}
