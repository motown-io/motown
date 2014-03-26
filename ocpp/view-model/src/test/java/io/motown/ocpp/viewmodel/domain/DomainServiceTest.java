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
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.entities.ReservationIdentifier;
import io.motown.ocpp.viewmodel.persistence.entities.Transaction;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.ReservationIdentifierRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.TransactionRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.gateway.EventWaitingGateway;
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

    @Autowired
    @Qualifier("entityManagerOcppViewModel")
    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityManager.clear();
        deleteFromDatabase(entityManager, ChargingStation.class);
        deleteFromDatabase(entityManager, Transaction.class);
        deleteFromDatabase(entityManager, ReservationIdentifier.class);

        domainService = new DomainService();
        domainService.setChargingStationRepository(chargingStationRepository);
        domainService.setTransactionRepository(transactionRepository);
        domainService.setReservationIdentifierRepository(reservationIdentifierRepository);
        domainService.setEntityManagerFactory(entityManagerFactory);

        gateway = mock(DomainCommandGateway.class);
        domainService.setCommandGateway(gateway);

        eventWaitingGateway = mock(EventWaitingGateway.class);
        domainService.setEventWaitingGateway(eventWaitingGateway);
    }

    @Test
    public void testBootUnknownChargingStation() {
        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(CHARGING_STATION_ID, CHARGING_STATION_ADDRESS, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL,
                CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        assertFalse(bootChargingStationResult.isAccepted());

        verify(gateway).send(eq(new CreateChargingStationCommand(CHARGING_STATION_ID)), any(CommandCallback.class));
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
    public void testBootKnownChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId(), CHARGING_STATION_ADDRESS);
        cs.setRegistered(true);
        cs.setNumberOfEvses(2);
        cs.setConfigured(true);
        chargingStationRepository.insert(cs);

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
        domainService.heartbeat(CHARGING_STATION_ID);

        verify(gateway).send(new HeartbeatCommand(CHARGING_STATION_ID));
    }

    @Test
    public void testDataTransfer() {
        domainService.dataTransfer(CHARGING_STATION_ID, DATA_TRANSFER_DATA, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID);

        verify(gateway).send(new IncomingDataTransferCommand(CHARGING_STATION_ID, DATA_TRANSFER_VENDOR, DATA_TRANSFER_MESSAGE_ID, DATA_TRANSFER_DATA));
    }

    @Test
    public void testMeterValues() {
        domainService.meterValues(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES);

        verify(gateway).send(new ProcessMeterValueCommand(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES));
    }

    @Test
    public void testDiagnosticsFileNameReceived() {
        CorrelationToken correlationToken = new CorrelationToken();
        domainService.diagnosticsFileNameReceived(CHARGING_STATION_ID, getDiagnosticsFileName(), correlationToken);

        verify(gateway).send(new DiagnosticsFileNameReceivedCommand(CHARGING_STATION_ID, getDiagnosticsFileName()), correlationToken);
    }

    @Test
    public void testAuthorizationListVersionReceived() {
        CorrelationToken correlationToken = new CorrelationToken();
        domainService.authorizationListVersionReceived(CHARGING_STATION_ID, getAuthorizationListVersion(), correlationToken);

        verify(gateway).send(new AuthorizationListVersionReceivedCommand(CHARGING_STATION_ID, getAuthorizationListVersion()), correlationToken);
    }

    @Test
    public void testAuthorize() {
        FutureEventCallback futureEventCallback = getFutureEventCallback();
        domainService.authorize(CHARGING_STATION_ID, IDENTIFYING_TOKEN.getToken(), futureEventCallback);
        verify(eventWaitingGateway).sendAndWaitForEvent(new AuthorizeCommand(CHARGING_STATION_ID, IDENTIFYING_TOKEN), futureEventCallback);
    }

    @Test
    public void testDiagnosticsUploadStatusUpdate() {
        domainService.diagnosticsUploadStatusUpdate(CHARGING_STATION_ID, true);
        verify(gateway).send(new UpdateDiagnosticsUploadStatusCommand(CHARGING_STATION_ID, true));

        domainService.diagnosticsUploadStatusUpdate(CHARGING_STATION_ID, false);
        verify(gateway).send(new UpdateDiagnosticsUploadStatusCommand(CHARGING_STATION_ID, false));
    }

    @Test
    public void testFirmwareStatusUpdate() {
        domainService.firmwareStatusUpdate(CHARGING_STATION_ID, FirmwareStatus.DOWNLOADED);
        verify(gateway).send(new UpdateFirmwareStatusCommand(CHARGING_STATION_ID, FirmwareStatus.DOWNLOADED));

        domainService.firmwareStatusUpdate(CHARGING_STATION_ID, FirmwareStatus.INSTALLATION_FAILED);
        verify(gateway).send(new UpdateFirmwareStatusCommand(CHARGING_STATION_ID, FirmwareStatus.INSTALLATION_FAILED));
    }

    @Test
    public void testStatusNotification() {
        Date now = new Date();
        domainService.statusNotification(CHARGING_STATION_ID, EVSE_ID, getStatusNotifactionErrorCode(), ComponentStatus.AVAILABLE, getStatusNotificationInfo(), now, getVendor(), getVendorErrorCode());
        Map<String, String> attributes = new HashMap<>();
        attributes.put(DomainService.ERROR_CODE_KEY, getStatusNotifactionErrorCode());
        attributes.put(DomainService.INFO_KEY, getStatusNotificationInfo());
        attributes.put(DomainService.VENDOR_ID_KEY, getVendor());
        attributes.put(DomainService.VENDOR_ERROR_CODE_KEY, getVendorErrorCode());

        verify(gateway).send(new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, now, attributes));

        domainService.statusNotification(CHARGING_STATION_ID, getChargingStationComponentId(), getStatusNotifactionErrorCode(), ComponentStatus.AVAILABLE, getStatusNotificationInfo(), now, getVendor(), getVendorErrorCode());
        verify(gateway).send(new ChargingStationStatusNotificationCommand(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, now, attributes));
    }

    @Test
    public void testStatusNotificationEmptyArgs() {
        Date now = new Date();
        domainService.statusNotification(CHARGING_STATION_ID, EVSE_ID, null, ComponentStatus.AVAILABLE, null, now, null, null);
        Map<String, String> attributes = new HashMap<>();

        verify(gateway).send(new ComponentStatusNotificationCommand(CHARGING_STATION_ID, ChargingStationComponent.CONNECTOR, EVSE_ID, ComponentStatus.AVAILABLE, now, attributes));

        domainService.statusNotification(CHARGING_STATION_ID, getChargingStationComponentId(), null, ComponentStatus.AVAILABLE, null, now, null, null);
        verify(gateway).send(new ChargingStationStatusNotificationCommand(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, now, attributes));
    }

    @Test
    public void testStatusChanged() {
        String statusMessage = "Test message";

        domainService.statusChanged(CHARGING_STATION_ID, RequestStatus.SUCCESS, CORRELATION_TOKEN, statusMessage);
        verify(gateway).send(new StatusChangedCommand(CHARGING_STATION_ID, RequestStatus.SUCCESS, statusMessage), CORRELATION_TOKEN);

        domainService.statusChanged(CHARGING_STATION_ID, RequestStatus.FAILURE, CORRELATION_TOKEN, statusMessage);
        verify(gateway).send(new StatusChangedCommand(CHARGING_STATION_ID, RequestStatus.FAILURE, statusMessage), CORRELATION_TOKEN);
    }

    @Test
    public void testConfigureChargingStation() {
        domainService.configureChargingStation(CHARGING_STATION_ID, CONFIGURATION_ITEMS);
        verify(gateway).send(new ConfigureChargingStationCommand(CHARGING_STATION_ID, CONFIGURATION_ITEMS));
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownChargingStation() {
        domainService.startTransaction(UNKNOWN_CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnregisteredChargingStation() {
        chargingStationRepository.insert(new ChargingStation(CHARGING_STATION_ID.getId()));

        domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnconfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setRegistered(true);
        chargingStationRepository.insert(cs);

        domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionInvalidEvse() {
        chargingStationRepository.insert(getRegisteredAndConfiguredChargingStation());

        domainService.startTransaction(CHARGING_STATION_ID, UNKNOWN_EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownEvse() {
        chargingStationRepository.insert(getRegisteredAndConfiguredChargingStation());

        domainService.startTransaction(CHARGING_STATION_ID, UNKNOWN_EVSE_ID, IDENTIFYING_TOKEN, 0, new Date(), RESERVATION_ID, PROTOCOL);
    }

    @Test
    public void testStartTransactionEmptyAttributesChargingStation() {
        chargingStationRepository.insert(getRegisteredAndConfiguredChargingStation());

        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, now, null, PROTOCOL);
        assertTrue(ocppTransactionId > 0);

        TransactionId transactionId = new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);

        verify(gateway).send(new StartTransactionCommand(CHARGING_STATION_ID, transactionId, EVSE_ID, IDENTIFYING_TOKEN, 0, now, getEmptyAttributesMap()));
    }

    @Test
    public void testStartTransactionChargingStation() {
        System.err.println("testStartTransactionChargingStation");
        chargingStationRepository.insert(getRegisteredAndConfiguredChargingStation());

        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, now, RESERVATION_ID, PROTOCOL);
        assertTrue(ocppTransactionId > 0);

        TransactionId transactionId = new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);

        verify(gateway).send(new StartTransactionCommand(CHARGING_STATION_ID, transactionId, EVSE_ID, IDENTIFYING_TOKEN, 0, now, getStartTransactionAttributesMap(RESERVATION_ID.getNumber())));
    }

    @Test
    public void testStopTransaction() {
        int ocppTransactionId = 0;
        NumberedTransactionId transactionId = new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);
        int meterStopValue = 1;
        Date now = new Date();

        domainService.stopTransaction(CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, now, getEmptyMeterValuesList());

        verify(gateway).send(new StopTransactionCommand(CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, now));
    }

    /**
     * Stopping a transaction with meter values should trigger a 'ProcessMeterValueCommand' besides the 'StopTransactionCommand'.
     */
    @Test
    public void testStopTransactionWithMeterValues() {
        chargingStationRepository.insert(getRegisteredAndConfiguredChargingStation());

        // registers a transaction in the transactionRepository
        Date startTransactionDate = new Date();
        int ocppTransactionId = domainService.startTransaction(CHARGING_STATION_ID, EVSE_ID, IDENTIFYING_TOKEN, 0, startTransactionDate, RESERVATION_ID, PROTOCOL);

        NumberedTransactionId transactionId = new NumberedTransactionId(CHARGING_STATION_ID, PROTOCOL, ocppTransactionId);
        int meterStopValue = 1;
        Date stopTransactionDate = new Date();

        domainService.stopTransaction(CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, stopTransactionDate, METER_VALUES);

        verify(gateway).send(new StopTransactionCommand(CHARGING_STATION_ID, transactionId, IDENTIFYING_TOKEN, meterStopValue, stopTransactionDate));
        // stored transaction should provide the evse id that's needed to process the meter values
        verify(gateway).send(new ProcessMeterValueCommand(CHARGING_STATION_ID, transactionId, EVSE_ID, METER_VALUES));
    }

    @Test
    public void testRetrieveChargingStationAddress() {
        String address = domainService.retrieveChargingStationAddress(CHARGING_STATION_ID);
        assertEquals(address, "");

        chargingStationRepository.insert(getRegisteredAndConfiguredChargingStation());

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
