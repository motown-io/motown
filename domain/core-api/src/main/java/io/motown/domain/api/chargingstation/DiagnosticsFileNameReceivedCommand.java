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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DiagnosticsFileNameReceivedCommand} is the command which is published upon receipt of the filename the charging
 * station will be using to upload the diagnostics to.
 */
public final class DiagnosticsFileNameReceivedCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String diagnosticsFileName;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DiagnosticsFileNameReceivedCommand} with an identifier and a diagnostics file name.
     *
     * @param chargingStationId   the identifier of the charging station.
     * @param diagnosticsFileName the filename the charging station will be using to send over the diagnostics (empty if
     *                            no file is being sent).
     * @param identityContext     identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code diagnosticsFileName} or {@code identityContext} is {@code null}.
     */
    public DiagnosticsFileNameReceivedCommand(ChargingStationId chargingStationId, String diagnosticsFileName, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.diagnosticsFileName = checkNotNull(diagnosticsFileName);
        this.identityContext = checkNotNull(identityContext);
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

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, diagnosticsFileName, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DiagnosticsFileNameReceivedCommand other = (DiagnosticsFileNameReceivedCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.diagnosticsFileName, other.diagnosticsFileName) && Objects.equals(this.identityContext, other.identityContext);
    }
}
