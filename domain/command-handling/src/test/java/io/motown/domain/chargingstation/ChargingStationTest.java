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
    public void testRegisteringUnacceptedChargingStationUserWithoutRights() {
        fixture.given(CREATED_CHARGING_STATION)
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRegisteringAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
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
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testChargePointCreation() {
        fixture.given()
                .when(new CreateChargingStationCommand(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testCreatingAndAcceptingChargingStation() {
        fixture.given()
                .when(new CreateAndAcceptChargingStationCommand(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, ROOT_IDENTITY_CONTEXT),
                        new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testChargeAcceptance() {
        fixture.given(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT))
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationAcceptedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testChargeAcceptanceUnauthorizedIdentityContext() {
        fixture.given(new ChargingStationCreatedEvent(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT))
                .when(new AcceptChargingStationCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfigurationForNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestConfigurationItemsCommand(CHARGING_STATION_ID, CONFIGURATION_KEYS, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfigurationForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestConfigurationItemsCommand(CHARGING_STATION_ID, CONFIGURATION_KEYS, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestConfiguration() {
        fixture.given(CHARGING_STATION)
                .when(new RequestConfigurationItemsCommand(CHARGING_STATION_ID, CONFIGURATION_KEYS, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ConfigurationItemsRequestedEvent(CHARGING_STATION_ID, CONFIGURATION_KEYS, PROTOCOL, ROOT_IDENTITY_CONTEXT));
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
        fixture.given(CHARGING_STATION)
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, START_TRANSACTION_INFO, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, START_TRANSACTION_INFO, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testStartTransactionFilledAttributes() {
        fixture.given(CHARGING_STATION)
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, START_TRANSACTION_INFO, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, START_TRANSACTION_INFO, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testConfigureChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new ConfigureChargingStationCommand(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationConfiguredEvent(CHARGING_STATION_ID, EVSES, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testReceiveConfigurationItems() {
        fixture.given(CHARGING_STATION)
                .when(new ReceiveConfigurationItemsCommand(CHARGING_STATION_ID, CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ConfigurationItemsReceivedEvent(CHARGING_STATION_ID, CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestingToUnlockEvseForUnregisteredChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
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
                .when(new StartTransactionCommand(CHARGING_STATION_ID, TRANSACTION_ID, new StartTransactionInfo(UNKNOWN_EVSE_ID, 0, new Date(), IDENTIFYING_TOKEN, Collections.<String, String>emptyMap()), NULL_USER_IDENTITY_CONTEXT))
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
    public void testRequestSoftResetUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestSoftResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestSoftResetNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestSoftResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestHardResetChargingStation() {
        fixture.given(CHARGING_STATION)
                .when(new RequestHardResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new HardResetChargingStationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestHardResetUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestHardResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestHardResetNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestHardResetChargingStationCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToInoperative() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToInoperativeCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeChargingStationAvailabilityToInoperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestChangeNotAcceptedChargingStationAvailabilityToInoperative() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToInoperativeCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeUnconfiguredChargingStationAvailabilityToInoperative() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToInoperativeCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeChargingStationAvailabilityToOperative() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToOperativeCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeChargingStationAvailabilityToOperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestChangeNotAcceptedChargingStationAvailabilityToOperative() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToOperativeCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeUnconfiguredChargingStationAvailabilityToOperative() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestChangeChargingStationAvailabilityToOperativeCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeComponentAvailabilityToInoperative() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeComponentAvailabilityToInoperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeComponentAvailabilityToInoperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestChangeNotAcceptedComponentAvailabilityToInoperative() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestChangeComponentAvailabilityToInoperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeUnconfiguredComponentAvailabilityToInoperative() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestChangeComponentAvailabilityToInoperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeComponentAvailabilityToOperative() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeComponentAvailabilityToOperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeComponentAvailabilityToOperativeRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestChangeNotAcceptedComponentAvailabilityToOperative() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestChangeComponentAvailabilityToOperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestChangeUnconfiguredComponentAvailabilityToOperative() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestChangeComponentAvailabilityToOperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testChangingChargingStationAvailabilityToInoperative() {
        fixture.given(CHARGING_STATION)
                .when(new ChangeChargingStationAvailabilityToInoperativeCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationAvailabilityChangedToInoperativeEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
    }

    @Test
    public void testChangingChargingStationAvailabilityToOperative() {
        fixture.given(CHARGING_STATION)
                .when(new ChangeChargingStationAvailabilityToOperativeCommand(CHARGING_STATION_ID, IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationAvailabilityChangedToOperativeEvent(CHARGING_STATION_ID, IDENTITY_CONTEXT));
    }

    @Test
    public void testChangingComponentAvailabilityToInoperative() {
        fixture.given(CHARGING_STATION)
                .when(new ChangeComponentAvailabilityToInoperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ComponentAvailabilityChangedToInoperativeEvent(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testChangingComponentAvailabilityToOperative() {
        fixture.given(CHARGING_STATION)
                .when(new ChangeComponentAvailabilityToOperativeCommand(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ComponentAvailabilityChangedToOperativeEvent(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testDataTransfer() {
        fixture.given(CHARGING_STATION)
                .when(new RequestDataTransferCommand(CHARGING_STATION_ID, DATA_TRANSFER_MESSAGE, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new DataTransferRequestedEvent(CHARGING_STATION_ID, PROTOCOL, DATA_TRANSFER_MESSAGE, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testDataTransferResponse() {
        String data = "{'test' : 'response data'}";
        fixture.given(CHARGING_STATION)
                .when(new DataTransferResponseCommand(CHARGING_STATION_ID, data, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new DataTransferResponseReceivedEvent(CHARGING_STATION_ID, data, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetDiagnostics() {
        String targetLocation = "ftp://abc.com/";
        fixture.given(CHARGING_STATION)
                .when(new RequestDiagnosticsCommand(CHARGING_STATION_ID, DIAGNOSTICS_UPLOAD_SETTINGS, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new DiagnosticsRequestedEvent(CHARGING_STATION_ID, PROTOCOL, DIAGNOSTICS_UPLOAD_SETTINGS, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetDiagnosticsForNotAcceptedChargingStation() {
        String targetLocation = "ftp://abc.com/";
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestDiagnosticsCommand(CHARGING_STATION_ID, DIAGNOSTICS_UPLOAD_SETTINGS, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testGetDiagnosticsForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestDiagnosticsCommand(CHARGING_STATION_ID, DIAGNOSTICS_UPLOAD_SETTINGS, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
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
                .expectEvents(new DiagnosticsUploadStatusUpdatedEvent(CHARGING_STATION_ID, true, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testFirmwareStatusUpdate() {
        fixture.given(CHARGING_STATION)
                .when(new UpdateFirmwareStatusCommand(CHARGING_STATION_ID, FirmwareStatus.DOWNLOAD_FAILED, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new FirmwareStatusUpdatedEvent(CHARGING_STATION_ID, FirmwareStatus.DOWNLOAD_FAILED, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestClearCache() {
        fixture.given(CHARGING_STATION)
                .when(new RequestClearCacheCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ClearCacheRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestClearCacheForNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestClearCacheCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestClearCacheForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestClearCacheCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testClearCache() {
        fixture.given(CHARGING_STATION)
                .when(new ClearCacheCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new CacheClearedEvent(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestFirmwareUpdate() {
        String updateLocation = "https://somewhere.nl";
        Date retrieveDate = new Date();
        Map<String, String> attributes = new HashMap<>();

        fixture.given(CHARGING_STATION)
                .when(new RequestFirmwareUpdateCommand(CHARGING_STATION_ID, updateLocation, retrieveDate, attributes, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new FirmwareUpdateRequestedEvent(CHARGING_STATION_ID, PROTOCOL, updateLocation, retrieveDate, attributes, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestFirmwareUpdateForNotAcceptedChargingStation() {
        String updateLocation = "https://somewhere.nl";
        Date retrieveDate = new Date();
        Map<String, String> attributes = new HashMap<>();

        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestFirmwareUpdateCommand(CHARGING_STATION_ID, updateLocation, retrieveDate, attributes, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestFirmwareUpdateForUnconfiguredChargingStation() {
        String updateLocation = "https://somewhere.nl";
        Date retrieveDate = new Date();
        Map<String, String> attributes = new HashMap<>();

        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestFirmwareUpdateCommand(CHARGING_STATION_ID, updateLocation, retrieveDate, attributes, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testSendAuthorizationList() {
        final String hash = "4894007260";

        fixture.given(CHARGING_STATION)
                .when(new RequestSendAuthorizationListCommand(CHARGING_STATION_ID, IDENTIFYING_TOKENS, LIST_VERSION, hash, AuthorizationListUpdateType.FULL, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new SendAuthorizationListRequestedEvent(CHARGING_STATION_ID, PROTOCOL, IDENTIFYING_TOKENS, LIST_VERSION, hash, AuthorizationListUpdateType.FULL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testSendAuthorizationListToNotAcceptedChargingStation() {
        final String hash = "4894007260";

        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestSendAuthorizationListCommand(CHARGING_STATION_ID, IDENTIFYING_TOKENS, LIST_VERSION, hash, AuthorizationListUpdateType.FULL, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testSendAuthorizationListToUnconfiguredChargingStation() {
        final String hash = "4894007260";

        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestSendAuthorizationListCommand(CHARGING_STATION_ID, IDENTIFYING_TOKENS, LIST_VERSION, hash, AuthorizationListUpdateType.FULL, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testChangeAuthorizationList() {
        fixture.given(CHARGING_STATION)
                .when(new ChangeAuthorizationListCommand(CHARGING_STATION_ID, LIST_VERSION, AuthorizationListUpdateType.FULL, IDENTIFYING_TOKENS, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new AuthorizationListChangedEvent(CHARGING_STATION_ID, LIST_VERSION, AuthorizationListUpdateType.FULL, IDENTIFYING_TOKENS, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetAuthorizationListVersionRequest() {
        fixture.given(CHARGING_STATION)
                .when(new RequestAuthorizationListVersionCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new AuthorizationListVersionRequestedEvent(CHARGING_STATION_ID, PROTOCOL, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testGetAuthorizationListVersionRequestFromNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestAuthorizationListVersionCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testGetAuthorizationListVersionRequestFromUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestAuthorizationListVersionCommand(CHARGING_STATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testGetAuthorizationListVersionReceived() {
        fixture.given(CHARGING_STATION)
                .when(new AuthorizationListVersionReceivedCommand(CHARGING_STATION_ID, LIST_VERSION, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new AuthorizationListVersionReceivedEvent(CHARGING_STATION_ID, LIST_VERSION, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestReserveNowReservableChargingStation() {
        Date expiryDate = new Date();
        fixture.given(RESERVABLE_CHARGING_STATION)
                .when(new RequestReserveNowCommand(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ReserveNowRequestedEvent(CHARGING_STATION_ID, PROTOCOL, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestReserveNowNotReservableChargingStation() {
        Date expiryDate = new Date();
        fixture.given(CHARGING_STATION)
                .when(new RequestReserveNowCommand(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ReserveNowRequestedForUnreservableChargingStationEvent(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestReserveNowNotAcceptedChargingStation() {
        Date expiryDate = new Date();
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestReserveNowCommand(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestReserveNowUnconfiguredChargingStation() {
        Date expiryDate = new Date();
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestReserveNowCommand(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, expiryDate, PARENT_IDENTIFYING_TOKEN, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestCancelReservation() {
        fixture.given(CHARGING_STATION)
                .when(new RequestCancelReservationCommand(CHARGING_STATION_ID, RESERVATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new CancelReservationRequestedEvent(CHARGING_STATION_ID, PROTOCOL, RESERVATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testRequestCancelReservationForNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestCancelReservationCommand(CHARGING_STATION_ID, RESERVATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testRequestCancelReservationForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestCancelReservationCommand(CHARGING_STATION_ID, RESERVATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testCancelReservation() {
        fixture.given(CHARGING_STATION)
                .when(new CancelReservationCommand(CHARGING_STATION_ID, RESERVATION_ID, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ReservationCancelledEvent(CHARGING_STATION_ID, RESERVATION_ID, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testIncomingDataTransfer() {
        fixture.given(CHARGING_STATION)
                .when(new IncomingDataTransferCommand(CHARGING_STATION_ID, DATA_TRANSFER_MESSAGE, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new IncomingDataTransferReceivedEvent(CHARGING_STATION_ID, DATA_TRANSFER_MESSAGE, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testIncomingDataTransferResult() {
        String dataToTransfer = "Data to return in response";
        IncomingDataTransferResultStatus status = IncomingDataTransferResultStatus.ACCEPTED;
        fixture.given(CHARGING_STATION)
                .when(new IncomingDataTransferResponseCommand(CHARGING_STATION_ID, dataToTransfer, status, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new IncomingDataTransferResultEvent(CHARGING_STATION_ID, dataToTransfer, status, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testChargingStationStatusNotification() {
        StatusNotification statusNotification = new StatusNotification(ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap());

        fixture.given(CHARGING_STATION)
                .when(new ChargingStationStatusNotificationCommand(CHARGING_STATION_ID, statusNotification, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, statusNotification, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testComponentStatusNotification() {
        StatusNotification statusNotification = new StatusNotification(ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap());

        fixture.given(CHARGING_STATION)
                .when(new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, statusNotification, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new ComponentStatusNotificationReceivedEvent(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, statusNotification, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testChangeConfiguration() {
        fixture.given(CHARGING_STATION)
                .when(new RequestChangeConfigurationItemCommand(CHARGING_STATION_ID, CONFIGURATION_ITEM, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChangeConfigurationItemRequestedEvent(CHARGING_STATION_ID, PROTOCOL, CONFIGURATION_ITEM, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testChangeConfigurationForNotAcceptedChargingStation() {
        fixture.given(UNCONFIGURED_CHARGING_STATION)
                .when(new RequestChangeConfigurationItemCommand(CHARGING_STATION_ID, CONFIGURATION_ITEM, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testChangeConfigurationForUnconfiguredChargingStation() {
        fixture.given(UNCONFIGURED_ACCEPTED_CHARGING_STATION)
                .when(new RequestChangeConfigurationItemCommand(CHARGING_STATION_ID, CONFIGURATION_ITEM, ROOT_IDENTITY_CONTEXT))
                .expectException(IllegalStateException.class);
    }

    @Test
    public void testChangeConfigurationItem() {
        fixture.given(CHARGING_STATION)
                .when(new ChangeConfigurationItemCommand(CHARGING_STATION_ID, CONFIGURATION_ITEM, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ConfigurationItemChangedEvent(CHARGING_STATION_ID, CONFIGURATION_ITEM, ROOT_IDENTITY_CONTEXT));
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
                .when(new GrantAuthorizationCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.ACCEPTED, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testDenyAuthorization() {
        fixture.given(CHARGING_STATION)
                .when(new DenyAuthorizationCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN, NULL_USER_IDENTITY_CONTEXT))
                .expectEvents(new AuthorizationResultEvent(CHARGING_STATION_ID, IDENTIFYING_TOKEN, AuthorizationResultStatus.INVALID, NULL_USER_IDENTITY_CONTEXT));
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
                .when(new PlaceChargingStationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationPlacedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testImproveChargingStationLocationCommand() {
        fixture.given(CHARGING_STATION)
                .when(new ImproveChargingStationLocationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationLocationImprovedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void MoveChargingStationCommand() {
        fixture.given(CHARGING_STATION)
                .when(new MoveChargingStationCommand(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationMovedEvent(CHARGING_STATION_ID, COORDINATES, ADDRESS, ACCESSIBILITY, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testSettingChargingStationOpeningTimes() {
        fixture.given(CHARGING_STATION)
                .when(new SetChargingStationOpeningTimesCommand(CHARGING_STATION_ID, OPENING_TIMES, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationOpeningTimesSetEvent(CHARGING_STATION_ID, OPENING_TIMES, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testAddingChargingStationOpeningTimes() {
        fixture.given(CHARGING_STATION)
                .when(new AddChargingStationOpeningTimesCommand(CHARGING_STATION_ID, OPENING_TIMES, ROOT_IDENTITY_CONTEXT))
                .expectEvents(new ChargingStationOpeningTimesAddedEvent(CHARGING_STATION_ID, OPENING_TIMES, ROOT_IDENTITY_CONTEXT));
    }

    @Test
    public void testReserveNowResult() {
        Date reservationExpiryDate = new Date();
        fixture.given(CHARGING_STATION)
                .when(new ReserveNowCommand(CHARGING_STATION_ID, RESERVATION_ID, EVSE_ID, reservationExpiryDate, IDENTITY_CONTEXT))
                .expectEvents(new ReservedNowEvent(CHARGING_STATION_ID, RESERVATION_ID, EVSE_ID, reservationExpiryDate, IDENTITY_CONTEXT));
    }

    @Test
    public void testUnlockEvseResult() {
        fixture.given(CHARGING_STATION)
                .when(new UnlockEvseCommand(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT))
                .expectEvents(new EvseUnlockedEvent(CHARGING_STATION_ID, EVSE_ID, IDENTITY_CONTEXT));
    }
}
