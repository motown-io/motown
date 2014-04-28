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
package io.motown.operatorapi.viewmodel;

import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.chargingstation.ChargingStationSentMeterValuesEvent;
import io.motown.domain.api.chargingstation.ComponentStatus;
import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.entities.Evse;
import io.motown.operatorapi.viewmodel.persistence.entities.Transaction;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import io.motown.operatorapi.viewmodel.persistence.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.*;

@ContextConfiguration("classpath:operator-api-view-model-test-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionEventListenerTest {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    private TransactionEventListener listener;

    @Before
    public void setUp() throws Exception {
        listener = new TransactionEventListener();
        listener.setRepository(repository);
    }

    @Test
    public void testHandleTransactionStartedEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN_ACCEPTED, METER_START, new Date(), BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
        Transaction transaction = repository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transaction);
        assertEquals(0, transaction.getMeterStop());
        assertNull(transaction.getStoppedTimestamp());
    }

    @Test
    public void testHandleTransactionStoppedEvent() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setEvses(ImmutableSet.<Evse>builder().add(new Evse("1", ComponentStatus.AVAILABLE)).build());
        chargingStationRepository.createOrUpdate(cs);

        listener.handle(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN_ACCEPTED, METER_START, new Date(), BOOT_NOTIFICATION_ATTRIBUTES, IDENTITY_CONTEXT));
        Transaction transaction = repository.findByTransactionId(TRANSACTION_ID.getId());
        assertNotNull(transaction);
        assertEquals(0, transaction.getMeterStop());
        assertNull(transaction.getStoppedTimestamp());

        listener.handle(new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN_ACCEPTED, METER_STOP, new Date(), IDENTITY_CONTEXT));
        transaction = repository.findByTransactionId(TRANSACTION_ID.getId());
        assertTrue(transaction.getMeterStop() > 0 && transaction.getMeterStop() > transaction.getMeterStart());
        assertNotNull(transaction.getStoppedTimestamp());
        assertTrue(transaction.getStoppedTimestamp().after(transaction.getStartedTimestamp()));
    }

    @Test
    public void testHandleChargingStationSentMeterValuesEvent() {
        Transaction transaction = new Transaction(CHARGING_STATION_ID.getId(), TRANSACTION_ID.getId());
        transaction.setEvseId(EVSE_ID);
        repository.createOrUpdate(transaction);
        assertTrue(transaction.getMeterValues().isEmpty());

        listener.handle(new ChargingStationSentMeterValuesEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, METER_VALUES, IDENTITY_CONTEXT));
        transaction = repository.findByTransactionId(TRANSACTION_ID.getId());
        assertFalse(transaction.getMeterValues().isEmpty());
        assertEquals(2, transaction.getMeterValues().size());
    }
}
