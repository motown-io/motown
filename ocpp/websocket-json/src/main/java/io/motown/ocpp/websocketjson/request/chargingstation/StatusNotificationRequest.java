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

@Deprecated
public class StatusNotificationRequest {

    private int connectorId;

    private ChargePointStatus status;

    private ChargePointErrorCode errorCode;

    private String info;

    private Date timestamp;

    private String vendorId;

    private String vendorErrorCode;

    public StatusNotificationRequest(int connectorId, ChargePointStatus status, ChargePointErrorCode errorCode, String info, Date timestamp, String vendorId, String vendorErrorCode) {
        this.connectorId = connectorId;
        this.status = status;
        this.errorCode = errorCode;
        this.info = info;
        this.timestamp = timestamp != null ? new Date(timestamp.getTime()) : null;
        this.vendorId = vendorId;
        this.vendorErrorCode = vendorErrorCode;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public ChargePointStatus getStatus() {
        return status;
    }

    public ChargePointErrorCode getErrorCode() {
        return errorCode;
    }

    public String getInfo() {
        return info;
    }

    public Date getTimestamp() {
        return timestamp != null ? new Date(timestamp.getTime()) : null;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getVendorErrorCode() {
        return vendorErrorCode;
    }

}
