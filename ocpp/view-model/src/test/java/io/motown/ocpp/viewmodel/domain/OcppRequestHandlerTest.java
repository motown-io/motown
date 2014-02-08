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

import static io.motown.ocpp.viewmodel.domain.TestUtils.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OcppRequestHandlerTest {

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
        when(domainService.generateReservationIdentifier(any(ChargingStationId.class), any(String.class))).thenReturn(new NumberedReservationId(getChargingStationId(), getProtocol(), 1));
        requestHandler.setDomainService(domainService);

        client = mock(ChargingStationOcpp15Client.class);
        requestHandler.setChargingStationOcpp15Client(client);
    }

    @Test
    public void testConfigurationRequestedEvent() {
        requestHandler.handle(new ConfigurationRequestedEvent(getChargingStationId(), getProtocol()));

        verify(client).getConfiguration(getChargingStationId());
    }

    @Test
    public void testStartTransactionRequestedEvent() {
        requestHandler.handle(new StartTransactionRequestedEvent(getChargingStationId(), getProtocol(), getIdentifyingToken(), getEvseId()));

        verify(client).startTransaction(getChargingStationId(), getIdentifyingToken(), getEvseId());
    }

    @Test
    public void testStopTransactionRequestedEvent() {
        requestHandler.handle(new StopTransactionRequestedEvent(getChargingStationId(), getProtocol(), getNumberedTransactionId()));

        verify(client).stopTransaction(getChargingStationId(), getNumberedTransactionId().getNumber());
    }

    @Test
    public void noTransactionStoppedIfTransactionIdIsIncorrectType() {
        requestHandler.handle(new StopTransactionRequestedEvent(getChargingStationId(), getProtocol(), new UuidTransactionId()));

        verifyZeroInteractions(client);
    }

    @Test
    public void testRequestSoftResetChargingStationEvent() {
        requestHandler.handle(new SoftResetChargingStationRequestedEvent(getChargingStationId(), getProtocol()));

        verify(client).softReset(getChargingStationId());
    }

    @Test
    public void testRequestHardResetChargingStationEvent() {
        requestHandler.handle(new HardResetChargingStationRequestedEvent(getChargingStationId(), getProtocol()));

        verify(client).hardReset(getChargingStationId());
    }

    @Test
    public void testUnlockEvseRequestedEvent() {
        requestHandler.handle(new UnlockEvseRequestedEvent(getChargingStationId(), getProtocol(), getEvseId()));

        verify(client).unlockConnector(getChargingStationId(), getEvseId());
    }

    @Test
    public void testChangeChargingStationAvailabilityToInoperativeRequested() {
        requestHandler.handle(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(getChargingStationId(), getProtocol(), getEvseId()));

        verify(client).changeAvailabilityToInoperative(getChargingStationId(), getEvseId());
    }

    @Test
    public void testChangeChargingStationAvailabilityToOperativeRequested() {
        requestHandler.handle(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(getChargingStationId(), getProtocol(), getEvseId()));

        verify(client).changeAvailabilityToOperative(getChargingStationId(), getEvseId());
    }

    @Test
    public void testDiagnosticsRequestedEvent() {
        String uploadLocation = "ftp://abc.com/xyz";
        requestHandler.handle(new DiagnosticsRequestedEvent(getChargingStationId(), getProtocol(), uploadLocation));

        verify(client).getDiagnostics(getChargingStationId(), uploadLocation, null, null, null, null);
    }

    @Test
    public void testReserveNowRequestedEvent() {
        Date expiryDate = new Date();
        requestHandler.handle(new ReserveNowRequestedEvent(getChargingStationId(), getProtocol(), getEvseId(), getIdentifyingToken(), expiryDate, null));

        verify(client).reserveNow(getChargingStationId(), getEvseId(), getIdentifyingToken(), expiryDate, null, getReservationNumber());
    }

    @Test
    public void testDataTransferEvent() {
        requestHandler.handle(new DataTransferEvent(getChargingStationId(), getProtocol(), getVendor(), getMessageId(), getData()));

        verify(client).dataTransfer(getChargingStationId(), getVendor(), getMessageId(), getData());
    }

    @Test
    public void testChangeConfigurationEvent() {
        requestHandler.handle(new ChangeConfigurationEvent(getChargingStationId(), getProtocol(), getConfigurationKey(), getConfigurationValue()));

        verify(client).changeConfiguration(getChargingStationId(), getConfigurationKey(), getConfigurationValue());
    }

    @Test
    public void testClearCacheRequestedEvent() {
        requestHandler.handle(new ClearCacheRequestedEvent(getChargingStationId(), getProtocol()));

        verify(client).clearCache(getChargingStationId());
    }

    @Test
    public void testFirmwareUpdateRequestedEvent() {
        Date retrievedDate = new Date();
        Map<String, String> attributes = new HashMap<>();
        requestHandler.handle(new FirmwareUpdateRequestedEvent(getChargingStationId(), getProtocol(), getFirmwareUpdateLocation(), retrievedDate, attributes));

        verify(client).updateFirmware(getChargingStationId(), getFirmwareUpdateLocation(), retrievedDate, null, null);

        requestHandler.handle(new FirmwareUpdateRequestedEvent(getChargingStationId(), getProtocol(), getFirmwareUpdateLocation(), retrievedDate, getUpdateFirmwareAttributes(Integer.toString(NUMBER_OF_RETRIES), Integer.toString(RETRY_INTERVAL))));

        verify(client).updateFirmware(getChargingStationId(), getFirmwareUpdateLocation(), retrievedDate, NUMBER_OF_RETRIES, RETRY_INTERVAL);
    }

    @Test
    public void testAuthorizationListVersionRequestedEvent() {
        when(client.getAuthorizationListVersion(getChargingStationId())).thenReturn(LIST_VERSION);
        requestHandler.handle(new AuthorizationListVersionRequestedEvent(getChargingStationId(), getProtocol()));

        verify(domainService).authorizationListVersionReceived(getChargingStationId(), LIST_VERSION);
    }

    @Test
    public void testSendAuthorizationListRequestedEvent() {
        requestHandler.handle(new SendAuthorizationListRequestedEvent(getChargingStationId(), getProtocol(), getAuthorizationList(), getAuthorizationListVersion(), getAuthorizationListHash(), getAuthorizationListUpdateType()));

        verify(client).sendAuthorizationList(getChargingStationId(), getAuthorizationListHash(), getAuthorizationListVersion(), getAuthorizationList(), getAuthorizationListUpdateType());
    }

}
