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
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DiagnosticsFileNameReceivedEvent} is the event which is published when a diagnostics filename is received,
 * indicating that the charging station has diagnostics that it will upload.
 */
public final class DiagnosticsFileNameReceivedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String diagnosticsFileName;

    /**
     * Creates a {@code DiagnosticsFileNameReceivedEvent} with an identifier and a diagnostics file name.
     *
     * @param chargingStationId     the identifier of the charging station.
     * @param diagnosticsFileName   the filename of the diagnostics file used by the charging station to upload the diagnostics information
     *
     * @throws NullPointerException if {@code chargingStationId} or {@code diagnosticsFileName} is {@code null}.
     * @throws IllegalArgumentException if {@code diagnosticsFileName} is empty.
     */
    public DiagnosticsFileNameReceivedEvent(ChargingStationId chargingStationId, String diagnosticsFileName) {
        this.chargingStationId = checkNotNull(chargingStationId);

        checkNotNull(diagnosticsFileName);
        checkArgument(!diagnosticsFileName.isEmpty());
        this.diagnosticsFileName = diagnosticsFileName;
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
     * @return the filename of the diagnostics file used by the charging station to upload the diagnostics information
     */
    public String getDiagnosticsFileName() {
        return diagnosticsFileName;
    }
}
