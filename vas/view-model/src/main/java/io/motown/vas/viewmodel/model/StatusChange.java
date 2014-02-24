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
package io.motown.vas.viewmodel.model;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class StatusChange {

    private String chargingStationId;

    private Date timestamp;

    private ComponentStatus status;

    private int connectorsFree;

    /**
     * Create a status change object.
     *
     * @param chargingStationId charging station identifier.
     * @param timestamp timestamp of the change.
     * @param status new status.
     * @param connectorsFree amount of free connectors.
     * @throws NullPointerException if chargingStationId, timestamp or status is null.
     */
    public StatusChange(String chargingStationId, Date timestamp, ComponentStatus status, int connectorsFree) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
        this.status = checkNotNull(status);
        this.connectorsFree = connectorsFree;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ComponentStatus getStatus() {
        return status;
    }

    public int getConnectorsFree() {
        return connectorsFree;
    }
}
