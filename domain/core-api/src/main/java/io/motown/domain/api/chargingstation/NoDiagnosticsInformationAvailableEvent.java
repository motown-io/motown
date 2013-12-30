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
 * {@code NoDiagnosticsInformationAvailableEvent} is the event which is published when no diagnostics information is
 * available after requesting the diagnostics information.
 */
public final class NoDiagnosticsInformationAvailableEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    /**
     * Creates a {@code NoDiagnosticsInformationAvailableEvent}.
     *
     * @param chargingStationId     the identifier of the charging station.
     *
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public NoDiagnosticsInformationAvailableEvent(ChargingStationId chargingStationId) {
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

}
