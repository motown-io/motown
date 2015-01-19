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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestDiagnosticsCommand} is the command which is published when a charging station's diagnostics information
 * is requested.
 */
public final class RequestDiagnosticsCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final DiagnosticsUploadSettings diagnosticsUploadSettings;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestDiagnosticsCommand}.
     *
     * @param chargingStationId the charging station identifier.
     * @param diagnosticsUploadSettings the settings for the diagnostics upload.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code diagnosticsUploadSettings} or {@code identityContext} is {@code null}.
     */
    public RequestDiagnosticsCommand(ChargingStationId chargingStationId, DiagnosticsUploadSettings diagnosticsUploadSettings,
                                     IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.diagnosticsUploadSettings = checkNotNull(diagnosticsUploadSettings);
        this.identityContext = checkNotNull(identityContext);
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
     * Gets the diagnostics upload settings.
     *
     * @return the diagnostics upload settings.
     */
    public DiagnosticsUploadSettings getDiagnosticsUploadSettings() {
        return diagnosticsUploadSettings;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

}
