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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The command to trigger the retrieval of the ChargingStation's configuration
 */
public class RequestConfigurationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    public RequestConfigurationCommand(ChargingStationId chargingStationId) {
        checkArgument(chargingStationId != null);

        this.chargingStationId = chargingStationId;
    }

    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

}
