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
package io.motown.ocpp.viewmodel.domain;

import java.util.Date;

/**
 * Contains values which reflect the result of a 'boot charging station' command.
 */
public class BootChargingStationResult {

    /**
     * @param accepted whether the charging station has been accepted by the central system
     * @param heartbeatInterval the interval for heartbeats in seconds
     * @param timeStamp current time of the server
     */
    public BootChargingStationResult(boolean accepted, int heartbeatInterval, Date timeStamp) {
        this.accepted = accepted;
        this.heartbeatInterval = heartbeatInterval;
        this.timeStamp = timeStamp;
    }

    /**
     * Whether the charging station has been accepted by the central system
     */
    public boolean accepted;

    /**
     * The interval for heartbeats in seconds
     */
    public int heartbeatInterval;

    /**
     * Current time of the server, used by the charging station to synchronize its local time
     */
    public Date timeStamp;

}
