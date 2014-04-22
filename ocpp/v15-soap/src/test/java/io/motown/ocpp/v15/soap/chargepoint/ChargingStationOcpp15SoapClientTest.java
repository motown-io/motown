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

import io.motown.domain.api.chargingstation.AuthorizationListUpdateType;
import io.motown.domain.api.chargingstation.DataTransferRequestResult;
import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.domain.api.chargingstation.RequestResult;
import io.motown.ocpp.v15.soap.chargepoint.schema.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.v15.soap.V15SOAPTestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ChargingStationOcpp15SoapClientTest {

    private ChargePointService chargePointService;

    private ChargingStationOcpp15SoapClient client;

    private IdentifyingTokenConverterService identifyingTokenConverterService;

    @Before
    public void setUp() {
        ChargingStationProxyFactory chargingStationProxyFactory = mock(ChargingStationProxyFactory.class);
        chargePointService = mock(ChargePointService.class);
        DomainService domainService = mock(DomainService.class);
        identifyingTokenConverterService = new IdentifyingTokenConverterService();

        when(chargingStationProxyFactory.createChargingStationService(anyString())).thenReturn(chargePointService);

        client = new ChargingStationOcpp15SoapClient();
        client.setChargingStationProxyFactory(chargingStationProxyFactory);
        client.setDomainService(domainService);
        client.setIdentifyingTokenConverterService(identifyingTokenConverterService);
    }

    @Test
    public void getConfigurationVerifyReturnValue() {
        when(chargePointService.getConfiguration(any(GetConfigurationRequest.class), anyString())).thenReturn(getGetConfigurationResponse());

        Map<String, String> configuration = client.getConfiguration(CHARGING_STATION_ID);

        for (KeyValue keyValue : getGetConfigurationResponse().getConfigurationKey()) {
            assertTrue(configuration.containsKey(keyValue.getKey()));
            assertEquals(configuration.get(keyValue.getKey()), keyValue.getValue());
        }
    }

    @Test
    public void getConfigurationVerifyServiceCall() {
        when(chargePointService.getConfiguration(any(GetConfigurationRequest.class), anyString())).thenReturn(getGetConfigurationResponse());
        ArgumentCaptor<GetConfigurationRequest> getConfigurationRequestArgument = ArgumentCaptor.forClass(GetConfigurationRequest.class);

        client.getConfiguration(CHARGING_STATION_ID);

        verify(chargePointService).getConfiguration(getConfigurationRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        Assert.assertEquals(getConfigurationRequestArgument.getValue().getKey().size(), 0);
    }

    @Test
    public void startTransactionAcceptedVerifyReturnValue() {
        when(chargePointService.remoteStartTransaction(any(RemoteStartTransactionRequest.class), anyString())).thenReturn(getRemoteStartTransactionResponse(RemoteStartStopStatus.ACCEPTED));

        RequestResult requestResult = client.startTransaction(CHARGING_STATION_ID, IDENTIFYING_TOKEN, EVSE_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void startTransactionRejectedVerifyReturnValue() {
        when(chargePointService.remoteStartTransaction(any(RemoteStartTransactionRequest.class), anyString())).thenReturn(getRemoteStartTransactionResponse(RemoteStartStopStatus.REJECTED));

        RequestResult requestResult = client.startTransaction(CHARGING_STATION_ID, IDENTIFYING_TOKEN, EVSE_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void startTransactionVerifyServiceCall() {
        when(chargePointService.remoteStartTransaction(any(RemoteStartTransactionRequest.class), anyString())).thenReturn(getRemoteStartTransactionResponse(RemoteStartStopStatus.ACCEPTED));
        ArgumentCaptor<RemoteStartTransactionRequest> remoteStartTransactionRequestArgument = ArgumentCaptor.forClass(RemoteStartTransactionRequest.class);

        client.startTransaction(CHARGING_STATION_ID, IDENTIFYING_TOKEN, EVSE_ID);

        verify(chargePointService).remoteStartTransaction(remoteStartTransactionRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(IDENTIFYING_TOKEN.getToken(), remoteStartTransactionRequestArgument.getValue().getIdTag());
        assertEquals(Integer.valueOf(EVSE_ID.getId()), remoteStartTransactionRequestArgument.getValue().getConnectorId());
    }

    @Test
    public void stopTransactionAcceptedVerifyReturnValue() {
        when(chargePointService.remoteStopTransaction(any(RemoteStopTransactionRequest.class), anyString())).thenReturn(getRemoteStopTransactionResponse(RemoteStartStopStatus.ACCEPTED));

        RequestResult requestResult = client.stopTransaction(CHARGING_STATION_ID, ((NumberedTransactionId) TRANSACTION_ID).getNumber());

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void stopTransactionRejectedVerifyReturnValue() {
        when(chargePointService.remoteStopTransaction(any(RemoteStopTransactionRequest.class), anyString())).thenReturn(getRemoteStopTransactionResponse(RemoteStartStopStatus.REJECTED));

        RequestResult requestResult = client.stopTransaction(CHARGING_STATION_ID, ((NumberedTransactionId) TRANSACTION_ID).getNumber());

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void stopTransactionVerifyServiceCall() {
        when(chargePointService.remoteStopTransaction(any(RemoteStopTransactionRequest.class), anyString())).thenReturn(getRemoteStopTransactionResponse(RemoteStartStopStatus.ACCEPTED));
        ArgumentCaptor<RemoteStopTransactionRequest> remoteStopTransactionRequestArgument = ArgumentCaptor.forClass(RemoteStopTransactionRequest.class);

        client.stopTransaction(CHARGING_STATION_ID, ((NumberedTransactionId) TRANSACTION_ID).getNumber());

        verify(chargePointService).remoteStopTransaction(remoteStopTransactionRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(((NumberedTransactionId) TRANSACTION_ID).getNumber(), remoteStopTransactionRequestArgument.getValue().getTransactionId());
    }

    @Test
    public void softResetAcceptedVerifyReturnValue() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.ACCEPTED));

        boolean hasReset = client.softReset(CHARGING_STATION_ID);

        assertTrue(hasReset);
    }

    @Test
    public void softResetRejectedVerifyReturnValue() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.REJECTED));

        boolean hasReset = client.softReset(CHARGING_STATION_ID);

        assertFalse(hasReset);
    }

    @Test
    public void softResetVerifyServiceCall() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.ACCEPTED));
        ArgumentCaptor<ResetRequest> resetRequestArgument = ArgumentCaptor.forClass(ResetRequest.class);

        client.softReset(CHARGING_STATION_ID);

        verify(chargePointService).reset(resetRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(ResetType.SOFT, resetRequestArgument.getValue().getType());
    }

    @Test
    public void hardResetAcceptedVerifyReturnValue() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.ACCEPTED));

        boolean hasReset = client.hardReset(CHARGING_STATION_ID);

        assertTrue(hasReset);
    }

    @Test
    public void hardResetRejectedVerifyReturnValue() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.REJECTED));

        boolean hasReset = client.hardReset(CHARGING_STATION_ID);

        assertFalse(hasReset);
    }

    @Test
    public void hardResetVerifyServiceCall() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.ACCEPTED));
        ArgumentCaptor<ResetRequest> resetRequestArgument = ArgumentCaptor.forClass(ResetRequest.class);

        client.hardReset(CHARGING_STATION_ID);

        verify(chargePointService).reset(resetRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(ResetType.HARD, resetRequestArgument.getValue().getType());
    }

    @Test
    public void unlockConnectorAcceptedVerifyReturnValue() {
        when(chargePointService.unlockConnector(any(UnlockConnectorRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getUnlockConnectorResponse(UnlockStatus.ACCEPTED));

        RequestResult requestResult = client.unlockConnector(CHARGING_STATION_ID, EVSE_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void unlockConnectorRejectedVerifyReturnValue() {
        when(chargePointService.unlockConnector(any(UnlockConnectorRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getUnlockConnectorResponse(UnlockStatus.REJECTED));

        RequestResult requestResult = client.unlockConnector(CHARGING_STATION_ID, EVSE_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void unlockConnectorVerifyServiceCall() {
        when(chargePointService.unlockConnector(any(UnlockConnectorRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getUnlockConnectorResponse(UnlockStatus.ACCEPTED));
        ArgumentCaptor<UnlockConnectorRequest> unlockConnectorArgument = ArgumentCaptor.forClass(UnlockConnectorRequest.class);

        client.unlockConnector(CHARGING_STATION_ID, EVSE_ID);

        verify(chargePointService).unlockConnector(unlockConnectorArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(Integer.parseInt(EVSE_ID.getId()), unlockConnectorArgument.getValue().getConnectorId());
    }

    @Test
    public void changeAvailabilityToInoperativeAcceptedVerifyReturnValue() {
        when(chargePointService.changeAvailability(any(ChangeAvailabilityRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeAvailabilityResponse(AvailabilityStatus.ACCEPTED));

        RequestResult requestResult = client.changeAvailabilityToInoperative(CHARGING_STATION_ID, EVSE_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void changeAvailabilityToInoperativeRejectedVerifyReturnValue() {
        when(chargePointService.changeAvailability(any(ChangeAvailabilityRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeAvailabilityResponse(AvailabilityStatus.REJECTED));

        RequestResult requestResult = client.changeAvailabilityToInoperative(CHARGING_STATION_ID, EVSE_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void changeAvailabilityToInoperativeVerifyServiceCall() {
        when(chargePointService.changeAvailability(any(ChangeAvailabilityRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeAvailabilityResponse(AvailabilityStatus.ACCEPTED));
        ArgumentCaptor<ChangeAvailabilityRequest> changeAvailabilityRequestArgument = ArgumentCaptor.forClass(ChangeAvailabilityRequest.class);

        client.changeAvailabilityToInoperative(CHARGING_STATION_ID, EVSE_ID);

        verify(chargePointService).changeAvailability(changeAvailabilityRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(Integer.parseInt(EVSE_ID.getId()), changeAvailabilityRequestArgument.getValue().getConnectorId());
        assertEquals(AvailabilityType.INOPERATIVE, changeAvailabilityRequestArgument.getValue().getType());
    }

    @Test
    public void changeAvailabilityToOperativeAcceptedVerifyReturnValue() {
        when(chargePointService.changeAvailability(any(ChangeAvailabilityRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeAvailabilityResponse(AvailabilityStatus.ACCEPTED));

        RequestResult requestResult = client.changeAvailabilityToOperative(CHARGING_STATION_ID, EVSE_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void changeAvailabilityToOperativeRejectedVerifyReturnValue() {
        when(chargePointService.changeAvailability(any(ChangeAvailabilityRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeAvailabilityResponse(AvailabilityStatus.REJECTED));

        RequestResult requestResult = client.changeAvailabilityToOperative(CHARGING_STATION_ID, EVSE_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void changeAvailabilityOnoperativeVerifyServiceCall() {
        when(chargePointService.changeAvailability(any(ChangeAvailabilityRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeAvailabilityResponse(AvailabilityStatus.ACCEPTED));
        ArgumentCaptor<ChangeAvailabilityRequest> changeAvailabilityRequestArgument = ArgumentCaptor.forClass(ChangeAvailabilityRequest.class);

        client.changeAvailabilityToOperative(CHARGING_STATION_ID, EVSE_ID);

        verify(chargePointService).changeAvailability(changeAvailabilityRequestArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(Integer.parseInt(EVSE_ID.getId()), changeAvailabilityRequestArgument.getValue().getConnectorId());
        assertEquals(AvailabilityType.OPERATIVE, changeAvailabilityRequestArgument.getValue().getType());
    }

    @Test
    public void dataTransferAcceptedVerifyReturnValue() {
        when(chargePointService.dataTransfer(any(DataTransferRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getDataTransferResponse(DataTransferStatus.ACCEPTED));

        DataTransferRequestResult result = client.dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA);

        assertEquals(RequestResult.SUCCESS, result.getRequestResult());
    }

    @Test
    public void dataTransferRejectedVerifyReturnValue() {
        when(chargePointService.dataTransfer(any(DataTransferRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getDataTransferResponse(DataTransferStatus.REJECTED));

        DataTransferRequestResult result = client.dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA);

        assertEquals(RequestResult.FAILURE, result.getRequestResult());
    }

    @Test
    public void dataTransferVerifyServiceCall() {
        when(chargePointService.dataTransfer(any(DataTransferRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getDataTransferResponse(DataTransferStatus.ACCEPTED));
        ArgumentCaptor<DataTransferRequest> dataTransferArgument = ArgumentCaptor.forClass(DataTransferRequest.class);

        client.dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA);

        verify(chargePointService).dataTransfer(dataTransferArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(DATA_TRANSFER_MESSAGE_ID, dataTransferArgument.getValue().getMessageId());
        assertEquals(DATA_TRANSFER_DATA, dataTransferArgument.getValue().getData());
        assertEquals(DATA_TRANSFER_VENDOR, dataTransferArgument.getValue().getVendorId());
    }

    @Test
    public void changeConfigurationAcceptedVerifyReturnValue() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.ACCEPTED));

        boolean hasConfigurationChanged = client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM);

        assertTrue(hasConfigurationChanged);
    }

    @Test
    public void changeConfigurationNotSupportedVerifyReturnValue() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.NOT_SUPPORTED));

        boolean hasConfigurationChanged = client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM);

        assertFalse(hasConfigurationChanged);
    }

    @Test
    public void changeConfigurationRejectedVerifyReturnValue() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.REJECTED));

        boolean hasConfigurationChanged = client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM);

        assertFalse(hasConfigurationChanged);
    }

    @Test
    public void changeConfigurationVerifyServiceCall() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.ACCEPTED));
        ArgumentCaptor<ChangeConfigurationRequest> changeConfigurationArgument = ArgumentCaptor.forClass(ChangeConfigurationRequest.class);

        client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM);

        verify(chargePointService).changeConfiguration(changeConfigurationArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(CONFIGURATION_ITEM.getKey(), changeConfigurationArgument.getValue().getKey());
        assertEquals(CONFIGURATION_ITEM.getValue(), changeConfigurationArgument.getValue().getValue());
    }

    @Test
    public void getDiagnosticsVerifyReturnValue() {
        when(chargePointService.getDiagnostics(any(GetDiagnosticsRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getGetDiagnosticsResponse(DIAGNOSTICS_FILENAME));

        String returnFilename = client.getDiagnostics(CHARGING_STATION_ID, UPLOAD_LOCATION, NUMBER_OF_RETRIES, RETRY_INTERVAL, PERIOD_START_TIME, PERIOD_STOP_TIME);

        assertEquals(DIAGNOSTICS_FILENAME, returnFilename);
    }

    @Test
    public void getDiagnosticsVerifyServiceCall() {
        when(chargePointService.getDiagnostics(any(GetDiagnosticsRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getGetDiagnosticsResponse(DIAGNOSTICS_FILENAME));
        ArgumentCaptor<GetDiagnosticsRequest> getDiagnosticsArgument = ArgumentCaptor.forClass(GetDiagnosticsRequest.class);

        client.getDiagnostics(CHARGING_STATION_ID, UPLOAD_LOCATION, NUMBER_OF_RETRIES, RETRY_INTERVAL, PERIOD_START_TIME, PERIOD_STOP_TIME);

        verify(chargePointService).getDiagnostics(getDiagnosticsArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(UPLOAD_LOCATION, getDiagnosticsArgument.getValue().getLocation());
        assertEquals(NUMBER_OF_RETRIES, getDiagnosticsArgument.getValue().getRetries());
        assertEquals(RETRY_INTERVAL, getDiagnosticsArgument.getValue().getRetryInterval());
        assertEquals(PERIOD_START_TIME, getDiagnosticsArgument.getValue().getStartTime());
        assertEquals(PERIOD_STOP_TIME, getDiagnosticsArgument.getValue().getStopTime());
    }

    @Test
    public void clearCacheAcceptedVerifyReturnValue() {
        when(chargePointService.clearCache(any(ClearCacheRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getClearCacheResponse(ClearCacheStatus.ACCEPTED));

        RequestResult requestResult = client.clearCache(CHARGING_STATION_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void clearCacheRejectedVerifyReturnValue() {
        when(chargePointService.clearCache(any(ClearCacheRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getClearCacheResponse(ClearCacheStatus.REJECTED));

        RequestResult requestResult = client.clearCache(CHARGING_STATION_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void clearCacheVerifyServiceCall() {
        when(chargePointService.clearCache(any(ClearCacheRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getClearCacheResponse(ClearCacheStatus.ACCEPTED));

        client.clearCache(CHARGING_STATION_ID);

        verify(chargePointService).clearCache(any(ClearCacheRequest.class), eq(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void updateFirmwareNoException() {
        client.updateFirmware(CHARGING_STATION_ID, DOWNLOAD_LOCATION, RETRIEVED_DATE, NUMBER_OF_RETRIES, RETRY_INTERVAL);
    }

    @Test
    public void updateFirmwareVerifyServiceCall() {
        ArgumentCaptor<UpdateFirmwareRequest> updateFirmwareArgument = ArgumentCaptor.forClass(UpdateFirmwareRequest.class);

        client.updateFirmware(CHARGING_STATION_ID, DOWNLOAD_LOCATION, RETRIEVED_DATE, NUMBER_OF_RETRIES, RETRY_INTERVAL);

        verify(chargePointService).updateFirmware(updateFirmwareArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(DOWNLOAD_LOCATION, updateFirmwareArgument.getValue().getLocation());
        assertEquals(NUMBER_OF_RETRIES, updateFirmwareArgument.getValue().getRetries());
        assertEquals(RETRY_INTERVAL, updateFirmwareArgument.getValue().getRetryInterval());
        assertEquals(RETRIEVED_DATE, updateFirmwareArgument.getValue().getRetrieveDate());
    }

    @Test
    public void getAuthorizationListVersionVerifyReturnValue() {
        int listVersion = 1;
        when(chargePointService.getLocalListVersion(any(GetLocalListVersionRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getGetLocalListVersionResponse(listVersion));

        int authorizationListVersion = client.getAuthorizationListVersion(CHARGING_STATION_ID);

        assertEquals(listVersion, authorizationListVersion);
    }

    @Test
    public void getAuthorizationListVersionVerifyServiceCall() {
        when(chargePointService.getLocalListVersion(any(GetLocalListVersionRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getGetLocalListVersionResponse(1));

        client.getAuthorizationListVersion(CHARGING_STATION_ID);

        verify(chargePointService).getLocalListVersion(any(GetLocalListVersionRequest.class), eq(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void sendAuthorizationListAcceptedVerifyReturnValue() {
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.ACCEPTED));

        RequestResult requestResult = client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.FULL);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void sendAuthorizationListFailedVerifyReturnValue() {
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.FAILED));

        RequestResult requestResult = client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.FULL);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void sendAuthorizationListHashErrorVerifyReturnValue() {
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.HASH_ERROR));

        RequestResult requestResult = client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.FULL);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void sendAuthorizationListNotSupportedVerifyReturnValue() {
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.NOT_SUPPORTED));

        RequestResult requestResult = client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.FULL);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void sendAuthorizationListVersionMismatchVerifyReturnValue() {
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.VERSION_MISMATCH));

        RequestResult requestResult = client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.FULL);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void sendAuthorizationListUpdateTypeFullVerifyServiceCall() {
        ArgumentCaptor<SendLocalListRequest> sendLocalListArgument = ArgumentCaptor.forClass(SendLocalListRequest.class);
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.ACCEPTED));

        client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.FULL);

        verify(chargePointService).sendLocalList(sendLocalListArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(AUTH_LIST_HASH, sendLocalListArgument.getValue().getHash());
        assertEquals(LIST_VERSION, sendLocalListArgument.getValue().getListVersion());
        assertEquals(UpdateType.FULL, sendLocalListArgument.getValue().getUpdateType());
        assertEquals(identifyingTokenConverterService.convertIdentifyingTokenList(IDENTIFYING_TOKENS).size(), sendLocalListArgument.getValue().getLocalAuthorisationList().size());
    }

    @Test
    public void sendAuthorizationListUpdateTypeDifferentialVerifyServiceCall() {
        ArgumentCaptor<SendLocalListRequest> sendLocalListArgument = ArgumentCaptor.forClass(SendLocalListRequest.class);
        when(chargePointService.sendLocalList(any(SendLocalListRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getSendLocalListResponse(UpdateStatus.ACCEPTED));

        client.sendAuthorizationList(CHARGING_STATION_ID, AUTH_LIST_HASH, LIST_VERSION, IDENTIFYING_TOKENS, AuthorizationListUpdateType.DIFFERENTIAL);

        verify(chargePointService).sendLocalList(sendLocalListArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(UpdateType.DIFFERENTIAL, sendLocalListArgument.getValue().getUpdateType());
    }

    @Test
    public void reserveNowAcceptedVerifyReturnValue() {
        when(chargePointService.reserveNow(any(ReserveNowRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getReserveNowResponse(ReservationStatus.ACCEPTED));

        io.motown.domain.api.chargingstation.ReservationStatus reservationStatus = client.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, PARENT_IDENTIFYING_TOKEN, RESERVATION_ID.getNumber());

        assertEquals(io.motown.domain.api.chargingstation.ReservationStatus.ACCEPTED, reservationStatus);
    }

    @Test
    public void reserveNowFaultedVerifyReturnValue() {
        when(chargePointService.reserveNow(any(ReserveNowRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getReserveNowResponse(ReservationStatus.FAULTED));

        io.motown.domain.api.chargingstation.ReservationStatus reservationStatus = client.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, PARENT_IDENTIFYING_TOKEN, RESERVATION_ID.getNumber());

        assertEquals(io.motown.domain.api.chargingstation.ReservationStatus.FAULTED, reservationStatus);
    }

    @Test
    public void reserveNowOccupiedVerifyReturnValue() {
        when(chargePointService.reserveNow(any(ReserveNowRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getReserveNowResponse(ReservationStatus.OCCUPIED));

        io.motown.domain.api.chargingstation.ReservationStatus reservationStatus = client.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, PARENT_IDENTIFYING_TOKEN, RESERVATION_ID.getNumber());

        assertEquals(io.motown.domain.api.chargingstation.ReservationStatus.OCCUPIED, reservationStatus);
    }

    @Test
    public void reserveNowRejectedVerifyReturnValue() {
        when(chargePointService.reserveNow(any(ReserveNowRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getReserveNowResponse(ReservationStatus.REJECTED));

        io.motown.domain.api.chargingstation.ReservationStatus reservationStatus = client.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, PARENT_IDENTIFYING_TOKEN, RESERVATION_ID.getNumber());

        assertEquals(io.motown.domain.api.chargingstation.ReservationStatus.REJECTED, reservationStatus);
    }

    @Test
    public void reserveNowUnavailableVerifyReturnValue() {
        when(chargePointService.reserveNow(any(ReserveNowRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getReserveNowResponse(ReservationStatus.UNAVAILABLE));

        io.motown.domain.api.chargingstation.ReservationStatus reservationStatus = client.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, PARENT_IDENTIFYING_TOKEN, RESERVATION_ID.getNumber());

        assertEquals(io.motown.domain.api.chargingstation.ReservationStatus.UNAVAILABLE, reservationStatus);
    }


    @Test
    public void reserveNowVersionVerifyServiceCall() {
        when(chargePointService.reserveNow(any(ReserveNowRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getReserveNowResponse(ReservationStatus.ACCEPTED));
        ArgumentCaptor<ReserveNowRequest> reserveNowArgument = ArgumentCaptor.forClass(ReserveNowRequest.class);

        client.reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, EXPIRY_DATE, PARENT_IDENTIFYING_TOKEN, RESERVATION_ID.getNumber());

        verify(chargePointService).reserveNow(reserveNowArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(Integer.parseInt(EVSE_ID.getId()), reserveNowArgument.getValue().getConnectorId());
        assertEquals(IDENTIFYING_TOKEN.getToken(), reserveNowArgument.getValue().getIdTag());
        assertEquals(EXPIRY_DATE, reserveNowArgument.getValue().getExpiryDate());
        assertEquals(PARENT_IDENTIFYING_TOKEN.getToken(), reserveNowArgument.getValue().getParentIdTag());
        assertEquals(RESERVATION_ID.getNumber(), reserveNowArgument.getValue().getReservationId());
    }

}
