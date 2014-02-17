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
package io.motown.ocpp.v15.soap.centralsystem;

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.chargingstation.MeterValue;
import io.motown.ocpp.v15.soap.centralsystem.schema.*;
import io.motown.ocpp.v15.soap.centralsystem.schema.FirmwareStatus;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.v15.soap.V15SOAPTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MotownCentralSystemServiceTest {

    private MotownCentralSystemService motownCentralSystemService;

    private DomainService domainService;

    @Before
    public void setup() {
        motownCentralSystemService = new MotownCentralSystemService();

        domainService = mock(DomainService.class);
        motownCentralSystemService.setDomainService(domainService);
        motownCentralSystemService.setContext(mock(WebServiceContext.class));
    }

    @Test
    public void dataTransferAcceptedVerifyResponse() {
        DataTransferRequest request = new DataTransferRequest();

        DataTransferResponse response = motownCentralSystemService.dataTransfer(request, CHARGING_STATION_ID.getId());

        assertEquals(DataTransferStatus.ACCEPTED, response.getStatus());
    }

    @Test
    public void dataTransferVerifyServiceCall() {
        DataTransferRequest request = new DataTransferRequest();
        request.setData(DATA_TRANSFER_DATA);
        request.setVendorId(DATA_TRANSFER_VENDOR);
        request.setMessageId(DATA_TRANSFER_MESSAGE_ID);

        motownCentralSystemService.dataTransfer(request, CHARGING_STATION_ID.getId());

        verify(domainService).dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_DATA, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID);
    }

    @Test
    public void statusNotificationVerifyResponse() {
        StatusNotificationRequest request = new StatusNotificationRequest();
        request.setStatus(ChargePointStatus.AVAILABLE);

        StatusNotificationResponse response = motownCentralSystemService.statusNotification(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
    }

    @Test
    public void statusNotificationVerifyServiceCall() {
        StatusNotificationRequest request = new StatusNotificationRequest();
        request.setStatus(ChargePointStatus.FAULTED);
        request.setVendorId(CHARGING_STATION_VENDOR);
        request.setConnectorId(EVSE_ID.getNumberedId());
        request.setErrorCode(ChargePointErrorCode.GROUND_FAILURE);
        request.setInfo(STATUS_NOTIFICATION_ERROR_INFO);
        request.setTimestamp(FIVE_MINUTES_AGO);
        request.setVendorErrorCode(STATUS_NOTIFICATION_VENDOR_ERROR_CODE);

        motownCentralSystemService.statusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).statusNotification(CHARGING_STATION_ID, EVSE_ID, ChargePointErrorCode.GROUND_FAILURE.value(), ComponentStatus.FAULTED, STATUS_NOTIFICATION_ERROR_INFO, FIVE_MINUTES_AGO, CHARGING_STATION_VENDOR, STATUS_NOTIFICATION_VENDOR_ERROR_CODE);
    }

    @Test
    public void stopTransactionVerifyResponse() {
        StopTransactionRequest request = new StopTransactionRequest();
        request.setIdTag(IDENTIFYING_TOKEN.getToken());

        StopTransactionResponse response = motownCentralSystemService.stopTransaction(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
    }

//    @Test
//    public void stopTransactionVerifyServiceCall() {
//        StopTransactionRequest request = new StopTransactionRequest();
//        request.setIdTag(IDENTIFYING_TOKEN.getToken());
//        request.setMeterStop(METER_STOP);
//        request.setTimestamp(FIVE_MINUTES_AGO);
//        request.setTransactionId(TRANSACTION_NUMBER);
//        request.getTransactionData().addAll(getTransactionDataForMeterValues(METER_VALUES));
//
//        motownCentralSystemService.stopTransaction(request, CHARGING_STATION_ID.getId());
//
//        verify(domainService).stopTransaction(CHARGING_STATION_ID, new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, TRANSACTION_NUMBER), IDENTIFYING_TOKEN, METER_STOP, FIVE_MINUTES_AGO, METER_VALUES);
//    }

    @Test
    public void bootNotificationAcceptedVerifyResponse() {
        BootNotificationRequest request = new BootNotificationRequest();
        Date now = new Date();
        BootChargingStationResult result = new BootChargingStationResult(true, HEARTBEAT_INTERVAL, now);
        when(domainService.bootChargingStation(any(ChargingStationId.class), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(result);

        BootNotificationResponse response = motownCentralSystemService.bootNotification(request, CHARGING_STATION_ID.getId());

        assertEquals(response.getStatus(), RegistrationStatus.ACCEPTED);
        assertEquals(response.getCurrentTime(), now);
        assertEquals(response.getHeartbeatInterval(), HEARTBEAT_INTERVAL.intValue());
    }

    @Test
    public void bootNotificationRejectedVerifyResponse() {
        BootNotificationRequest request = new BootNotificationRequest();
        Date now = new Date();
        BootChargingStationResult result = new BootChargingStationResult(false, HEARTBEAT_INTERVAL, now);
        when(domainService.bootChargingStation(any(ChargingStationId.class), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(result);

        BootNotificationResponse response = motownCentralSystemService.bootNotification(request, CHARGING_STATION_ID.getId());

        assertEquals(response.getStatus(), RegistrationStatus.REJECTED);
        assertEquals(response.getCurrentTime(), now);
        assertEquals(response.getHeartbeatInterval(), HEARTBEAT_INTERVAL.intValue());
    }

    @Test
    public void bootNotificationVerifyServiceCall() {
        BootNotificationRequest request = new BootNotificationRequest();
        request.setChargePointVendor(CHARGING_STATION_VENDOR);
        request.setChargePointModel(CHARGING_STATION_MODEL);
        request.setChargePointSerialNumber(CHARGING_STATION_SERIAL_NUMBER);
        request.setFirmwareVersion(CHARGING_STATION_FIRMWARE_VERSION);
        request.setIccid(CHARGING_STATION_ICCID);
        request.setImsi(CHARGING_STATION_IMSI);
        request.setMeterType(CHARGING_STATION_METER_TYPE);
        request.setMeterSerialNumber(CHARGING_STATION_METER_SERIAL_NUMBER);

        String addressWillNotBeResolved = "";
        Date now = new Date();
        BootChargingStationResult result = new BootChargingStationResult(true, HEARTBEAT_INTERVAL, now);
        when(domainService.bootChargingStation(any(ChargingStationId.class), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(result);

        motownCentralSystemService.bootNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).bootChargingStation(CHARGING_STATION_ID, addressWillNotBeResolved, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL_IDENTIFIER, CHARGING_STATION_SERIAL_NUMBER,
                CHARGING_STATION_FIRMWARE_VERSION, CHARGING_STATION_ICCID, CHARGING_STATION_IMSI, CHARGING_STATION_METER_TYPE, CHARGING_STATION_METER_SERIAL_NUMBER);
    }

    @Test
    public void heartBeatVerifyResponse() {
        HeartbeatRequest request = new HeartbeatRequest();

        HeartbeatResponse response = motownCentralSystemService.heartbeat(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
        assertNotNull(response.getCurrentTime());
    }

    @Test
    public void heartBeatVerifyServiceCall() {
        HeartbeatRequest request = new HeartbeatRequest();

        motownCentralSystemService.heartbeat(request, CHARGING_STATION_ID.getId());

        verify(domainService).heartbeat(CHARGING_STATION_ID);
    }

    @Test
    public void meterValuesVerifyResponse() {
        MeterValuesRequest request = new MeterValuesRequest();
        request.setTransactionId(TRANSACTION_NUMBER);

        MeterValuesResponse response = motownCentralSystemService.meterValues(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
    }

    @Test
    public void meterValuesVerifyServiceCall() {
        MeterValuesRequest request = new MeterValuesRequest();
        request.setConnectorId(EVSE_ID.getNumberedId());
        request.setTransactionId(TRANSACTION_NUMBER);
        List<io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue> meterValuesSoap = getMeterValuesSoap(4);
        request.getValues().addAll(meterValuesSoap);
        List<MeterValue> expectedMeterValuesList = new ArrayList<>();
        for (io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue mv : meterValuesSoap) {
            expectedMeterValuesList.add(new MeterValue(mv.getTimestamp(), mv.getValue().toString()));
        }

        motownCentralSystemService.meterValues(request, CHARGING_STATION_ID.getId());

        verify(domainService).meterValues(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, expectedMeterValuesList);
    }

    @Test
    public void diagnosticsStatusNotificationVerifyResponse() {
        DiagnosticsStatusNotificationRequest request = new DiagnosticsStatusNotificationRequest();

        DiagnosticsStatusNotificationResponse response = motownCentralSystemService.diagnosticsStatusNotification(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
    }

    @Test
    public void diagnosticsStatusNotificationUploadedVerifyServiceCall() {
        DiagnosticsStatusNotificationRequest request = new DiagnosticsStatusNotificationRequest();
        request.setStatus(DiagnosticsStatus.UPLOADED);

        motownCentralSystemService.diagnosticsStatusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).diagnosticsUploadStatusUpdate(CHARGING_STATION_ID, true);
    }

    @Test
    public void diagnosticsStatusNotificationUploadFailedVerifyServiceCall() {
        DiagnosticsStatusNotificationRequest request = new DiagnosticsStatusNotificationRequest();
        request.setStatus(DiagnosticsStatus.UPLOAD_FAILED);

        motownCentralSystemService.diagnosticsStatusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).diagnosticsUploadStatusUpdate(CHARGING_STATION_ID, false);
    }

    //TODO test authorize

    @Test
    public void firmwareStatusNotificationVerifyResponse() {
        FirmwareStatusNotificationRequest request = new FirmwareStatusNotificationRequest();

        FirmwareStatusNotificationResponse response = motownCentralSystemService.firmwareStatusNotification(request, CHARGING_STATION_ID.getId());

        assertNotNull(response);
    }

    @Test
    public void firmwareStatusNotificationInstalledVerifyServiceCall() {
        FirmwareStatusNotificationRequest request = new FirmwareStatusNotificationRequest();
        request.setStatus(FirmwareStatus.INSTALLED);

        motownCentralSystemService.firmwareStatusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, io.motown.domain.api.chargingstation.FirmwareStatus.INSTALLED);
    }

    @Test
    public void firmwareStatusNotificationInstallFailedVerifyServiceCall() {
        FirmwareStatusNotificationRequest request = new FirmwareStatusNotificationRequest();
        request.setStatus(FirmwareStatus.INSTALLATION_FAILED);

        motownCentralSystemService.firmwareStatusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, io.motown.domain.api.chargingstation.FirmwareStatus.INSTALLATION_FAILED);
    }

    @Test
    public void firmwareStatusNotificationDownloadedVerifyServiceCall() {
        FirmwareStatusNotificationRequest request = new FirmwareStatusNotificationRequest();
        request.setStatus(FirmwareStatus.DOWNLOADED);

        motownCentralSystemService.firmwareStatusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, io.motown.domain.api.chargingstation.FirmwareStatus.DOWNLOADED);
    }

    @Test
    public void firmwareStatusNotificationDownloadFailedVerifyServiceCall() {
        FirmwareStatusNotificationRequest request = new FirmwareStatusNotificationRequest();
        request.setStatus(FirmwareStatus.DOWNLOAD_FAILED);

        motownCentralSystemService.firmwareStatusNotification(request, CHARGING_STATION_ID.getId());

        verify(domainService).firmwareStatusUpdate(CHARGING_STATION_ID, io.motown.domain.api.chargingstation.FirmwareStatus.DOWNLOAD_FAILED);
    }

    @Test
    public void startTransactionVerifyResponse() {
        StartTransactionRequest request = new StartTransactionRequest();
        request.setConnectorId(EVSE_ID.getNumberedId());
        request.setIdTag(IDENTIFYING_TOKEN.getToken());

        StartTransactionResponse response = motownCentralSystemService.startTransaction(request, CHARGING_STATION_ID.getId());

        assertNotNull(response.getIdTagInfo());
        assertNotNull(response.getTransactionId());
    }

    @Test
    public void startTransactionVerifyServiceCall() {
        StartTransactionRequest request = new StartTransactionRequest();
        request.setConnectorId(EVSE_ID.getNumberedId());
        request.setIdTag(IDENTIFYING_TOKEN.getToken());
        request.setMeterStart(METER_START);
        request.setTimestamp(TWO_MINUTES_AGO);

        motownCentralSystemService.startTransaction(request, CHARGING_STATION_ID.getId());

        verify(domainService).startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, METER_START, TWO_MINUTES_AGO, null, PROTOCOL_IDENTIFIER);
    }

    @Test
    public void startTransactionWithReservationVerifyServiceCall() {
        StartTransactionRequest request = new StartTransactionRequest();
        request.setConnectorId(EVSE_ID.getNumberedId());
        request.setIdTag(IDENTIFYING_TOKEN.getToken());
        request.setMeterStart(METER_START);
        request.setTimestamp(TWO_MINUTES_AGO);
        request.setReservationId(RESERVATION_ID.getNumber());

        motownCentralSystemService.startTransaction(request, CHARGING_STATION_ID.getId());

        verify(domainService).startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, METER_START, TWO_MINUTES_AGO, RESERVATION_ID, PROTOCOL_IDENTIFIER);
    }
}
