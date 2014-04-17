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
package io.motown.ocpp.websocketjson.schema;

import static com.google.common.base.Preconditions.checkNotNull;

public enum MessageProcUri {
    AUTHORIZE("Authorize"),
    START_TRANSACTION("StartTransaction"),
    STOP_TRANSACTION("StopTransaction"),
    HEARTBEAT("Heartbeat"),
    METERVALUES("MeterValues"),
    BOOT_NOTIFICATION("BootNotification"),
    STATUS_NOTIFICATION("StatusNotification"),
    FIRMWARE_STATUS_NOTIFICATION("FirmwareStatusNotification"),
    DIAGNOSTICSS_STATUS_NOTIFICATION("DiagnosticsStatusNotification"),
    DATA_TRANSFER("DataTransfer"),

    UNLOCK_CONNECTOR("UnlockConnector"),
    RESET("Reset"),
    CHANGE_AVAILABILITY("ChangeAvailability"),
    GET_DIAGNOSTICS("GetDiagnostics"),
    CLEAR_CACHE("ClearCache"),
    UPDATE_FIRMWARE("UpdateFirmware"),
    CHANGE_CONFIGURATION("ChangeConfiguration"),
    REMOTE_START_TRANSACTION("RemoteStartTransaction"),
    REMOTE_STOP_TRANSACTION("RemoteStopTransaction"),
    CANCEL_RESERVATION("CancelReservation"),
    GET_CONFIGURATION("GetConfiguration"),
    GET_LOCALLIST_VERSION("GetLocalListVersion"),
    RESERVE_NOW("ReserveNow"),
    SEND_LOCALLIST("SendLocalList")
    ;

    private final String value;

    private MessageProcUri(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static MessageProcUri fromValue(String value) {
        checkNotNull(value);

        for (MessageProcUri uri : values()) {
            if (uri.value.equalsIgnoreCase(value)) {
                return uri;
            }
        }

        throw new IllegalArgumentException("Unknown procedure uri");
    }
}
