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
package io.motown.domain.chargingstation;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static io.motown.domain.chargingstation.ChargingStationTestUtils.*;

public class ChargingStationTest {

    private FixtureConfiguration<ChargingStation> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
    }

    @Test
    public void testBootingCreatedChargingStation() {
        fixture.given(getCreatedChargingStation(false))
               .when(new BootChargingStationCommand(getChargingStationId(), getProtocol(), getAttributes()))
               .expectEvents(new UnconfiguredChargingStationBootedEvent(getChargingStationId(), getProtocol(), getAttributes()));
    }

    @Test
    public void testBootingRegisteredChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new BootChargingStationCommand(getChargingStationId(), getProtocol(), getAttributes()))
               .expectEvents(new UnconfiguredChargingStationBootedEvent(getChargingStationId(), getProtocol(), getAttributes()));
    }

    @Test
    public void testBootingConfiguredChargingStation() {
        fixture.given(getChargingStation())
               .when(new BootChargingStationCommand(getChargingStationId(), getProtocol(), getAttributes()))
               .expectEvents(new ConfiguredChargingStationBootedEvent(getChargingStationId(), getProtocol(), getAttributes()));
    }

    @Test
    public void testHeartbeat() {
        fixture.given(getChargingStation())
                .when(new HeartbeatCommand(getChargingStationId()))
                .expectEvents(new ChargingStationSentHeartbeatEvent(getChargingStationId()));
    }

    @Test
    public void testMultipleMeterValues() {
        List<MeterValue> meterValues = getMeterValues();

        fixture.given(getChargingStation())
                .when(new ProcessMeterValueCommand(getChargingStationId(), getNumberedTransactionId(), getConnectorId(), meterValues))
                .expectEvents(new ChargingStationSentMeterValuesEvent(getChargingStationId(), getNumberedTransactionId(), getConnectorId(), meterValues));
    }

    @Test
    public void testNoMeterValues() {
        List<MeterValue> meterValues = null;

        fixture.given(getChargingStation())
                .when(new ProcessMeterValueCommand(getChargingStationId(), getNumberedTransactionId(), getConnectorId(), meterValues))
                .expectEvents(new ChargingStationSentMeterValuesEvent(getChargingStationId(), getNumberedTransactionId(), getConnectorId(), meterValues));
    }

    @Test
    public void testMeterValuesNoTransaction() {
        List<MeterValue> meterValues = getMeterValues();
        TransactionId transactionId = null;

        fixture.given(getChargingStation())
                .when(new ProcessMeterValueCommand(getChargingStationId(), transactionId, getConnectorId(), meterValues))
                .expectEvents(new ChargingStationSentMeterValuesEvent(getChargingStationId(), transactionId, getConnectorId(), meterValues));
    }

    @Test
    public void testRegisteringUnacceptedChargingStation() {
        fixture.given(getCreatedChargingStation(false))
               .when(new AcceptChargingStationCommand(getChargingStationId()))
               .expectEvents(new ChargingStationAcceptedEvent(getChargingStationId()));
    }

    @Test
    public void testRegisteringAcceptedChargingStation() {
        fixture.given(getCreatedChargingStation(true))
               .when(new AcceptChargingStationCommand(getChargingStationId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRegisteringNonExistentChargingStation() {
        fixture.given()
               .when(new AcceptChargingStationCommand(getChargingStationId()))
               .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void testRegisteringAlreadyRegisteredChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new AcceptChargingStationCommand(getChargingStationId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testChargePointCreation() {
        fixture.given()
               .when(new CreateChargingStationCommand(getChargingStationId()))
               .expectEvents(new ChargingStationCreatedEvent(getChargingStationId()));
    }

    @Test
    public void testCreatingAndAcceptingChargingStation() {
        fixture.given()
               .when(new CreateAndAcceptChargingStationCommand(getChargingStationId()))
               .expectEvents(new ChargingStationCreatedEvent(getChargingStationId()),
                       new ChargingStationAcceptedEvent(getChargingStationId()));
    }

    @Test
    public void testChargeAcceptance() {
        fixture.given(new ChargingStationCreatedEvent(getChargingStationId()))
                .when(new AcceptChargingStationCommand(getChargingStationId()))
                .expectEvents(new ChargingStationAcceptedEvent(getChargingStationId()));
    }

    @Test
    public void testRequestConfigurationForUnconfiguredChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new RequestConfigurationCommand(getChargingStationId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfigurationForUnregisteredChargingStation() {
        fixture.given(getConfiguredChargingStation(false))
               .when(new RequestConfigurationCommand(getChargingStationId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfiguration() {
        fixture.given(getChargingStation())
               .when(new RequestConfigurationCommand(getChargingStationId()))
               .expectEvents(new ConfigurationRequestedEvent(getChargingStationId(), getProtocol()));
    }

    @Test
    public void testStartTransactionEmptyAttributes() {
        Date now = new Date();
        ConnectorId connectorId = new ConnectorId(1);
        int meterStart = 0;

        fixture.given(getChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), connectorId, getTextualToken(), meterStart, now))
                .expectEvents(new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), connectorId, getTextualToken(), meterStart, now, getEmptyAttributesMap()));
    }

    @Test
    public void testStartTransactionFilledAttributes() {
        Date now = new Date();
        ConnectorId connectorId = new ConnectorId(1);
        int meterStart = 0;

        fixture.given(getChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), connectorId, getTextualToken(), meterStart, now, getAttributes()))
                .expectEvents(new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), connectorId, getTextualToken(), meterStart, now, getAttributes()));
    }

    @Test
    public void testConfigureChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new ConfigureChargingStationCommand(getChargingStationId(), getConnectors(), getConfigurationItems()))
               .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), getConfigurationItems()));
    }

    @Test
    public void testConfigureChargingStationWithoutConnectors() {
        fixture.given(getRegisteredChargingStation())
               .when(new ConfigureChargingStationCommand(getChargingStationId(), getConfigurationItems()))
               .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), Collections.<Connector>emptySet(), getConfigurationItems()));
    }

    @Test
    public void testConfigureChargingStationWithoutConfigurationItems() {
        fixture.given(getRegisteredChargingStation())
               .when(new ConfigureChargingStationCommand(getChargingStationId(), getConnectors()))
               .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), getConnectors(), Collections.<String, String>emptyMap()));
    }

    @Test
    public void testRequestingToUnlockConnectorForUnregisteredChargingStation() {
        fixture.given(getConfiguredChargingStation(false))
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), getConnectorId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToUnlockConnectorForUnconfiguredChargingStation() {
        fixture.given(getRegisteredChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), getConnectorId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToStartTransactionForUnconfiguredChargingStation() {
        fixture.given(getRegisteredChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), getConnectorId(), getTextualToken(), 0, new Date()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToUnlockConnector() {
        fixture.given(getChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), getConnectorId()))
                .expectEvents(new UnlockConnectorRequestedEvent(getChargingStationId(), getProtocol(), getConnectorId()));
    }

    @Test
    public void testRequestingToUnlockUnknownConnector() {
        fixture.given(getChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), new ConnectorId(3)))
                .expectEvents(new ConnectorNotFoundEvent(getChargingStationId(), new ConnectorId(3)));
    }

    @Test
    public void testStartTransactionOnUnknownConnector() {
        fixture.given(getChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), new ConnectorId(3), getTextualToken(), 0, new Date()))
                .expectEvents(new ConnectorNotFoundEvent(getChargingStationId(), new ConnectorId(3)));
    }

    @Test
    public void testRequestingToUnlockAllConnectors() {
        fixture.given(getChargingStation())
                .when(new RequestUnlockConnectorCommand(getChargingStationId(), Connector.ALL))
                .expectEvents(new UnlockConnectorRequestedEvent(getChargingStationId(), getProtocol(), new ConnectorId(1)),
                        new UnlockConnectorRequestedEvent(getChargingStationId(), getProtocol(), new ConnectorId(2)));
    }

    @Test
    public void testRequestSoftResetChargingStation() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestSoftResetChargingStationCommand(getChargingStationId()))
                .expectEvents(new SoftResetChargingStationRequestedEvent(getChargingStationId(), getProtocol()));
    }

    @Test
    public void testRequestHardResetChargingStation() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestHardResetChargingStationCommand(getChargingStationId()))
                .expectEvents(new HardResetChargingStationRequestedEvent(getChargingStationId(), getProtocol()));
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToInoperative() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestChangeChargingStationAvailabilityToInoperativeCommand(getChargingStationId(), getConnectorId()))
                .expectEvents(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(getChargingStationId(), getProtocol(), getConnectorId()));
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToOperative() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestChangeChargingStationAvailabilityToOperativeCommand(getChargingStationId(), getConnectorId()))
                .expectEvents(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(getChargingStationId(), getProtocol(), getConnectorId()));
    }

    @Test
    public void testDataTransfer() {
        String messageId = "updateChargeSchema";
        String data = "{'schema' : [{'offset':'2013-07-19T12:47:11.5959704Z', 'power':0.0}, {'offset':'2013-07-19T12:52:11.5959704Z'}]}";
        fixture.given(getConfiguredChargingStation(true))
                .when(new DataTransferCommand(getChargingStationId(), getVendorId(), messageId, data))
                .expectEvents(new DataTransferEvent(getChargingStationId(), getProtocol(), getVendorId(), messageId, data));
    }

    @Test
    public void testGetDiagnostics() {
        String targetLocation = "ftp://abc.com/";
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestDiagnosticsCommand(getChargingStationId(), targetLocation, null, null, null, null))
                .expectEvents(new DiagnosticsRequestedEvent(getChargingStationId(), getProtocol(), targetLocation));
    }

    @Test
    public void testGetDiagnosticsFileNameReceived() {
        String diagnosticsFileName = "diagnostics.zip";
        fixture.given(getConfiguredChargingStation(true))
                .when(new DiagnosticsFileNameReceivedCommand(getChargingStationId(), diagnosticsFileName))
                .expectEvents(new DiagnosticsFileNameReceivedEvent(getChargingStationId(), diagnosticsFileName));
    }

    @Test
    public void testDiagnosticsStatusUpdate() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new UpdateDiagnosticsUploadStatusCommand(getChargingStationId(), true))
                .expectEvents(new DiagnosticsUploadStatusUpdatedEvent(getChargingStationId(), true));
    }

    @Test
    public void testFirmwareStatusUpdate() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new UpdateFirmwareStatusCommand(getChargingStationId(), FirmwareStatus.DOWNLOAD_FAILED))
                .expectEvents(new FirmwareStatusUpdatedEvent(getChargingStationId(), FirmwareStatus.DOWNLOAD_FAILED));
    }

    @Test
    public void testClearCache() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestClearCacheCommand(getChargingStationId()))
                .expectEvents(new ClearCacheRequestedEvent(getChargingStationId(), getProtocol()));
    }

    @Test
    public void testRequestFirmwareUpdate() {
        String updateLocation = "https://somewhere.nl";
        Date retrieveDate = new Date();

        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestFirmwareUpdateCommand(getChargingStationId(), updateLocation, retrieveDate, null))
                .expectEvents(new FirmwareUpdateRequestedEvent(getChargingStationId(), getProtocol(), updateLocation, retrieveDate, null));
    }

    @Test
    public void testGetAuthorisationListVersionRequest() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestAuthorisationListVersionCommand(getChargingStationId()))
                .expectEvents(new AuthorisationListVersionRequestedEvent(getChargingStationId(), getProtocol()));
    }

    @Test
    public void testGetAuthorisationListVersionReceived() {
        int version = 1;
        fixture.given(getConfiguredChargingStation(true))
                .when(new AuthorisationListVersionReceivedCommand(getChargingStationId(), version))
                .expectEvents(new AuthorisationListVersionReceivedEvent(getChargingStationId(), version));
    }

    @Test
    public void testRequestReserveNow() {
        Date expiryDate = new Date();
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestReserveNowCommand(getChargingStationId(), getConnectorId(), getTextualToken(), expiryDate, getTextualToken()))
                .expectEvents(new ReserveNowRequestedEvent(getChargingStationId(), getProtocol(), getConnectorId(), getTextualToken(), expiryDate, getTextualToken()));
    }

    @Test
    public void testRequestCancelReservation() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestCancelReservationCommand(getChargingStationId(), getReservationId()))
                .expectEvents(new CancelReservationRequestedEvent(getChargingStationId(), getProtocol(), getReservationId()));
    }

    @Test
    public void testReservationStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new ReservationStatusChangedCommand(getChargingStationId(), getReservationId(), getReservationStatus()))
                .expectEvents(new ReservationStatusChangedEvent(getChargingStationId(), getReservationId(), getReservationStatus()));
    }

    @Test
    public void testIncomingDataTransfer() {
        String messageId = "MessageId";
        String dataToTransfer = "Data to transfer";
        fixture.given(getConfiguredChargingStation(true))
                .when(new IncomingDataTransferCommand(getChargingStationId(), getVendorId(), messageId, dataToTransfer))
                .expectEvents(new IncomingDataTransferReceivedEvent(getChargingStationId(), getVendorId(), messageId, dataToTransfer));
    }

    @Test
    public void testChargingStationStatusNotification() {
        Date timeStamp = new Date();

        fixture.given(getConfiguredChargingStation(true))
                .when(new ChargingStationStatusNotificationCommand(getChargingStationId(), ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()))
                .expectEvents(new ChargingStationStatusNotificationReceivedEvent(getChargingStationId(), ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()));
    }

    @Test
    public void testComponentStatusNotification() {
        Date timeStamp = new Date();

        fixture.given(getConfiguredChargingStation(true))
                .when(new ComponentStatusNotificationCommand(getChargingStationId(), ChargingStationComponent.CONNECTOR, getConnectorId(), ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()))
                .expectEvents(new ComponentStatusNotificationReceivedEvent(getChargingStationId(), ChargingStationComponent.CONNECTOR, getConnectorId(), ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()));
    }

    @Test
    public void testClearCacheRequestStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new ClearCacheStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new ClearCacheStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testChangeAvailabilityToInoperativeStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new ChangeAvailabilityToInoperativeStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new ChangeAvailabilityToInoperativeStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testChangeAvailabilityToOperativeStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new ChangeAvailabilityToOperativeStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new ChangeAvailabilityToOperativeStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testChangeConfigurationStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new ChangeConfigurationStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new ChangeConfigurationStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testDataTransferStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new DataTransferStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new DataTransferStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testHardResetStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new HardResetStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new HardResetStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testSendAuthorisationListStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new SendAuthorisationListStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new SendAuthorisationListStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testSoftResetStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new SoftResetStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new SoftResetStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testStartTransactionStatusChangedCommand() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new StartTransactionStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new StartTransactionStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testStopTransactionStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new StopTransactionStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new StopTransactionStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testUnlockConnectorStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new UnlockConnectorStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new UnlockConnectorStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }
}
