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
 * {@code DiagnosticsFileNameReceivedCommand} is the command which is published upon receipt of the filename the charging
 * station will be using to upload the diagnostics to.
 */
public final class DiagnosticsFileNameReceivedCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String diagnosticsFileName;

    /**
     * Creates a {@code DiagnosticsFileNameReceivedCommand} with an identifier and a diagnostics file name.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param diagnosticsFileName the filename the charging station will be using to send over the diagnostics
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public DiagnosticsFileNameReceivedCommand(ChargingStationId chargingStationId, String diagnosticsFileName) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.diagnosticsFileName = diagnosticsFileName;
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
     * @return the diagnostics file name
     */
    public String getDiagnosticsFileName() {
        return diagnosticsFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiagnosticsFileNameReceivedCommand that = (DiagnosticsFileNameReceivedCommand) o;

        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (diagnosticsFileName != null ? !diagnosticsFileName.equals(that.diagnosticsFileName) : that.diagnosticsFileName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + (diagnosticsFileName != null ? diagnosticsFileName.hashCode() : 0);
        return result;
    }
}
