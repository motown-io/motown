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
package io.motown.domain.api.chargingstation;

import org.junit.Test;

import java.util.Date;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.FIVE_MINUTES_AGO;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.TWO_MINUTES_AGO;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RequestDiagnosticsCommandTest {

    private static final String UPLOAD_LOCATION = "http://localhost/";

    private static final int NUM_RETRIES = 3;

    private static final int RETRY_INTERVAL = 60;

    private static final Date START_TIME = FIVE_MINUTES_AGO;

    private static final Date END_TIME = TWO_MINUTES_AGO;

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNull() {
        new RequestDiagnosticsCommand(null, UPLOAD_LOCATION, null, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void illegalArgumentExceptionThrownWhenCreatingCommandWithoutUploadLocation() {
        new RequestDiagnosticsCommand(CHARGING_STATION_ID, null, null, null, null, null);
    }

    @Test
    public void datesShouldNotBeModifiable() {
        Date startTime = new Date();
        Date endTime = new Date();
        RequestDiagnosticsCommand command = new RequestDiagnosticsCommand(CHARGING_STATION_ID, UPLOAD_LOCATION, NUM_RETRIES, RETRY_INTERVAL, startTime, endTime);

        startTime.setTime(0);
        endTime.setTime(0);

        assertFalse(startTime.equals(command.getPeriodStartTime()));
        assertFalse(endTime.equals(command.getPeriodEndTime()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void exposedDatesShouldNotAffectInternalRepresentation() {
        RequestDiagnosticsCommand command = new RequestDiagnosticsCommand(CHARGING_STATION_ID, UPLOAD_LOCATION, NUM_RETRIES, RETRY_INTERVAL, START_TIME, END_TIME);

        Date startTime = command.getPeriodStartTime();
        startTime.setTime(0);
        Date endTime = command.getPeriodEndTime();
        endTime.setTime(0);

        assertFalse(startTime.equals(command.getPeriodStartTime()));
        assertFalse(endTime.equals(command.getPeriodEndTime()));
    }

    @Test
    public void constructorSetsFields() {
        RequestDiagnosticsCommand command = new RequestDiagnosticsCommand(CHARGING_STATION_ID, UPLOAD_LOCATION, NUM_RETRIES, RETRY_INTERVAL, START_TIME, END_TIME);

        assertEquals(CHARGING_STATION_ID, command.getChargingStationId());
        assertEquals(UPLOAD_LOCATION, command.getUploadLocation());
        assertEquals(Integer.valueOf(NUM_RETRIES), command.getNumRetries());
        assertEquals(Integer.valueOf(RETRY_INTERVAL), command.getRetryInterval());
        assertEquals(START_TIME, command.getPeriodStartTime());
        assertEquals(END_TIME, command.getPeriodEndTime());
    }

    @Test
    public void testNullStartAndEndPeriods() {
        RequestDiagnosticsCommand command = new RequestDiagnosticsCommand(CHARGING_STATION_ID, UPLOAD_LOCATION, NUM_RETRIES, RETRY_INTERVAL, null, null);

        assertNull(command.getPeriodStartTime());
        assertNull(command.getPeriodEndTime());
    }

}
