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
package io.motown.ocpp.websocketjson.request.chargingstation;

import java.util.Date;

public class StartTransactionRequest {

    private int connectorId;

    private String idTag;

    private Date timestamp;

    private int meterStart;

    private int reservationId;

    public StartTransactionRequest(int connectorId, String idTag, Date timestamp, int meterStart, int reservationId) {
        this.connectorId = connectorId;
        this.idTag = idTag;
        this.timestamp = timestamp;
        this.meterStart = meterStart;
        this.reservationId = reservationId;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public String getIdTag() {
        return idTag;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getMeterStart() {
        return meterStart;
    }

    public int getReservationId() {
        return reservationId;
    }
}
