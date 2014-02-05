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
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.ReservationIdentifierRepository;
import io.motown.ocpp.viewmodel.persistence.repostories.TransactionRepository;
import org.axonframework.commandhandling.CommandCallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.Map;

import static io.motown.ocpp.viewmodel.domain.TestUtils.*;
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

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ReservationIdentifierRepository reservationIdentifierRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {

        chargingStationRepository.deleteAll();
        transactionRepository.deleteAll();
        reservationIdentifierRepository.deleteAll();

        domainService = new DomainService();
        domainService.setChargingStationRepository(chargingStationRepository);
        domainService.setTransactionRepository(transactionRepository);
        domainService.setReservationIdentifierRepository(reservationIdentifierRepository);
        domainService.setEntityManagerFactory(entityManagerFactory);

        gateway = mock(DomainCommandGateway.class);
        domainService.setCommandGateway(gateway);
    }

    @Test
    public void testBootUnknownChargingStation() {
        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(getChargingStationId(), getChargingStationAddress(), getVendor(), getModel(), getProtocol(), getChargingStationSerialNumber(), getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber());
        assertFalse(bootChargingStationResult.isAccepted());

        verify(gateway).send(eq(new CreateChargingStationCommand(getChargingStationId())), any(CommandCallback.class));
    }

    @Test
    public void testBootNullAndEmptyChargingStationAddress() {
        BootChargingStationResult result = domainService.bootChargingStation(getChargingStationId(), null, getVendor(), getModel(), getProtocol(), getChargingStationSerialNumber(), getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber());
        assertFalse(result.isAccepted());

        result = domainService.bootChargingStation(getChargingStationId(), "", getVendor(), getModel(), getProtocol(), getChargingStationSerialNumber(), getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber());
        assertFalse(result.isAccepted());
    }

    @Test
    public void testBootKnownChargingStation() {
        ChargingStation cs = new ChargingStation(getChargingStationId().getId(), getChargingStationAddress());
        cs.setRegistered(true);
        cs.setNumberOfConnectors(2);
        cs.setConfigured(true);
        chargingStationRepository.save(cs);

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(getChargingStationId(), getChargingStationAddress(), getVendor(), getModel(), getProtocol(), getChargingStationSerialNumber(), getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber());
        assertTrue(bootChargingStationResult.isAccepted());

        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("vendor", getVendor());
        attributes.put("model", getModel());
        attributes.put("address", getChargingStationAddress());
        attributes.put("chargingStationSerialNumber", getChargingStationSerialNumber());
        attributes.put("firmwareVersion", getFirmwareVersion());
        attributes.put("iccid", getIccid());
        attributes.put("imsi", getImsi());
        attributes.put("meterType", getMeterType());
        attributes.put("meterSerialNumber", getMeterSerialNumber());

        // test if the charging station is stored and in the state we expect it after a boot of an unknown charging station
        cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertEquals(cs.getId(), getChargingStationId().getId());
        assertEquals(cs.getIpAddress(), getChargingStationAddress());
        assertEquals(cs.getNumberOfConnectors(), 2);
        assertTrue(cs.isConfigured());
        assertTrue(cs.isRegistered());
        assertTrue(cs.isRegisteredAndConfigured());
        assertEquals(cs.getIpAddress(), getChargingStationAddress());

        verify(gateway).send(new BootChargingStationCommand(getChargingStationId(), getProtocol(), attributes));
    }

    @Test
    public void testHeartbeat() {
        domainService.heartbeat(getChargingStationId());

        verify(gateway).send(new HeartbeatCommand(getChargingStationId()));
    }

    //TODO rewrite test for new authorize functionality
//    @Test
//    public void testAuthorize() {
//        AuthorizationResult result = domainService.authorize(getChargingStationId(), getIdTag());
//        verify(gateway).sendAndWait(new AuthorizeCommand(getChargingStationId(), getIdentifyingToken()), 0, TimeUnit.SECONDS);
//    }

    @Test
    public void testConfigureChargingStation() {
        domainService.configureChargingStation(getChargingStationId(), getConfigurationItems());
        verify(gateway).send(new ConfigureChargingStationCommand(getChargingStationId(), getConfigurationItems()));
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownChargingStation() {
        domainService.startTransaction(new ChargingStationId(getRandomString()), new ConnectorId(1), getIdentifyingToken(), 0, new Date(), getReservationId(), getProtocol());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnregisteredChargingStation() {
        chargingStationRepository.save(new ChargingStation(getChargingStationId().getId()));

        domainService.startTransaction(getChargingStationId(), new ConnectorId(1), getIdentifyingToken(), 0, new Date(), getReservationId(), getProtocol());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnconfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(getChargingStationId().getId());
        cs.setRegistered(true);
        chargingStationRepository.save(cs);

        domainService.startTransaction(getChargingStationId(), new ConnectorId(1), getIdentifyingToken(), 0, new Date(), getReservationId(), getProtocol());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionInvalidConnector() {
        chargingStationRepository.save(getRegisteredAndConfiguredChargingStation());

        domainService.startTransaction(getChargingStationId(), new ConnectorId(999), getIdentifyingToken(), 0, new Date(), getReservationId(), getProtocol());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownConnector() {
        chargingStationRepository.save(getRegisteredAndConfiguredChargingStation());

        domainService.startTransaction(getChargingStationId(), new ConnectorId(getConnectors().size() + 1), getIdentifyingToken(), 0, new Date(), getReservationId(), getProtocol());
    }

    @Test
    public void testStartTransactionEmptyAttributesChargingStation() {
        chargingStationRepository.save(getRegisteredAndConfiguredChargingStation());

        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(getChargingStationId(), new ConnectorId(1), getIdentifyingToken(), 0, now, null, getProtocol());
        assertTrue(ocppTransactionId > 0);

        TransactionId transactionId = new NumberedTransactionId(getChargingStationId(), getProtocol(), ocppTransactionId);

        verify(gateway).send(new StartTransactionCommand(getChargingStationId(), transactionId, new ConnectorId(1), getIdentifyingToken(), 0, now, getEmptyAttributesMap()));
    }

    @Test
    public void testStartTransactionChargingStation() {
        chargingStationRepository.save(getRegisteredAndConfiguredChargingStation());

        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(getChargingStationId(), new ConnectorId(1), getIdentifyingToken(), 0, now, getReservationId(), getProtocol());
        assertTrue(ocppTransactionId > 0);

        TransactionId transactionId = new NumberedTransactionId(getChargingStationId(), getProtocol(), ocppTransactionId);

        verify(gateway).send(new StartTransactionCommand(getChargingStationId(), transactionId, new ConnectorId(1), getIdentifyingToken(), 0, now, getStartTransactionAttributesMap(getReservationNumber())));
    }

    @Test
    public void testStopTransaction() {
        int ocppTransactionId = 0;
        NumberedTransactionId transactionId = new NumberedTransactionId(getChargingStationId(), getProtocol(), ocppTransactionId);
        int meterStopValue = 1;
        Date now = new Date();

        domainService.stopTransaction(getChargingStationId(), transactionId, getIdentifyingToken(), meterStopValue, now, getEmptyMeterValuesList());

        verify(gateway).send(new StopTransactionCommand(getChargingStationId(), transactionId, getIdentifyingToken(), meterStopValue, now));
    }

    /**
     * Stopping a transaction with meter values should trigger a 'ProcessMeterValueCommand' besides the 'StopTransactionCommand'.
     */
    @Test
    public void testStopTransactionWithMeterValues() {
        chargingStationRepository.save(getRegisteredAndConfiguredChargingStation());

        // registers a transaction in the transactionRepository
        Date startTransactionDate = new Date();
        int ocppTransactionId = domainService.startTransaction(getChargingStationId(), getConnectorId(), getIdentifyingToken(), 0, startTransactionDate, getReservationId(), getProtocol());


        NumberedTransactionId transactionId = new NumberedTransactionId(getChargingStationId(), getProtocol(), ocppTransactionId);
        int meterStopValue = 1;
        Date stopTransactionDate = new Date();

        domainService.stopTransaction(getChargingStationId(), transactionId, getIdentifyingToken(), meterStopValue, stopTransactionDate, getMeterValuesList());

        verify(gateway).send(new StopTransactionCommand(getChargingStationId(), transactionId, getIdentifyingToken(), meterStopValue, stopTransactionDate));
        // stored transaction should provide the connector id that's needed to process the meter values
        verify(gateway).send(new ProcessMeterValueCommand(getChargingStationId(), transactionId, getConnectorId(), getMeterValuesList()));
    }

    @Test
    public void testRetrieveChargingStationAddress() {
        String address = domainService.retrieveChargingStationAddress(getChargingStationId());
        assertEquals(address, "");

        chargingStationRepository.save(getRegisteredAndConfiguredChargingStation());

        address = domainService.retrieveChargingStationAddress(getChargingStationId());
        assertEquals(address, getChargingStationAddress());
    }

    @Test
    public void testGenerateReservationIdentifier() {
        NumberedReservationId numberedReservationId = domainService.generateReservationIdentifier(getChargingStationId(), getProtocol());

        assertNotNull(numberedReservationId.getId());
        assertNotNull(numberedReservationId.getNumber());
    }

    @Test
    public void testReservationStatusChanged() {
        domainService.reservationStatusChanged(getChargingStationId(), getReservationId(), getReservationStatus());

        verify(gateway).send(new ReservationStatusChangedCommand(getChargingStationId(), getReservationId(), getReservationStatus()));
    }

}
