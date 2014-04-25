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

import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repositories.ChargingStationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.viewmodel.domain.OccpViewModelTestUtils.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ContextConfiguration("classpath:ocpp-view-model-test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CreateChargingStationCommandCallbackTest {

    private DomainService domainService;

    private ChargingStationRepository chargingStationRepository;

    private CreateChargingStationCommandCallback createChargingStationCommandCallback;

    @Before
    public void setUp() {
        chargingStationRepository = mock(ChargingStationRepository.class);

        domainService = mock(DomainService.class);
        domainService.setChargingStationRepository(chargingStationRepository);

        DomainCommandGateway gateway = mock(DomainCommandGateway.class);
        domainService.setCommandGateway(gateway);

        createChargingStationCommandCallback = new CreateChargingStationCommandCallback(CHARGING_STATION_ID, CHARGING_STATION_ADDRESS, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL,
                CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY, chargingStationRepository, domainService);
    }

    @Test
    public void testOnSuccess() {
        createChargingStationCommandCallback.onSuccess(new Object());

        verify(domainService).bootChargingStation(CHARGING_STATION_ID, CHARGING_STATION_ADDRESS, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, PROTOCOL, CHARGING_STATION_SERIAL_NUMBER,
                CHARGE_BOX_SERIAL_NUMBER, getFirmwareVersion(), getIccid(), getImsi(), getMeterType(), getMeterSerialNumber(), ADD_ON_IDENTITY);
        verify(chargingStationRepository).createOrUpdate(new ChargingStation(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void testOnFailure() {
        // should do nothing
        createChargingStationCommandCallback.onFailure(new Throwable());
    }

}
