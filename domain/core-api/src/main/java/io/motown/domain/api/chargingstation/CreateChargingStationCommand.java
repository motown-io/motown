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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code CreateChargingStationCommand} is the command which is published when a charging station should be created.
 */
public final class CreateChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    /**
     * Creates a {@code CreateChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public CreateChargingStationCommand(ChargingStationId chargingStationId) {
        this.chargingStationId = checkNotNull(chargingStationId);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateChargingStationCommand that = (CreateChargingStationCommand) o;

        if (!chargingStationId.equals(that.chargingStationId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return chargingStationId.hashCode();
    }
}
