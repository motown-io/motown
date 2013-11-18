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
 * The Boot command contains all information about a chargepoint.
 * When the point does not exist (yet), you have the option to create it.
 */
public class BootChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final Map<String, String> attributes;

    public BootChargingStationCommand(ChargingStationId chargingStationId) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.attributes = ImmutableMap.of();
    }

    public BootChargingStationCommand(ChargingStationId chargingStationId, Map<String, String> attributes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.attributes = ImmutableMap.copyOf(attributes);
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    public Map<String, String> getAttributes() {
        return ImmutableMap.copyOf(attributes);
    }
}
