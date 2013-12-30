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
 * {@code UpdateFirmwareStatusCommand} is the command which is published upon receipt of the status
 * notification on the firmware update process.
 */
public final class UpdateFirmwareStatusCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final FirmwareStatus status;

    /**
     * Creates a {@code UpdateDiagnosticsUploadStatusCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param status            the status of the firmware update
     * @throws NullPointerException if {@code chargingStationId} or {@code status} is {@code null}.
     */
    public UpdateFirmwareStatusCommand(ChargingStationId chargingStationId, FirmwareStatus status) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.status = checkNotNull(status);
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
     * @return the firmware status
     */
    public FirmwareStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateFirmwareStatusCommand that = (UpdateFirmwareStatusCommand) o;

        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }
}
