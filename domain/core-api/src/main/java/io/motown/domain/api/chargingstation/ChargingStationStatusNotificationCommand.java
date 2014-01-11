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

import java.util.Date;
import java.util.Map;

/**
 * {@code ChargingStationStatusNotificationCommand} is the command which is published when a charging station notifies
 * Motown about its status.
 */
public final class ChargingStationStatusNotificationCommand extends StatusNotificationCommand {

    /**
     * Creates a {@code ChargingStationStatusNotificationCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param status            the status of the component
     * @param timeStamp         the optional date and time
     * @param attributes        optional attributes
     * @throws NullPointerException if {@code chargingStationId}, {@code status}, {@code timestamp} or {@code attributes} is {@code null}.
     */
    public ChargingStationStatusNotificationCommand(ChargingStationId chargingStationId, ComponentStatus status, Date timeStamp, Map<String, String> attributes) {
        super(chargingStationId, status, timeStamp, attributes);
    }
}
