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
package io.motown.ocpp.v15.soap;

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.chargingstation.MeterValue;
import io.motown.ocpp.v15.soap.centralsystem.schema.*;
import io.motown.ocpp.v15.soap.chargepoint.schema.*;
import io.motown.ocpp.v15.soap.chargepoint.schema.DataTransferResponse;
import io.motown.ocpp.v15.soap.chargepoint.schema.DataTransferStatus;
import io.motown.ocpp.v15.soap.chargepoint.schema.ReservationStatus;
import io.motown.ocpp.viewmodel.domain.DomainService;

import javax.persistence.EntityManager;
import java.util.*;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.FIVE_MINUTES_AGO;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.METER_STOP;

public final class V15SOAPTestUtils {

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

    public static final String AUTH_LIST_HASH = "hash";

    public static final String STATUS_NOTIFICATION_ERROR_INFO = "error info";

    public static final String STATUS_NOTIFICATION_VENDOR_ERROR_CODE = "007";

    public static final Integer HEARTBEAT_INTERVAL = 900;

    public static final String PROTOCOL_IDENTIFIER = "OCPPS15";

    public static final String CHARGING_STATION_FIRMWARE_VERSION = "FW001";

    public static final String CHARGING_STATION_ICCID = "ICCID";

    public static final String CHARGING_STATION_IMSI = "IMSI";

    public static final String CHARGING_STATION_METER_TYPE = "METER_TYPE";

    public static final String CHARGING_STATION_METER_SERIAL_NUMBER = "METER_SERIAL_NUMBER";

    /**
     * Private no-arg constructor to prevent instantiation of utility class.
     */
    private V15SOAPTestUtils() {
    }

    public static void deleteFromDatabase(EntityManager entityManager, Class jpaEntityClass) {
        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM " + jpaEntityClass.getName()).executeUpdate();
        entityManager.getTransaction().commit();
    }

    public static String getConfigurationValue() {
        return "configValue";
    }

    public static Map<String, String> getUpdateFirmwareAttributes(String numberOfRetries, String retryInterval) {
        return ImmutableMap.<String, String>builder()
                .put(FirmwareUpdateAttributeKey.NUM_RETRIES, numberOfRetries)
                .put(FirmwareUpdateAttributeKey.RETRY_INTERVAL, retryInterval)
                .build();
    }

    public static Integer getAuthorizationListVersion() {
        return 1;
    }

    public static AuthorizationListUpdateType getAuthorizationListUpdateType() {
        return AuthorizationListUpdateType.DIFFERENTIAL;
    }

    public static List<IdentifyingToken> getAuthorizationList() {
        List<IdentifyingToken> list = new ArrayList<>();
        list.add(new TextualToken("1"));
        list.add(new TextualToken("2"));
        return list;
    }

    public static GetConfigurationResponse getGetConfigurationResponse() {
        GetConfigurationResponse response = new GetConfigurationResponse();

        response.getConfigurationKey().add(getKeyValue("HeartbeatInterval", "900"));
        response.getConfigurationKey().add(getKeyValue("ConnectionTimeOut", "1200"));
        response.getConfigurationKey().add(getKeyValue("ResetRetries", "5"));

        return response;
    }

    public static String getFirmwareUpdateLocation() {
        return "ftp://test";
    }

    public static String getAuthorizationListHash() {
        return "hash";
    }

    public static String getConfigurationKey() {
        return "configKey";
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

    public static SendLocalListResponse getSendLocalListResponse(UpdateStatus status) {
        SendLocalListResponse response = new SendLocalListResponse();
        response.setStatus(status);
        return response;
    }

    public static ReserveNowResponse getReserveNowResponse(ReservationStatus status) {
        ReserveNowResponse response = new ReserveNowResponse();
        response.setStatus(status);
        return response;
    }

    public static List<TransactionData> getTransactionDataForMeterValues(List<MeterValue> meterValues) {
        List<TransactionData> transactionData = new ArrayList<>();

        for (MeterValue meterValue : meterValues) {
            io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue.Value value = new io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue.Value();
            value.setValue(meterValue.getValue());
            value.setContext(ReadingContext.fromValue(meterValue.getAttributes().get(DomainService.CONTEXT_KEY)));
            value.setFormat(ValueFormat.fromValue(meterValue.getAttributes().get(DomainService.FORMAT_KEY)));
            value.setMeasurand(Measurand.fromValue(meterValue.getAttributes().get(DomainService.MEASURAND_KEY)));
            value.setLocation(Location.fromValue(meterValue.getAttributes().get(DomainService.LOCATION_KEY)));
            value.setUnit(UnitOfMeasure.fromValue(meterValue.getAttributes().get(DomainService.UNIT_KEY)));

            io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue meterValueSoap = new io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue();
            meterValueSoap.getValue().add(value);
            meterValueSoap.setTimestamp(meterValue.getTimestamp());

            TransactionData data = new TransactionData();
            data.getValues().add(meterValueSoap);

            transactionData.add(data);
        }

        return transactionData;
    }
    
    public static List<io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue> getMeterValuesSoap(int numberOfEntries) {
        List<io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue> meterValues = new ArrayList<>();

        for (int i = 0; i < numberOfEntries; i++) {
            io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue.Value meterValue = new io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue.Value();
            meterValue.setValue(String.valueOf(METER_STOP));

            io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue meterValueSoap = new io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue();
            meterValueSoap.getValue().add(meterValue);
            meterValueSoap.setTimestamp(FIVE_MINUTES_AGO);

            meterValues.add(meterValueSoap);
        }

        return meterValues;
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
