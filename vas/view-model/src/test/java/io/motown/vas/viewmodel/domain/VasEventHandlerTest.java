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
package io.motown.vas.viewmodel.domain;

import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.vas.viewmodel.VasEventHandler;
import io.motown.vas.viewmodel.persistence.entities.ChargingStation;
import io.motown.vas.viewmodel.persistence.repostories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.vas.viewmodel.domain.TestUtils.getChargingStationId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:vas-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class VasEventHandlerTest {

    private VasEventHandler eventHandler;

    @Autowired
    private ChargingStationRepository chargingStationRepository;

    @Before
    public void setUp() {
        chargingStationRepository.deleteAll();

        eventHandler = new VasEventHandler();

        eventHandler.setChargingStationRepository(chargingStationRepository);
    }

    @Test
    public void testChargingStationBootedEvent() {
        assertNull(chargingStationRepository.findOne(getChargingStationId().getId()));

        eventHandler.handle(new ChargingStationCreatedEvent(getChargingStationId()));

        ChargingStation cs = chargingStationRepository.findOne(getChargingStationId().getId());
        assertNotNull(cs);

        assertEquals(cs.getId(), getChargingStationId().getId());
    }

    //TODO: Finish tests - Ingo Pak, 21 Jan 2014

}
