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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.Ocpp15RequestHandler;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.motown.domain.api.chargingstation.ChargingStationTestUtils.*;
import static io.motown.ocpp.viewmodel.domain.OccpViewModelTestUtils.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Ocpp15RequestHandlerTest {

    private Ocpp15RequestHandler requestHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    private ChargingStationOcpp15Client client;

    private DomainService domainService;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        requestHandler = new Ocpp15RequestHandler();

        domainService = mock(DomainService.class);
        when(domainService.generateReservationIdentifier(any(ChargingStationId.class), any(String.class))).thenReturn(new NumberedReservationId(CHARGING_STATION_ID, PROTOCOL, 1));
        requestHandler.setDomainService(domainService);

        client = mock(ChargingStationOcpp15Client.class);
        requestHandler.setChargingStationOcpp15Client(client);
    }

    @Test
    public void testConfigurationRequestedEvent() {
        requestHandler.handle(new ConfigurationRequestedEvent(CHARGING_STATION_ID, PROTOCOL));

        verify(client).getConfiguration(CHARGING_STATION_ID);
    }

    @Test
    public void testStartTransactionRequestedEvent() {
        requestHandler.handle(new StartTransactionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, IDENTIFYING_TOKEN, EVSE_ID));

        verify(client).startTransaction(CHARGING_STATION_ID, IDENTIFYING_TOKEN, EVSE_ID);
    }

    @Test
    public void testStopTransactionRequestedEvent() {
        requestHandler.handle(new StopTransactionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, TRANSACTION_ID));

        verify(client).stopTransaction(CHARGING_STATION_ID, ((NumberedTransactionId) TRANSACTION_ID).getNumber());
    }

    @Test
    public void noTransactionStoppedIfTransactionIdIsIncorrectType() {
        requestHandler.handle(new StopTransactionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, new UuidTransactionId()));

        verifyZeroInteractions(client);
    }

    @Test
    public void testRequestSoftResetChargingStationEvent() {
        requestHandler.handle(new SoftResetChargingStationRequestedEvent(CHARGING_STATION_ID, PROTOCOL));

        verify(client).softReset(CHARGING_STATION_ID);
    }

    @Test
    public void testRequestHardResetChargingStationEvent() {
        requestHandler.handle(new HardResetChargingStationRequestedEvent(CHARGING_STATION_ID, PROTOCOL));

        verify(client).hardReset(CHARGING_STATION_ID);
    }

    @Test
    public void testUnlockEvseRequestedEvent() {
        requestHandler.handle(new UnlockEvseRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID));

        verify(client).unlockConnector(CHARGING_STATION_ID, EVSE_ID);
    }

    @Test
    public void testChangeChargingStationAvailabilityToInoperativeRequested() {
        requestHandler.handle(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID));

        verify(client).changeAvailabilityToInoperative(CHARGING_STATION_ID, EVSE_ID);
    }

    @Test
    public void testChangeChargingStationAvailabilityToOperativeRequested() {
        requestHandler.handle(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID));

        verify(client).changeAvailabilityToOperative(CHARGING_STATION_ID, EVSE_ID);
    }

    @Test
    public void testDiagnosticsRequestedEvent() {
        String uploadLocation = "ftp://abc.com/xyz";
        requestHandler.handle(new DiagnosticsRequestedEvent(CHARGING_STATION_ID, PROTOCOL, uploadLocation));

        verify(client).getDiagnostics(CHARGING_STATION_ID, uploadLocation, null, null, null, null);
    }

    @Test
    public void testReserveNowRequestedEvent() {
        Date expiryDate = new Date();
        requestHandler.handle(new ReserveNowRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, null));

        verify(client).reserveNow(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, null, RESERVATION_ID.getNumber());
    }

    @Test
    public void testDataTransferEvent() {
        requestHandler.handle(new DataTransferEvent(CHARGING_STATION_ID, PROTOCOL, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA));

        verify(client).dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA);
    }

    @Test
    public void testChangeConfigurationEvent() {
        requestHandler.handle(new ChangeConfigurationEvent(CHARGING_STATION_ID, PROTOCOL, getConfigurationKey(), getConfigurationValue()));

        verify(client).changeConfiguration(CHARGING_STATION_ID, getConfigurationKey(), getConfigurationValue());
    }

    @Test
    public void testClearCacheRequestedEvent() {
        requestHandler.handle(new ClearCacheRequestedEvent(CHARGING_STATION_ID, PROTOCOL));

        verify(client).clearCache(CHARGING_STATION_ID);
    }

    @Test
    public void testFirmwareUpdateRequestedEvent() {
        Date retrievedDate = new Date();
        Map<String, String> attributes = new HashMap<>();
        requestHandler.handle(new FirmwareUpdateRequestedEvent(CHARGING_STATION_ID, PROTOCOL, getFirmwareUpdateLocation(), retrievedDate, attributes));

        verify(client).updateFirmware(CHARGING_STATION_ID, getFirmwareUpdateLocation(), retrievedDate, null, null);

        requestHandler.handle(new FirmwareUpdateRequestedEvent(CHARGING_STATION_ID, PROTOCOL, getFirmwareUpdateLocation(), retrievedDate, getUpdateFirmwareAttributes(Integer.toString(NUMBER_OF_RETRIES), Integer.toString(RETRY_INTERVAL))));

        verify(client).updateFirmware(CHARGING_STATION_ID, getFirmwareUpdateLocation(), retrievedDate, NUMBER_OF_RETRIES, RETRY_INTERVAL);
    }

    @Test
    public void testAuthorizationListVersionRequestedEvent() {
        when(client.getAuthorizationListVersion(CHARGING_STATION_ID)).thenReturn(LIST_VERSION);
        requestHandler.handle(new AuthorizationListVersionRequestedEvent(CHARGING_STATION_ID, PROTOCOL));

        verify(domainService).authorizationListVersionReceived(CHARGING_STATION_ID, LIST_VERSION);
    }

    @Test
    public void testSendAuthorizationListRequestedEvent() {
        requestHandler.handle(new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, PROTOCOL, getAuthorizationList(), getAuthorizationListVersion(), getAuthorizationListHash(), getAuthorizationListUpdateType()));

        verify(client).sendAuthorizationList(CHARGING_STATION_ID, getAuthorizationListHash(), getAuthorizationListVersion(), getAuthorizationList(), getAuthorizationListUpdateType());
    }

}
