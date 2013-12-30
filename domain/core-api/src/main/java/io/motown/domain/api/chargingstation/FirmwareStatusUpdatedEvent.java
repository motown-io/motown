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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code FirmwareStatusUpdatedEvent} is the event which is published when a firmware status update is
 * received.
 */
public final class FirmwareStatusUpdatedEvent {

    private final ChargingStationId chargingStationId;

    private final FirmwareStatus firmwareStatus;

    /**
     * Creates a {@code FirmwareStatusUpdatedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param firmwareStatus    the status of the firmware update.
     * @throws NullPointerException if {@code chargingStationId} or {@code firmwareStatus} is {@code null}.
     */
    public FirmwareStatusUpdatedEvent(ChargingStationId chargingStationId, FirmwareStatus firmwareStatus) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.firmwareStatus = checkNotNull(firmwareStatus);
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
     * Gets the firmware status.
     *
     * @return the firmware status.
     */
    public FirmwareStatus getFirmwareStatus() {
        return firmwareStatus;
    }
}
