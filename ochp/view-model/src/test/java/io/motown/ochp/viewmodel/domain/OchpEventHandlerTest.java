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
package io.motown.ochp.viewmodel.domain;

import io.motown.domain.api.chargingstation.TransactionStartedEvent;
import io.motown.domain.api.chargingstation.TransactionStoppedEvent;
import io.motown.ochp.viewmodel.OchpEventHandler;
import io.motown.ochp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ochp.viewmodel.persistence.entities.Transaction;
import io.motown.ochp.viewmodel.persistence.repostories.ChargingStationRepository;
import io.motown.ochp.viewmodel.persistence.repostories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:ochp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OchpEventHandlerTest {

    private OchpEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository.deleteAll();
        chargingStationRepository.deleteAll();

        eventHandler = new OchpEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
        eventHandler.setTransactionRepository(transactionRepository);
    }

    @Test
    public void testTransaction() {
        chargingStationRepository.saveAndFlush(new ChargingStation(CHARGING_STATION_ID.getId()));
        transactionRepository.saveAndFlush(new Transaction(chargingStationRepository.findByChargingStationId(CHARGING_STATION_ID.getId()), TRANSACTION_ID.getId()));
        assertNotNull(transactionRepository.findByTransactionId(TRANSACTION_ID.getId()));
    }

    @Test
    public void testTransactionWithoutChargingStation() {
        transactionRepository.saveAndFlush(new Transaction(TRANSACTION_ID.getId()));
        assertNotNull(transactionRepository.findByTransactionId(TRANSACTION_ID.getId()));
    }

    @Test
    public void testHandleTransactionStartedEvent() {
        eventHandler.handle(new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, METER_START, FIVE_MINUTES_AGO, CONFIGURATION_ITEMS));
    }

    @Test
    public void testHandleTransactionStoppedEvent() {
        eventHandler.handle(new TransactionStoppedEvent(CHARGING_STATION_ID, TRANSACTION_ID, IDENTIFYING_TOKEN, METER_STOP, TWO_MINUTES_AGO));
    }

}
