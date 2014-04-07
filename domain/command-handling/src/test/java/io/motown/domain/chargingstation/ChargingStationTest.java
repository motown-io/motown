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

import com.google.common.collect.ImmutableMap;
import io.motown.domain.api.chargingstation.*;
import org.axonframework.repository.AggregateNotFoundException;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;

public class ChargingStationTest {

    private FixtureConfiguration<ChargingStation> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Fixtures.newGivenWhenThenFixture(ChargingStation.class);
    }

    @Test
    public void testBootingCreatedChargingStation() {
        fixture.given(CREATED_CHARGING_STATION)
                .when(new BootChargingStationCommand(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT))
                .expectEvents(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
    }

    @Test
    public void testBootingRegisteredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new BootChargingStationCommand(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT))
                .expectEvents(new UnconfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
    }

    @Test
    public void testBootingConfiguredChargingStation() {
        fixture.given(CHARGING_STATION)
                .when(new BootChargingStationCommand(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT))
                .expectEvents(new ConfiguredChargingStationBootedEvent(CHARGING_STATION_ID, PROTOCOL, BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
    }

    @Test
    public void testHeartbeat() {
        fixture.given(CHARGING_STATION)
                .when(new HeartbeatCommand(CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationSentHeartbeatEvent(CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testMultipleMeterValues() {
        fixture.given(CHARGING_STATION)
                .when(new ProcessMeterValueCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testNoMeterValues() {
        List<MeterValue> meterValues = new ArrayList<>();

        fixture.given(CHARGING_STATION)
                .when(new ProcessMeterValueCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, meterValues, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, meterValues, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testMeterValuesNoTransaction() {
        TransactionId transactionId = null;

        fixture.given(CHARGING_STATION)
                .when(new ProcessMeterValueCommand(CHARGING_STATION_ID, transactionId, EVSE_ID, METER_VALUES, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, transactionId, EVSE_ID, METER_VALUES, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRegisteringUnacceptedChargingStation() {
        fixture.given(CREATED_CHARGING_STATION)
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRegisteringAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRegisteringNonExistentChargingStation() {
        fixture.given()
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectException(AggregateNotFoundException.class);
    }

    @Test
    public void testRegisteringAlreadyRegisteredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testChargePointCreation() {
        fixture.given()
                .when(new CreateChargingStationCommand(CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationCreatedEvent(CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testCreatingAndAcceptingChargingStation() {
        fixture.given()
                .when(new CreateAndAcceptChargingStationCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationCreatedEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT),
                        new ChargingStationAcceptedEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
    }

    @Test
    public void testChargeAcceptance() {
        fixture.given(new ChargingStationCreatedEvent(CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT))
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestConfigurationForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestConfigurationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfigurationForUnregisteredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestConfigurationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfiguration() {
        fixture.given(CHARGING_STATION)
                .when(new RequestConfigurationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ConfigurationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestStartTransaction() {
        fixture.given(CHARGING_STATION)
                .when(new RequestStartTransactionCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN, EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new StartTransactionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, IDENTIFYING_TOKEN, EVSE_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestStopTransaction() {
        fixture.given(CHARGING_STATION)
                .when(new RequestStopTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new StopTransactionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, TRANSACTION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testStartTransactionEmptyAttributes() {
        Date now = new Date();
        EvseId evseId = new EvseId(1);
        int meterStart = 0;

        fixture.given(CHARGING_STATION)
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, evseId, IDENTIFYING_TOKEN, meterStart, now, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, evseId, IDENTIFYING_TOKEN, meterStart, now, ImmutableMap.<String, String>of(), NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testStartTransactionFilledAttributes() {
        Date now = new Date();
        EvseId evseId = new EvseId(1);
        int meterStart = 0;

        fixture.given(CHARGING_STATION)
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, evseId, IDENTIFYING_TOKEN, meterStart, now, BOOT_NOTIFICATION_ATTRIBUTES, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, evseId, IDENTIFYING_TOKEN, meterStart, now, BOOT_NOTIFICATION_ATTRIBUTES, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testConfigureChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new ConfigureChargingStationCommand(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testConfigureChargingStationWithoutEvses() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new ConfigureChargingStationCommand(CHARGING_STATION_ID, CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, Collections.<Evse>emptySet(), CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testConfigureChargingStationWithoutConfigurationItems() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new ConfigureChargingStationCommand(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestingToUnlockEvseForUnregisteredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestUnlockEvseCommand(CHARGING_STATION_ID, EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToUnlockEvseForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestUnlockEvseCommand(CHARGING_STATION_ID, EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToStartTransactionForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), NULL_USER_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestingToUnlockEvse() {
        fixture.given(CHARGING_STATION)
                .when(new RequestUnlockEvseCommand(CHARGING_STATION_ID, EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new UnlockEvseRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestingToUnlockUnknownEvse() {
        fixture.given(CHARGING_STATION)
                .when(new RequestUnlockEvseCommand(CHARGING_STATION_ID, UNKNOWN_EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new EvseNotFoundEvent(CHARGING_STATION_ID, UNKNOWN_EVSE_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testStartTransactionOnUnknownEvse() {
        fixture.given(CHARGING_STATION)
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, UNKNOWN_EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new EvseNotFoundEvent(CHARGING_STATION_ID, UNKNOWN_EVSE_ID, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestingToUnlockAllEvses() {
        fixture.given(CHARGING_STATION)
                .when(new RequestUnlockEvseCommand(CHARGING_STATION_ID, Evse.ALL, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new UnlockEvseRequestedEvent(CHARGING_STATION_ID, PROTOCOL, new EvseId(1), ROOT_IDENTITY_CONTEXT),
                        new UnlockEvseRequestedEvent(CHARGING_STATION_ID, PROTOCOL, new EvseId(2), ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestSoftResetChargingStation() {
        fixture.given(CHARGING_STATION)
                .when(new RequestSoftResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new SoftResetChargingStationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestHardResetChargingStation() {
        fixture.given(CHARGING_STATION)
                .when(new RequestHardResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new HardResetChargingStationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToInoperative() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToInoperativeCommand(CHARGING_STATION_ID, EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToOperative() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToOperativeCommand(CHARGING_STATION_ID, EVSE_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testDataTransfer() {
        String messageId = "updateChargeSchema";
        String data = "{'schema' : [{'offset':'2013-07-19T12:47:11.5959704Z', 'power':0.0}, {'offset':'2013-07-19T12:52:11.5959704Z'}]}";
        fixture.given(CHARGING_STATION)
                .when(new DataTransferCommand(CHARGING_STATION_ID, CHARGING_STATION_VENDOR, messageId, data, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new DataTransferEvent(CHARGING_STATION_ID, PROTOCOL, CHARGING_STATION_VENDOR, messageId, data, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetDiagnostics() {
        String targetLocation = "ftp://abc.com/";
        fixture.given(CHARGING_STATION)
                .when(new RequestDiagnosticsCommand(CHARGING_STATION_ID, targetLocation, null, null, null, null, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new DiagnosticsRequestedEvent(CHARGING_STATION_ID, PROTOCOL, targetLocation, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetDiagnosticsFileNameReceived() {
        String diagnosticsFileName = "diagnostics.zip";
        fixture.given(CHARGING_STATION)
                .when(new DiagnosticsFileNameReceivedCommand(CHARGING_STATION_ID, diagnosticsFileName, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new DiagnosticsFileNameReceivedEvent(CHARGING_STATION_ID, diagnosticsFileName, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetDiagnosticsEmptyFileNameReceived() {
        String diagnosticsFileName = "";
        fixture.given(CHARGING_STATION)
                .when(new DiagnosticsFileNameReceivedCommand(CHARGING_STATION_ID, diagnosticsFileName, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new NoDiagnosticsInformationAvailableEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testDiagnosticsStatusUpdate() {
        fixture.given(CHARGING_STATION)
                .when(new UpdateDiagnosticsUploadStatusCommand(CHARGING_STATION_ID, true, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new DiagnosticsUploadStatusUpdatedEvent(CHARGING_STATION_ID, true));
    }

    @Test
    public void testFirmwareStatusUpdate() {
        fixture.given(CHARGING_STATION)
                .when(new UpdateFirmwareStatusCommand(CHARGING_STATION_ID, FirmwareStatus.DOWNLOAD_FAILED))
                .expectEvents(new FirmwareStatusUpdatedEvent(CHARGING_STATION_ID, FirmwareStatus.DOWNLOAD_FAILED));
    }

    @Test
    public void testClearCache() {
        fixture.given(CHARGING_STATION)
                .when(new RequestClearCacheCommand(CHARGING_STATION_ID))
                .expectEvents(new ClearCacheRequestedEvent(CHARGING_STATION_ID, PROTOCOL));
    }

    @Test
    public void testRequestFirmwareUpdate() {
        String updateLocation = "https://somewhere.nl";
        Date retrieveDate = new Date();
        Map<String, String> attributes = new HashMap<>();

        fixture.given(CHARGING_STATION)
                .when(new RequestFirmwareUpdateCommand(CHARGING_STATION_ID, updateLocation, retrieveDate, attributes))
                .expectEvents(new FirmwareUpdateRequestedEvent(CHARGING_STATION_ID, PROTOCOL, updateLocation, retrieveDate, attributes));
    }

    @Test
    public void testSendAuthorizationList() {
        final int version = 1;
        final String hash = "4894007260";

        fixture.given(CHARGING_STATION)
                .when(new SendAuthorizationListCommand(CHARGING_STATION_ID, IDENTIFYING_TOKENS, version, hash, AuthorizationListUpdateType.FULL))
                .expectEvents(new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, PROTOCOL, IDENTIFYING_TOKENS, version, hash, AuthorizationListUpdateType.FULL));
    }

    @Test
    public void testGetAuthorizationListVersionRequest() {
        fixture.given(CHARGING_STATION)
                .when(new RequestAuthorizationListVersionCommand(CHARGING_STATION_ID))
                .expectEvents(new AuthorizationListVersionRequestedEvent(CHARGING_STATION_ID, PROTOCOL));
    }

    @Test
    public void testGetAuthorizationListVersionReceived() {
        int version = 1;
        fixture.given(CHARGING_STATION)
                .when(new AuthorizationListVersionReceivedCommand(CHARGING_STATION_ID, version))
                .expectEvents(new AuthorizationListVersionReceivedEvent(CHARGING_STATION_ID, version));
    }

    @Test
    public void testRequestReserveNowReservableChargingStation() {
        Date expiryDate = new Date();
        fixture.given(RESERVABLE_CHARGING_STATION)
                .when(new RequestReserveNowCommand(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN))
                .expectEvents(new ReserveNowRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN));
    }

    @Test
    public void testRequestReserveNowNotReservableChargingStation() {
        Date expiryDate = new Date();
        fixture.given(CHARGING_STATION)
                .when(new RequestReserveNowCommand(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN))
                .expectEvents(new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN));
    }

    @Test
    public void testRequestCancelReservation() {
        fixture.given(CHARGING_STATION)
                .when(new RequestCancelReservationCommand(CHARGING_STATION_ID, RESERVATION_ID))
                .expectEvents(new CancelReservationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, RESERVATION_ID));
    }

    @Test
    public void testInformRequestResult() {
        String statusMessage = "Test message";
        fixture.given(CHARGING_STATION)
                .when(new InformRequestResultCommand(CHARGING_STATION_ID, RequestResult.SUCCESS, statusMessage))
                .expectEvents(new RequestResultEvent(CHARGING_STATION_ID, RequestResult.SUCCESS, statusMessage));
    }

    @Test
    public void testIncomingDataTransfer() {
        String messageId = "MessageId";
        String dataToTransfer = "Data to transfer";
        fixture.given(CHARGING_STATION)
                .when(new IncomingDataTransferCommand(CHARGING_STATION_ID, CHARGING_STATION_VENDOR, messageId, dataToTransfer))
                .expectEvents(new IncomingDataTransferReceivedEvent(CHARGING_STATION_ID, CHARGING_STATION_VENDOR, messageId, dataToTransfer));
    }

    @Test
    public void testChargingStationStatusNotification() {
        Date timeStamp = new Date();

        fixture.given(CHARGING_STATION)
                .when(new ChargingStationStatusNotificationCommand(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()))
                .expectEvents(new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()));
    }

    @Test
    public void testComponentStatusNotification() {
        Date timeStamp = new Date();

        fixture.given(CHARGING_STATION)
                .when(new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()))
                .expectEvents(new ComponentStatusNotificationReceivedEvent(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, timeStamp, Collections.<String, String>emptyMap()));
    }

    @Test
    public void testChangeConfiguration() {
        String configKey = "heartbeatInterval";
        String configValue = "800";
        fixture.given(CHARGING_STATION)
                .when(new ChangeConfigurationCommand(CHARGING_STATION_ID, configKey, configValue, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeConfigurationEvent(CHARGING_STATION_ID, PROTOCOL, configKey, configValue, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testStopTransaction() {
        Date now = new Date();
        fixture.given(CHARGING_STATION)
                .when(new StopTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, now, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, now, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testAuthorize() {
        fixture.given(CHARGING_STATION)
                .when(new AuthorizeCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new AuthorizationRequestedEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testGrantAuthorization() {
        fixture.given(CHARGING_STATION)
                .when(new GrantAuthorizationCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN))
                .expectEvents(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.ACCEPTED));
    }

    @Test
    public void testDenyAuthorization() {
        fixture.given(CHARGING_STATION)
                .when(new DenyAuthorizationCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN))
                .expectEvents(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.INVALID));
    }

    @Test
    public void testMakeChargingStationReservable() {
        fixture.given(CHARGING_STATION)
                .when(new MakeChargingStationReservableCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationMadeReservableEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testMakeChargingStationNotReservable() {
        fixture.given(CHARGING_STATION)
                .when(new MakeChargingStationNotReservableCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationMadeNotReservableEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testPlaceChargingStationCommand() {
        fixture.given(CHARGING_STATION)
                .when(new PlaceChargingStationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY))
                .expectEvents(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY));
    }

    @Test
    public void testImproveChargingStationLocationCommand() {
        fixture.given(CHARGING_STATION)
                .when(new ImproveChargingStationLocationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY))
                .expectEvents(new ChargingStationLocationImprovedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY));
    }

    @Test
    public void MoveChargingStationCommand() {
        fixture.given(CHARGING_STATION)
                .when(new MoveChargingStationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY))
                .expectEvents(new ChargingStationMovedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY));
    }
}
