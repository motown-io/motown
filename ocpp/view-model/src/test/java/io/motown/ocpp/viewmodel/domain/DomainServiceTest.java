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
import io.motown.ocpp.viewmodel.persistence.repostories.TransactionIdentifierRepository;
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
import java.util.concurrent.TimeUnit;

import static io.motown.ocpp.viewmodel.domain.TestUtils.*;
import static io.motown.ocpp.viewmodel.domain.TestUtils.getChargingStationAddress;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
    private TransactionIdentifierRepository transactionIdentifierRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() {

        chargingStationRepository.deleteAll();
        transactionIdentifierRepository.deleteAll();

        domainService = new DomainService();
        domainService.setChargingStationRepository(chargingStationRepository);
        domainService.setTransactionIdentifierRepository(transactionIdentifierRepository);
        domainService.setEntityManagerFactory(entityManagerFactory);

        gateway = mock(DomainCommandGateway.class);
        domainService.setCommandGateway(gateway);
    }

    @Test
    public void testBootUnknownChargingStation() {
        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(getChargingStationId(), getChargingStationAddress(), getVendor(), getModel(), getProtocol());
        assertFalse(bootChargingStationResult.isAccepted());

        verify(gateway).send( eq(new CreateChargingStationCommand(getChargingStationId(), false)), any(CommandCallback.class));
    }

    @Test
    public void testBootKnownChargingStation() {
        ChargingStation cs = new ChargingStation(getChargingStationId().getId());
        cs.setRegistered(true);
        cs.setNumberOfConnectors(2);
        cs.setConfigured(true);
        chargingStationRepository.save(cs);

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(getChargingStationId(), getChargingStationAddress(), getVendor(), getModel(), getProtocol());
        assertTrue(bootChargingStationResult.isAccepted());

        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("vendor", getVendor());
        attributes.put("model", getModel());
        attributes.put("address", getChargingStationAddress());

        // test if the charging station is stored and in the state we expect it after a boot of an unknown charging station
        cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertEquals(cs.getId(), getChargingStationId().getId());
        assertEquals(cs.getIpAddress(), getChargingStationAddress());
        assertEquals(cs.getNumberOfConnectors(), 2);
        assertTrue(cs.isConfigured());
        assertTrue(cs.isRegistered());
        assertTrue(cs.isRegisteredAndConfigured());

        verify(gateway).send( new BootChargingStationCommand(getChargingStationId(), getProtocol(), attributes) );
    }

    @Test
    public void testAuthorize() {
        domainService.authorize(getChargingStationId(), getIdTag());
        verify(gateway).sendAndWait(new AuthorizeCommand(getChargingStationId(), getIdTag()), 0, TimeUnit.SECONDS);
    }

    @Test
    public void testConfigureChargingStation() {
        domainService.configureChargingStation(getChargingStationId(), getConfigurationItems());
        verify(gateway).send( new ConfigureChargingStationCommand(getChargingStationId(), getConfigurationItems()) );
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnknownChargingStation() {
        domainService.startTransaction(new ChargingStationId(getRandomString()), 1, getIdTag(), 0, new Date());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnregisteredChargingStation() {
        chargingStationRepository.save(new ChargingStation(getChargingStationId().getId()));

        domainService.startTransaction(getChargingStationId(), 1, getIdTag(), 0, new Date());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTransactionUnconfiguredChargingStation() {
        ChargingStation cs = new ChargingStation(getChargingStationId().getId());
        cs.setRegistered(true);
        chargingStationRepository.save(cs);

        domainService.startTransaction(getChargingStationId(), 1, getIdTag(), 0, new Date());
    }

    @Test
    public void testStartTransactionChargingStation() {
        ChargingStation cs = new ChargingStation(getChargingStationId().getId());
        cs.setRegistered(true);
        cs.setNumberOfConnectors(2);
        cs.setConfigured(true);
        chargingStationRepository.save(cs);

        Date now = new Date();
        int ocppTransactionId = domainService.startTransaction(getChargingStationId(), 1, getIdTag(), 0, now);
        assertTrue(ocppTransactionId > 0);

        verify(gateway).send(new StartTransactionCommand(getChargingStationId(), TransactionIdentifierTranslator.toString(getChargingStationId(), ocppTransactionId), 1, getIdTag(), 0, now));
    }

}
