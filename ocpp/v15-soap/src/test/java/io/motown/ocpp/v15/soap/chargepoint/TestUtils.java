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
package io.motown.ocpp.v15.soap.chargepoint;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.v15.soap.chargepoint.schema.*;
import io.motown.ocpp.v15.soap.chargepoint.schema.ReservationStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestUtils {

    public static final ChargingStationId CHARGING_STATION_ID = new ChargingStationId("CS-001");

    public static final IdentifyingToken IDENTIFYING_TOKEN = new TextualToken("token");

    public static final EvseId EVSE_ID = new EvseId(1);

    public static final int TRANSACTION_ID = 2;

    public static final String VENDOR_ID = "MOTOWN";

    public static final String DATA_TRANSFER_MESSAGE_ID = "MESSAGE_ID";

    public static final String DATA = "DATA";

    public static final String CONFIGURATION_KEY = "HEARTBEATINTERVAL";

    public static final String CONFIGURATION_VALUE = "900";

    public static final String DIAGNOSTICS_FILENAME = "diagnostics.txt";

    public static final String UPLOAD_LOCATION = "FTP://test";

    public static final Integer NUMBER_OF_RETRIES = 3;

    public static final Integer RETRY_INTERVAL = 60;

    public static final Date PERIOD_START_TIME = getFixedDate();

    public static final Date PERIOD_STOP_TIME = getFixedDate();

    public static final String DOWNLOAD_LOCATION = "FTP://test";

    public static final Date RETRIEVED_DATE = getFixedDate();

    public static final Date EXPIRY_DATE = getFixedDate();

    public static final IdentifyingToken PARENT_IDENTIFYING_TOKEN = new TextualToken("parent");

    public static final int RESERVATION_ID = 2;

    public static GetConfigurationResponse getGetConfigurationResponse() {
        GetConfigurationResponse response = new GetConfigurationResponse();

        response.getConfigurationKey().add(getKeyValue("HeartbeatInterval", "900"));
        response.getConfigurationKey().add(getKeyValue("ConnectionTimeOut", "1200"));
        response.getConfigurationKey().add(getKeyValue("ResetRetries", "5"));

        return response;
    }

    public static KeyValue getKeyValue(String key, String value) {
        KeyValue keyValue = new KeyValue();
        keyValue.setKey(key);
        keyValue.setValue(value);
        return keyValue;
    }

    public static RemoteStartTransactionResponse getRemoteStartTransactionResponse(RemoteStartStopStatus status) {
        RemoteStartTransactionResponse response = new RemoteStartTransactionResponse();
        response.setStatus(status);
        return response;
    }

    public static RemoteStopTransactionResponse getRemoteStopTransactionResponse(RemoteStartStopStatus status) {
        RemoteStopTransactionResponse response = new RemoteStopTransactionResponse();
        response.setStatus(status);
        return response;
    }

    public static ResetResponse getResetResponse(ResetStatus status) {
        ResetResponse response = new ResetResponse();
        response.setStatus(status);
        return response;
    }

    public static UnlockConnectorResponse getUnlockConnectorResponse(UnlockStatus status) {
        UnlockConnectorResponse response = new UnlockConnectorResponse();
        response.setStatus(status);
        return response;
    }

    public static ChangeAvailabilityResponse getChangeAvailabilityResponse(AvailabilityStatus status) {
        ChangeAvailabilityResponse response = new ChangeAvailabilityResponse();
        response.setStatus(status);
        return response;
    }

    public static DataTransferResponse getDataTransferResponse(DataTransferStatus status) {
        DataTransferResponse response = new DataTransferResponse();
        response.setStatus(status);
        return response;
    }

    public static ChangeConfigurationResponse getChangeConfigurationResponse(ConfigurationStatus status) {
        ChangeConfigurationResponse response = new ChangeConfigurationResponse();
        response.setStatus(status);
        return response;
    }

    public static GetDiagnosticsResponse getGetDiagnosticsResponse(String filename) {
        GetDiagnosticsResponse response = new GetDiagnosticsResponse();
        response.setFileName(filename);
        return response;
    }

    public static ClearCacheResponse getClearCacheResponse(ClearCacheStatus status) {
        ClearCacheResponse response = new ClearCacheResponse();
        response.setStatus(status);
        return response;
    }

    public static GetLocalListVersionResponse getGetLocalListVersionResponse(int listVersion) {
        GetLocalListVersionResponse response = new GetLocalListVersionResponse();
        response.setListVersion(listVersion);
        return response;
    }

    public static ReserveNowResponse getReserveNowResponse(ReservationStatus status) {
        ReserveNowResponse response = new ReserveNowResponse();
        response.setStatus(status);
        return response;
    }

    /**
     * Creates a fixed date so it can be compared to an instance of this method created later in time.

     * @return fixed date.
     */
    private static Date getFixedDate() {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
