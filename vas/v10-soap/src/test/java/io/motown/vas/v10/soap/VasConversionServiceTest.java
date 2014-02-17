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
package io.motown.vas.v10.soap;

import io.motown.vas.v10.soap.schema.ChargePointStatus;
import io.motown.vas.viewmodel.model.ChargingStation;
import io.motown.vas.viewmodel.model.VasChargingStationStatus;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static org.junit.Assert.assertEquals;

public class VasConversionServiceTest {

    private VasConversionService service;

    @Before
    public void setup() {
        service = new VasConversionService();
    }

    @Test
    public void getStatusOfNewInstanceChargingStationThrowsNoExceptions() {
        service.getStatus(new ChargingStation(CHARGING_STATION_ID.getId()));
    }

    @Test
    public void getStatusAvailableValidateReturnValue() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setState(VasChargingStationStatus.AVAILABLE);

        ChargePointStatus status = service.getStatus(cs);

        assertEquals(ChargePointStatus.AVAILABLE, status);
    }

    @Test
    public void getStatusUnknownValidateReturnValue() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setState(VasChargingStationStatus.UNKNOWN);

        ChargePointStatus status = service.getStatus(cs);

        assertEquals(ChargePointStatus.UNKNOWN, status);
    }

    @Test
    public void getStatusUnavailableValidateReturnValue() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setState(VasChargingStationStatus.UNAVAILABLE);

        ChargePointStatus status = service.getStatus(cs);

        assertEquals(ChargePointStatus.UNAVAILABLE, status);
    }

    @Test
    public void getStatusOccupiedValidateReturnValue() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setState(VasChargingStationStatus.OCCUPIED);

        ChargePointStatus status = service.getStatus(cs);

        assertEquals(ChargePointStatus.OCCUPIED, status);
    }

    @Test
    public void getStatusNullValidateReturnValue() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());
        cs.setState(null);

        ChargePointStatus status = service.getStatus(cs);

        assertEquals(ChargePointStatus.UNKNOWN, status);
    }

    @Test
    public void getStatusShouldThrowNoExeptionsForAnyState() {
        ChargingStation cs = new ChargingStation(CHARGING_STATION_ID.getId());

        for (VasChargingStationStatus status : VasChargingStationStatus.values()) {
            cs.setState(status);

            // no exception should be thrown
            service.getStatus(cs);
        }
    }

}
