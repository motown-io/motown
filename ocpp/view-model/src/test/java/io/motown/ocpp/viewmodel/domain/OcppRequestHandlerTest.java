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
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.ocpp.viewmodel.domain.TestUtils.*;
import static org.mockito.Mockito.*;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OcppRequestHandlerTest {

    private OcppRequestHandler requestHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    ChargingStationOcpp15Client client;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        requestHandler = new OcppRequestHandler();
        requestHandler.domainService = mock(DomainService.class);

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
        requestHandler.handle(new StartTransactionRequestedEvent(getChargingStationId(), getProtocol(), getIdentifyingToken(), getConnectorId()));

        verify(client).startTransaction(getChargingStationId(), getIdentifyingToken(), getConnectorId());
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
    public void testUnlockConnectorRequestedEvent() {
        requestHandler.handle(new UnlockConnectorRequestedEvent(getChargingStationId(), getProtocol(), 1));

        verify(client).unlockConnector(getChargingStationId(), 1);
    }

    @Test
    public void testChangeChargingStationAvailabilityToInoperativeRequested() {
        requestHandler.handle(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(getChargingStationId(), getProtocol(), getConnectorId()));

        verify(client).changeAvailabilityToInoperative(getChargingStationId(), getConnectorId());
    }

    @Test
    public void testChangeChargingStationAvailabilityToOperativeRequested() {
        requestHandler.handle(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(getChargingStationId(), getProtocol(), getConnectorId()));

        verify(client).changeAvailabilityToOperative(getChargingStationId(), getConnectorId());
    }

    @Test
    public void testDiagnosticsRequestedEvent() {
        String uploadLocation = "ftp://abc.com/xyz";
        requestHandler.handle(new DiagnosticsRequestedEvent(getChargingStationId(), getProtocol(), uploadLocation));

        verify(client).getDiagnostics(getChargingStationId(), uploadLocation, null, null, null, null);
    }

}
