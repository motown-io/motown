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
package io.motown.ocpp.v12.soap.chargepoint;

import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.domain.api.chargingstation.RequestResult;
import io.motown.ocpp.v12.soap.chargepoint.schema.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.v12.soap.V12SOAPTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChargingStationOcpp12SoapClientTest {

    private ChargePointService chargePointService;

    private ChargingStationOcpp12SoapClient client;

    @Before
    public void setUp() {
        ChargingStationProxyFactory chargingStationProxyFactory = mock(ChargingStationProxyFactory.class);
        chargePointService = mock(ChargePointService.class);
        DomainService domainService = mock(DomainService.class);

        when(chargingStationProxyFactory.createChargingStationService(anyString())).thenReturn(chargePointService);

        client = new ChargingStationOcpp12SoapClient();
        client.setChargingStationProxyFactory(chargingStationProxyFactory);
        client.setDomainService(domainService);
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

        RequestResult requestResult = client.softReset(CHARGING_STATION_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void softResetRejectedVerifyReturnValue() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.REJECTED));

        RequestResult requestResult = client.softReset(CHARGING_STATION_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
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

        RequestResult requestResult = client.hardReset(CHARGING_STATION_ID);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void hardResetRejectedVerifyReturnValue() {
        when(chargePointService.reset(any(ResetRequest.class), anyString())).thenReturn(getResetResponse(ResetStatus.REJECTED));

        RequestResult requestResult = client.hardReset(CHARGING_STATION_ID);

        assertEquals(RequestResult.FAILURE, requestResult);
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
    public void changeConfigurationAcceptedVerifyReturnValue() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.ACCEPTED));

        RequestResult requestResult = client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_KEY, CONFIGURATION_VALUE);

        assertEquals(RequestResult.SUCCESS, requestResult);
    }

    @Test
    public void changeConfigurationRejectedVerifyReturnValue() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.REJECTED));

        RequestResult requestResult = client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_KEY, CONFIGURATION_VALUE);

        assertEquals(RequestResult.FAILURE, requestResult);
    }

    @Test
    public void changeConfigurationVerifyServiceCall() {
        when(chargePointService.changeConfiguration(any(ChangeConfigurationRequest.class), eq(CHARGING_STATION_ID.getId()))).thenReturn(getChangeConfigurationResponse(ConfigurationStatus.ACCEPTED));
        ArgumentCaptor<ChangeConfigurationRequest> changeConfigurationArgument = ArgumentCaptor.forClass(ChangeConfigurationRequest.class);

        client.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_KEY, CONFIGURATION_VALUE);

        verify(chargePointService).changeConfiguration(changeConfigurationArgument.capture(), eq(CHARGING_STATION_ID.getId()));
        assertEquals(CONFIGURATION_KEY, changeConfigurationArgument.getValue().getKey());
        assertEquals(CONFIGURATION_VALUE, changeConfigurationArgument.getValue().getValue());
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

}
