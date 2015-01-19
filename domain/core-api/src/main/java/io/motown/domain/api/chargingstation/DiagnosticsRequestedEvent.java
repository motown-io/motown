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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DiagnosticsRequestedEvent} is the event which is published when a charging station's diagnostics are
 * requested. Protocol add-ons should respond to this event (if applicable) and request a charging station to send its
 * diagnostics.
 */
public final class DiagnosticsRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final DiagnosticsUploadSettings diagnosticsUploadSettings;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DiagnosticsRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol the charging station supports.
     * @param diagnosticsUploadSettings the settings for the diagnostics upload.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code diagnosticsUploadSettings} or
     *                              {@code identityContext} is {@code null}.
     */
    public DiagnosticsRequestedEvent(ChargingStationId chargingStationId, String protocol,
                                     DiagnosticsUploadSettings diagnosticsUploadSettings, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
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
     * @return the protocol identifier.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Gets the diagnostics upload settings.
     *
     * @return diagnostic upload settings.
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
