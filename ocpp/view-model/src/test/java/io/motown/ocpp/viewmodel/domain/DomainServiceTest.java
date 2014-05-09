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

import com.google.common.collect.Maps;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.utils.axon.EventWaitingGateway;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.entities.ReservationIdentifier;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repositories.ReservationIdentifierRepository;
import io.motown.ocpp.viewmodel.persistence.repositories.TransactionRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.viewmodel.domain.OccpViewModelTestUtils.*;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DomainServiceTest {

    private static final int AUTHORIZATION_TIMEOUT_IN_MILLIS = 1000;

    private static final ChargingStationId REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID = new ChargingStationId("CS-999");

    private DomainService domainService;

    private DomainCommandGateway gateway;

    private EventWaitingGateway eventWaitingGateway;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationIdentifierRepository reservationIdentifierRepository;

    @Autowired
    @Qualifier("entityManagerFactoryOcppViewModel")
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.clear();
        deleteFromDatabase(entityManager, ChargingStation.class);
        deleteFromDatabase(entityManager, Transaction.class);
        deleteFromDatabase(entityManager, ReservationIdentifier.class);

        domainService = new DomainService();
        domainService.setChargingStationRepository(chargingStationRepository);
        domainService.setTransactionRepository(transactionRepository);
        domainService.setReservationIdentifierRepository(reservationIdentifierRepository);
        domainService.setEntityManagerFactory(entityManagerFactory);
        domainService.setAuthorizationTimeoutInMillis(AUTHORIZATION_TIMEOUT_IN_MILLIS);
        domainService.setUserIdentitiesWithAllPermissions(USER_IDENTITIES_WITH_ALL_PERMISSIONS);

        gateway = mock(DomainCommandGateway.class);
        domainService.setCommandGateway(gateway);

        eventWaitingGateway = mock(EventWaitingGateway.class);
        domainService.setEventWaitingGateway(eventWaitingGateway);

        ChargingStation chargingStation = new ChargingStation(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID.getId());
        chargingStation.setConfigured(true);
        chargingStation.setRegistered(true);
        chargingStation.setNumberOfEvses(EVSES.size());
        chargingStationRepository.createOrUpdate(chargingStation);
    }

    @Test
    public void testBootUnknownChargingStation() {
        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(CHARGING_STATION_ID, CHARGING_STATION_ADDRESS, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL,
                CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        assertFalse(bootChargingStationResult.isAccepted());

        verify(gateway).send(eq(new CreateChargingStationCommand(CHARGING_STATION_ID, USER_IDENTITIES_WITH_ALL_PERMISSIONS, NULL_USER_IDENTITY_CONTEXT)), any(CommandCallback.class));
    }

    @Test
    public void testBootNullAndEmptyChargingStationAddress() {
        BootChargingStationResult result = domainService.bootChargingStation(CHARGING_STATION_ID, null, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL, CHARGING_STATION_SERIAL_NUMBER,
                CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        assertFalse(result.isAccepted());

        result = domainService.bootChargingStation(CHARGING_STATION_ID, "", CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(),
                getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        assertFalse(result.isAccepted());
    }

    @Test
    public void testBootUnconfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId(), CHARGING_STATION_ADDRESS);
        cs.setRegistered(true);
        cs.setNumberOfEvses(2);
        cs.setConfigured(false);
        chargingStationRepository.createOrUpdate(cs);

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(CHARGING_STATION_ID, CHARGING_STATION_ADDRESS, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL,
                CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        assertFalse(bootChargingStationResult.isAccepted());

        Map<String, String> attributes = new HashMap<>();
        DomainService.addAttributeIfNotNull(attributes, DomainService.ADDRESS_KEY, CHARGING_STATION_ADDRESS);
        DomainService.addAttributeIfNotNull(attributes, DomainService.VENDOR_KEY, CHARGING_STATION_VENDOR);
        DomainService.addAttributeIfNotNull(attributes, DomainService.MODEL_KEY, CHARGING_STATION_MODEL);
        DomainService.addAttributeIfNotNull(attributes, DomainService.CHARGING_STATION_SERIALNUMBER_KEY, CHARGING_STATION_SERIAL_NUMBER);
        DomainService.addAttributeIfNotNull(attributes, DomainService.CHARGE_BOX_SERIALNUMBER_KEY, CHARGE_BOX_SERIAL_NUMBER);
        DomainService.addAttributeIfNotNull(attributes, DomainService.FIRMWARE_VERSION_KEY, getFirmwareVersion());
        DomainService.addAttributeIfNotNull(attributes, DomainService.ICCID_KEY, getIccid());
        DomainService.addAttributeIfNotNull(attributes, DomainService.IMSI_KEY, getImsi());
        DomainService.addAttributeIfNotNull(attributes, DomainService.METER_TYPE_KEY, getMeterType());
        DomainService.addAttributeIfNotNull(attributes, DomainService.METER_SERIALNUMBER_KEY, getMeterSerialNumber());


        verify(gateway).send(eq(new BootChargingStationCommand(CHARGING_STATION_ID, PROTOCOL, attributes, NULL_USER_IDENTITY_CONTEXT)));
    }

    @Test
    public void testBootKnownChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId(), CHARGING_STATION_ADDRESS);
        cs.setRegistered(true);
        cs.setNumberOfEvses(2);
        cs.setConfigured(true);
        chargingStationRepository.createOrUpdate(cs);

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(CHARGING_STATION_ID, CHARGING_STATION_ADDRESS, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL,
                CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        assertTrue(bootChargingStationResult.isAccepted());

        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("vendor", CHARGING_STATION_VENDOR);
        attributes.put("model", CHARGING_STATION_MODEL);
        attributes.put("address", CHARGING_STATION_ADDRESS);
        attributes.put("chargingStationSerialNumber", CHARGING_STATION_SERIAL_NUMBER);
        attributes.put("chargeBoxSerialNumber", CHARGE_BOX_SERIAL_NUMBER);
        attributes.put("firmwareVersion", getFirmwareVersion());
        attributes.put("iccid", getIccid());
        attributes.put("imsi", getImsi());
        attributes.put("meterType", getMeterType());
        attributes.put("meterSerialNumber", getMeterSerialNumber());

        // test if the charging station is stored and in the state we expect it after a boot of an unknown charging station
        cs = chargingStationRepository.findOne(CHARGING_STATION_ID.getId());
        assertEquals(cs.getId(), CHARGING_STATION_ID.getId());
        assertEquals(cs.getIpAddress(), CHARGING_STATION_ADDRESS);
        assertEquals(cs.getNumberOfEvses(), 2);
        assertTrue(cs.isConfigured());
        assertTrue(cs.isRegistered());
        assertTrue(cs.isRegisteredAndConfigured());
        assertEquals(cs.getIpAddress(), CHARGING_STATION_ADDRESS);

        verify(gateway).send(new BootChargingStationCommand(CHARGING_STATION_ID, PROTOCOL, attributes, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testHeartbeat() {
        domainService.heartbeat(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, ADD_ON_IDENTITY);

        verify(gateway).send(new HeartbeatCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testDataTransfer() {
        FutureEventCallback futureEventCallback = getFutureEventCallback();
        domainService.incomingDataTransfer(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, DATA_TRANSFER_DATA, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, futureEventCallback, ADD_ON_IDENTITY);

        verify(eventWaitingGateway).sendAndWaitForEvent(new IncomingDataTransferCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA, NULL_USER_IDENTITY_CONTEXT), futureEventCallback, 10000);
    }

    @Test
    public void testMeterValues() {
        domainService.meterValues(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES, ADD_ON_IDENTITY);

        verify(gateway).send(new ProcessMeterValueCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testDiagnosticsFileNameReceived() {
        CorrelationToken correlationToken = new CorrelationToken();
        domainService.diagnosticsFileNameReceived(CHARGING_STATION_ID, getDiagnosticsFileName(), correlationToken, ADD_ON_IDENTITY);

        verify(gateway).send(new DiagnosticsFileNameReceivedCommand(CHARGING_STATION_ID, getDiagnosticsFileName(), NULL_USER_IDENTITY_CONTEXT), correlationToken);
    }

    @Test
    public void testAuthorizationListVersionReceived() {
        CorrelationToken correlationToken = new CorrelationToken();
        domainService.authorizationListVersionReceived(CHARGING_STATION_ID, getAuthorizationListVersion(), correlationToken, ADD_ON_IDENTITY);

        verify(gateway).send(new AuthorizationListVersionReceivedCommand(CHARGING_STATION_ID, getAuthorizationListVersion(), NULL_USER_IDENTITY_CONTEXT), correlationToken);
    }

    @Test
    public void testAuthorize() {
        FutureEventCallback futureEventCallback = getFutureEventCallback();
        domainService.authorize(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, IDENTIFYING_TOKEN.getToken(), futureEventCallback, ADD_ON_IDENTITY);
        verify(eventWaitingGateway).sendAndWaitForEvent(new AuthorizeCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, IDENTIFYING_TOKEN, NULL_USER_IDENTITY_CONTEXT), futureEventCallback, AUTHORIZATION_TIMEOUT_IN_MILLIS);
    }

    @Test
    public void testChangeConfiguration() {
        CorrelationToken correlationToken = new CorrelationToken();

        domainService.changeConfiguration(CHARGING_STATION_ID, CONFIGURATION_ITEM, correlationToken, ADD_ON_IDENTITY);
        verify(gateway).send(new ChangeConfigurationItemCommand(CHARGING_STATION_ID, CONFIGURATION_ITEM, NULL_USER_IDENTITY_CONTEXT), correlationToken);
    }

    @Test
    public void testDiagnosticsUploadStatusUpdate() {
        domainService.diagnosticsUploadStatusUpdate(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, true, ADD_ON_IDENTITY);
        verify(gateway).send(new UpdateDiagnosticsUploadStatusCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, true, NULL_USER_IDENTITY_CONTEXT));

        domainService.diagnosticsUploadStatusUpdate(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, false, ADD_ON_IDENTITY);
        verify(gateway).send(new UpdateDiagnosticsUploadStatusCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, false, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testFirmwareStatusUpdate() {
        domainService.firmwareStatusUpdate(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, FirmwareStatus.DOWNLOADED, ADD_ON_IDENTITY);
        verify(gateway).send(new UpdateFirmwareStatusCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, FirmwareStatus.DOWNLOADED, NULL_USER_IDENTITY_CONTEXT));

        domainService.firmwareStatusUpdate(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, FirmwareStatus.INSTALLATION_FAILED, ADD_ON_IDENTITY);
        verify(gateway).send(new UpdateFirmwareStatusCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, FirmwareStatus.INSTALLATION_FAILED, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testStatusNotification() {
        Date now = new Date();
        domainService.statusNotification(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, EVSE_ID, getStatusNotifactionErrorCode(), ComponentStatus.AVAILABLE, getStatusNotificationInfo(),
                now, getVendor(), getVendorErrorCode(), ADD_ON_IDENTITY);
        Map<String, String> attributes = new HashMap<>();
        attributes.put(DomainService.ERROR_CODE_KEY, getStatusNotifactionErrorCode());
        attributes.put(DomainService.INFO_KEY, getStatusNotificationInfo());
        attributes.put(DomainService.VENDOR_ID_KEY, getVendor());
        attributes.put(DomainService.VENDOR_ERROR_CODE_KEY, getVendorErrorCode());

        verify(gateway).send(new ComponentStatusNotificationCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, ChargingStationComponent.EVSE, EVSE_ID, ComponentStatus.AVAILABLE, now, attributes, NULL_USER_IDENTITY_CONTEXT));

        domainService.statusNotification(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, getChargingStationComponentId(), getStatusNotifactionErrorCode(),
                ComponentStatus.AVAILABLE, getStatusNotificationInfo(), now, getVendor(), getVendorErrorCode(), ADD_ON_IDENTITY);
        verify(gateway).send(new ChargingStationStatusNotificationCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, ComponentStatus.AVAILABLE, now, attributes, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testStatusNotificationEmptyArgs() {
        Date now = new Date();
        domainService.statusNotification(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, EVSE_ID, null, ComponentStatus.AVAILABLE, null, now, null, null, ADD_ON_IDENTITY);
        Map<String, String> attributes = new HashMap<>();

        verify(gateway).send(new ComponentStatusNotificationCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, ChargingStationComponent.EVSE, EVSE_ID, ComponentStatus.AVAILABLE, now, attributes, NULL_USER_IDENTITY_CONTEXT));

        domainService.statusNotification(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, getChargingStationComponentId(), null, ComponentStatus.AVAILABLE, null, now, null, null, ADD_ON_IDENTITY);
        verify(gateway).send(new ChargingStationStatusNotificationCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, ComponentStatus.AVAILABLE, now, attributes, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testReceiveConfigurationItems() {
        domainService.receiveConfigurationItems(CHARGING_STATION_ID, CONFIGURATION_ITEMS, ADD_ON_IDENTITY);
        verify(gateway).send(new ReceiveConfigurationItemsCommand(CHARGING_STATION_ID, CONFIGURATION_ITEMS, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownChargingStation() {
        domainService.startTransaction(UNKNOWN_CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnregisteredChargingStation() {
        chargingStationRepository.createOrUpdate(new ChargingStation(CHARGING_STATION_ID.getId()));

        domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnconfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setRegistered(true);
        chargingStationRepository.createOrUpdate(cs);

        domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionInvalidEvse() {
        domainService.startTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, UNKNOWN_EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownEvse() {
        domainService.startTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, UNKNOWN_EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);
    }

    @Test
    public void testStartTransactionEmptyAttributesChargingStation() {
        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, now, null, PROTOCOL, ADD_ON_IDENTITY);
        assertTrue(ocppTransactionId > 0);

        TransactionId transactionId = new NumberedTransactionId(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);

        verify(gateway).send(new StartTransactionCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, EVSE_ID, IDENTIFYING_TOKEN, 0, now, getEmptyAttributesMap(), NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testStartTransactionChargingStation() {
        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, now, RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);
        assertTrue(ocppTransactionId > 0);

        TransactionId transactionId = new NumberedTransactionId(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);

        verify(gateway).send(new StartTransactionCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, EVSE_ID, IDENTIFYING_TOKEN, 0, now, getStartTransactionAttributesMap(RESERVATION_ID.getNumber()), NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testStopTransaction() {
        int ocppTransactionId = 0;
        NumberedTransactionId transactionId = new NumberedTransactionId(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);
        int meterStopValue = 1;
        Date now = new Date();

        domainService.stopTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, now, getEmptyMeterValuesList(), ADD_ON_IDENTITY);

        verify(gateway).send(new StopTransactionCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, now, NULL_USER_IDENTITY_CONTEXT));
    }

    /**
     * Stopping a transaction with meter values should trigger a 'ProcessMeterValueCommand' besides the 'StopTransactionCommand'.
     */
    @Test
    public void testStopTransactionWithMeterValues() {
        // registers a transaction in the transactionRepository
        Date startTransactionDate = new Date();
        int ocppTransactionId = domainService.startTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, startTransactionDate, RESERVATION_ID, PROTOCOL, ADD_ON_IDENTITY);

        NumberedTransactionId transactionId = new NumberedTransactionId(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);
        int meterStopValue = 1;
        Date stopTransactionDate = new Date();

        domainService.stopTransaction(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, stopTransactionDate, METER_VALUES, ADD_ON_IDENTITY);

        verify(gateway).send(new StopTransactionCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, stopTransactionDate, NULL_USER_IDENTITY_CONTEXT));
        // stored transaction should provide the evse id that's needed to process the meter values
        verify(gateway).send(new ProcessMeterValueCommand(REGISTERED_AND_CONFIGURED_CHARGING_STATION_ID, transactionId, EVSE_ID, METER_VALUES, NULL_USER_IDENTITY_CONTEXT));
    }

    @Test
    public void testRetrieveChargingStationAddress() {
        String address = domainService.retrieveChargingStationAddress(CHARGING_STATION_ID);
        assertEquals(address, "");

        chargingStationRepository.createOrUpdate(getRegisteredAndConfiguredChargingStation());

        address = domainService.retrieveChargingStationAddress(CHARGING_STATION_ID);
        assertEquals(address, CHARGING_STATION_ADDRESS);
    }

    @Test
    public void testGenerateReservationIdentifier() {
        NumberedReservationId numberedReservationId = domainService.generateReservationIdentifier(CHARGING_STATION_ID, PROTOCOL);

        assertNotNull(numberedReservationId.getId());
        assertNotNull(numberedReservationId.getNumber());
    }

}
