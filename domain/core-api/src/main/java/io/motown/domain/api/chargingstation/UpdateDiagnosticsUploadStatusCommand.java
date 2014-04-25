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
 * {@code UpdateDiagnosticsUploadStatusCommand} is the command which is published upon receipt of the status
 * notification the charging station sends when the upload has finished or not.
 */
public final class UpdateDiagnosticsUploadStatusCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final boolean isUploaded;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code UpdateDiagnosticsUploadStatusCommand} with an identifier, an indicator if the file has
     * been successfully uploaded or not and identity context.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param isUploaded        the status of the upload, in case false the upload failed
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code identityContext} is {@code null}.
     */
    public UpdateDiagnosticsUploadStatusCommand(ChargingStationId chargingStationId, boolean isUploaded, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.isUploaded = isUploaded;
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
     * @return boolean indicating if the upload has succeeded or not
     */
    public boolean isUploaded() {
        return isUploaded;
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
        return Objects.hash(chargingStationId, isUploaded, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UpdateDiagnosticsUploadStatusCommand other = (UpdateDiagnosticsUploadStatusCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.isUploaded, other.isUploaded) && Objects.equals(this.identityContext, other.identityContext);
    }
}
