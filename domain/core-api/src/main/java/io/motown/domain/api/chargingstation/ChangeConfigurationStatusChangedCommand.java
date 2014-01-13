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

/**
 * {@code ChangeConfigurationStatusChangedCommand} is the command which is published to inform about the resulting status of the
 * change configuration request that has been sent to the charging station.
 */
public final class ChangeConfigurationStatusChangedCommand extends StatusChangedCommand {
    /**
     * Creates a {@code StatusChangedCommand} with an identifier and new status.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param status            the resulting status of the request
     * @throws NullPointerException if {@code chargingStationId} or {@code status} is {@code null}.
     */
    public ChangeConfigurationStatusChangedCommand(ChargingStationId chargingStationId, RequestStatus status) {
        super(chargingStationId, status);
    }
}
