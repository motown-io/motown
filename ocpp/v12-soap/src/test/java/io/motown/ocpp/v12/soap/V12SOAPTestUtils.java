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
package io.motown.ocpp.v12.soap;

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.FirmwareUpdateAttributeKey;
import io.motown.ocpp.v12.soap.chargepoint.schema.*;

import javax.persistence.EntityManager;
import java.util.*;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public final class V12SOAPTestUtils {

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

    public static final Integer HEARTBEAT_INTERVAL = 900;

    public static final String PROTOCOL_IDENTIFIER = "OCPPS12";

    public static final String CHARGING_STATION_FIRMWARE_VERSION = "FW001";

    public static final String CHARGING_STATION_ICCID = "ICCID";

    public static final String CHARGING_STATION_IMSI = "IMSI";

    public static final String CHARGING_STATION_METER_TYPE = "METER_TYPE";

    public static final String CHARGING_STATION_METER_SERIAL_NUMBER = "METER_SERIAL_NUMBER";

    /**
     * Private no-arg constructor to prevent instantiation of utility class.
     */
    private V12SOAPTestUtils() {
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


    public static List<io.motown.ocpp.v12.soap.centralsystem.schema.MeterValue> getMeterValuesSoap(int numberOfEntries) {
        List<io.motown.ocpp.v12.soap.centralsystem.schema.MeterValue> meterValues = new ArrayList<>();

        for (int i = 0; i < numberOfEntries; i++) {
            io.motown.ocpp.v12.soap.centralsystem.schema.MeterValue meterValueSoap = new io.motown.ocpp.v12.soap.centralsystem.schema.MeterValue();
            meterValueSoap.setValue(METER_STOP);
            meterValueSoap.setTimestamp(FIVE_MINUTES_AGO);

            meterValues.add(meterValueSoap);
        }

        return meterValues;
    }


    public static void deleteFromDatabase(EntityManager entityManager, Class jpaEntityClass) {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM " + jpaEntityClass.getName()).executeUpdate();
        entityManager.getTransaction().commit();
    }

    public static String getConfigurationKey() {
        return "configKey";
    }

    public static String getConfigurationValue() {
        return "configValue";
    }

    public static String getFirmwareUpdateLocation() {
        return "ftp://test";
    }

    public static Map<String, String> getUpdateFirmwareAttributes(String numberOfRetries, String retryInterval) {
        return ImmutableMap.<String, String>builder()
                .put(FirmwareUpdateAttributeKey.NUM_RETRIES, numberOfRetries)
                .put(FirmwareUpdateAttributeKey.RETRY_INTERVAL, retryInterval)
                .build();
    }

    /**
     * Creates a fixed date so it can be compared to an instance of this method created later in time.
     *
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
