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

import java.util.*;

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
                .when(new ProcessMeterValueCommand(getChargingStationId(), getNumberedTransactionId(), getEvseId(), meterValues))
                .expectEvents(new ChargingStationSentMeterValuesEvent(getChargingStationId(), getNumberedTransactionId(), getEvseId(), meterValues));
    }

    @Test
    public void testNoMeterValues() {
        List<MeterValue> meterValues = new ArrayList<>();

        fixture.given(getChargingStation())
                .when(new ProcessMeterValueCommand(getChargingStationId(), getNumberedTransactionId(), getEvseId(), meterValues))
                .expectEvents(new ChargingStationSentMeterValuesEvent(getChargingStationId(), getNumberedTransactionId(), getEvseId(), meterValues));
    }

    @Test
    public void testMeterValuesNoTransaction() {
        List<MeterValue> meterValues = getMeterValues();
        TransactionId transactionId = null;

        fixture.given(getChargingStation())
                .when(new ProcessMeterValueCommand(getChargingStationId(), transactionId, getEvseId(), meterValues))
                .expectEvents(new ChargingStationSentMeterValuesEvent(getChargingStationId(), transactionId, getEvseId(), meterValues));
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
    public void testRequestStartTransaction() {
        fixture.given(getRegisteredChargingStation())
                .when(new RequestStartTransactionCommand(getChargingStationId(), getTextualToken(), getEvseId()))
                .expectEvents(new StartTransactionRequestedEvent(getChargingStationId(), getProtocol(), getTextualToken(), getEvseId()));
    }

    @Test
    public void testRequestStopTransaction() {
        fixture.given(getRegisteredChargingStation())
                .when(new RequestStopTransactionCommand(getChargingStationId(), getNumberedTransactionId()))
                .expectEvents(new StopTransactionRequestedEvent(getChargingStationId(), getProtocol(), getNumberedTransactionId()));
    }

    @Test
    public void testStartTransactionEmptyAttributes() {
        Date now = new Date();
        EvseId evseId = new EvseId(1);
        int meterStart = 0;

        fixture.given(getChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), evseId, getTextualToken(), meterStart, now))
                .expectEvents(new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), evseId, getTextualToken(), meterStart, now, getEmptyAttributesMap()));
    }

    @Test
    public void testStartTransactionFilledAttributes() {
        Date now = new Date();
        EvseId evseId = new EvseId(1);
        int meterStart = 0;

        fixture.given(getChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), evseId, getTextualToken(), meterStart, now, getAttributes()))
                .expectEvents(new TransactionStartedEvent(getChargingStationId(), getNumberedTransactionId(), evseId, getTextualToken(), meterStart, now, getAttributes()));
    }

    @Test
    public void testConfigureChargingStation() {
        fixture.given(getRegisteredChargingStation())
               .when(new ConfigureChargingStationCommand(getChargingStationId(), getEvses(), getConfigurationItems()))
               .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), getEvses(), getConfigurationItems()));
    }

    @Test
    public void testConfigureChargingStationWithoutEvses() {
        fixture.given(getRegisteredChargingStation())
               .when(new ConfigureChargingStationCommand(getChargingStationId(), getConfigurationItems()))
               .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), Collections.<Evse>emptySet(), getConfigurationItems()));
    }

    @Test
    public void testConfigureChargingStationWithoutConfigurationItems() {
        fixture.given(getRegisteredChargingStation())
               .when(new ConfigureChargingStationCommand(getChargingStationId(), getEvses()))
               .expectEvents(new ChargingStationConfiguredEvent(getChargingStationId(), getEvses(), Collections.<String, String>emptyMap()));
    }

    @Test
    public void testRequestingToUnlockEvseForUnregisteredChargingStation() {
        fixture.given(getConfiguredChargingStation(false))
                .when(new RequestUnlockEvseCommand(getChargingStationId(), getEvseId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToUnlockEvseForUnconfiguredChargingStation() {
        fixture.given(getRegisteredChargingStation())
                .when(new RequestUnlockEvseCommand(getChargingStationId(), getEvseId()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToStartTransactionForUnconfiguredChargingStation() {
        fixture.given(getRegisteredChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), getEvseId(), getTextualToken(), 0, new Date()))
               .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToUnlockEvse() {
        fixture.given(getChargingStation())
                .when(new RequestUnlockEvseCommand(getChargingStationId(), getEvseId()))
                .expectEvents(new UnlockEvseRequestedEvent(getChargingStationId(), getProtocol(), getEvseId()));
    }

    @Test
    public void testRequestingToUnlockUnknownEvse() {
        fixture.given(getChargingStation())
                .when(new RequestUnlockEvseCommand(getChargingStationId(), UNKNOWN_EVSE_ID))
                .expectEvents(new EvseNotFoundEvent(getChargingStationId(), UNKNOWN_EVSE_ID));
    }

    @Test
    public void testStartTransactionOnUnknownEvse() {
        fixture.given(getChargingStation())
                .when(new StartTransactionCommand(getChargingStationId(), getNumberedTransactionId(), UNKNOWN_EVSE_ID, getTextualToken(), 0, new Date()))
                .expectEvents(new EvseNotFoundEvent(getChargingStationId(), UNKNOWN_EVSE_ID));
    }

    @Test
    public void testRequestingToUnlockAllEvses() {
        fixture.given(getChargingStation())
                .when(new RequestUnlockEvseCommand(getChargingStationId(), Evse.ALL))
                .expectEvents(new UnlockEvseRequestedEvent(getChargingStationId(), getProtocol(), new EvseId(1)),
                        new UnlockEvseRequestedEvent(getChargingStationId(), getProtocol(), new EvseId(2)));
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
                .when(new RequestChangeChargingStationAvailabilityToInoperativeCommand(getChargingStationId(), getEvseId()))
                .expectEvents(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(getChargingStationId(), getProtocol(), getEvseId()));
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToOperative() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestChangeChargingStationAvailabilityToOperativeCommand(getChargingStationId(), getEvseId()))
                .expectEvents(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(getChargingStationId(), getProtocol(), getEvseId()));
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
    public void testGetDiagnosticsEmptyFileNameReceived() {
        String diagnosticsFileName = "";
        fixture.given(getConfiguredChargingStation(true))
                .when(new DiagnosticsFileNameReceivedCommand(getChargingStationId(), diagnosticsFileName))
                .expectEvents(new NoDiagnosticsInformationAvailableEvent(getChargingStationId()));
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
        Map<String, String> attributes = new HashMap<>();

        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestFirmwareUpdateCommand(getChargingStationId(), updateLocation, retrieveDate, attributes))
                .expectEvents(new FirmwareUpdateRequestedEvent(getChargingStationId(), getProtocol(), updateLocation, retrieveDate, attributes));
    }

    @Test
    public void testGetAuthorizationListVersionRequest() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestAuthorizationListVersionCommand(getChargingStationId()))
                .expectEvents(new AuthorizationListVersionRequestedEvent(getChargingStationId(), getProtocol()));
    }

    @Test
    public void testGetAuthorizationListVersionReceived() {
        int version = 1;
        fixture.given(getConfiguredChargingStation(true))
                .when(new AuthorizationListVersionReceivedCommand(getChargingStationId(), version))
                .expectEvents(new AuthorizationListVersionReceivedEvent(getChargingStationId(), version));
    }

    @Test
    public void testRequestReserveNowReservableChargingStation() {
        Date expiryDate = new Date();
        fixture.given(getConfiguredReservableChargingStation(true))
                .when(new RequestReserveNowCommand(getChargingStationId(), getEvseId(), getTextualToken(), expiryDate, getTextualToken()))
                .expectEvents(new ReserveNowRequestedEvent(getChargingStationId(), getProtocol(), getEvseId(), getTextualToken(), expiryDate, getTextualToken()));
    }

    @Test
    public void testRequestReserveNowNotReservableChargingStation() {
        Date expiryDate = new Date();
        fixture.given(getConfiguredChargingStation(true))
                .when(new RequestReserveNowCommand(getChargingStationId(), getEvseId(), getTextualToken(), expiryDate, getTextualToken()))
                .expectEvents(new ReserveNowRequestedForUnreservableChargingStationEvent(getChargingStationId(), getEvseId(), getTextualToken(), expiryDate, getTextualToken()));
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
                .when(new ComponentStatusNotificationCommand(getChargingStationId(), ChargingStationComponent.CONNECTOR, getEvseId(), ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()))
                .expectEvents(new ComponentStatusNotificationReceivedEvent(getChargingStationId(), ChargingStationComponent.CONNECTOR, getEvseId(), ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()));
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
    public void testChangeConfiguration() {
        String configKey = "heartbeatInterval";
        String configValue = "800";
        fixture.given(getConfiguredChargingStation(true))
                .when(new ChangeConfigurationCommand(getChargingStationId(), configKey, configValue))
                .expectEvents(new ChangeConfigurationEvent(getChargingStationId(), getProtocol(), configKey, configValue));
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
    public void testSendAuthorizationListStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new SendAuthorizationListStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new SendAuthorizationListStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
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
    public void testStopTransaction() {
        Date now = new Date();
        fixture.given(getConfiguredChargingStation(true))
                .when(new StopTransactionCommand(getChargingStationId(), getNumberedTransactionId(), getTextualToken(), METER_STOP, now))
                .expectEvents(new TransactionStoppedEvent(getChargingStationId(), getNumberedTransactionId(), getTextualToken(), METER_STOP, now));
    }

    @Test
    public void testStopTransactionStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new StopTransactionStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new StopTransactionStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testUnlockEvseStatusChanged() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new UnlockEvseStatusChangedCommand(getChargingStationId(), RequestStatus.SUCCESS))
                .expectEvents(new UnlockEvseStatusChangedEvent(getChargingStationId(), RequestStatus.SUCCESS));
    }

    @Test
    public void testAuthorize() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new AuthorizeCommand(getChargingStationId(), getTextualToken()))
                .expectEvents(new AuthorizationRequestedEvent(getChargingStationId(), getTextualToken()));
    }

    @Test
    public void testGrantAuthorization() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new GrantAuthorizationCommand(getChargingStationId(), getTextualToken()))
                .expectEvents(new AuthorizationResultEvent(getChargingStationId(), getTextualToken(), AuthorizationResultStatus.ACCEPTED));
    }

    @Test
    public void testDenyAuthorization() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new DenyAuthorizationCommand(getChargingStationId(), getTextualToken()))
                .expectEvents(new AuthorizationResultEvent(getChargingStationId(), getTextualToken(), AuthorizationResultStatus.INVALID));
    }

    @Test
    public void testMakeChargingStationReservable() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new MakeChargingStationReservableCommand(getChargingStationId()))
                .expectEvents(new ChargingStationMadeReservableEvent(getChargingStationId()));
    }

    @Test
    public void testMakeChargingStationNotReservable() {
        fixture.given(getConfiguredChargingStation(true))
                .when(new MakeChargingStationNotReservableCommand(getChargingStationId()))
                .expectEvents(new ChargingStationMadeNotReservableEvent(getChargingStationId()));
    }

}
