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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;

public class DiagnosticsUploadSettingsTest {

    private static final String UPLOAD_LOCATION = "http://localhost/";

    private static final int NUM_RETRIES = 3;

    private static final int RETRY_INTERVAL = 60;

    private static final Date START_TIME = FIVE_MINUTES_AGO;

    private static final Date STOP_TIME = TWO_MINUTES_AGO;

    @Test(expected = NullPointerException.class)
    public void illegalArgumentExceptionThrownWhenCreatingCommandWithoutUploadLocation() {
        new DiagnosticsUploadSettings(null);
    }

    @Test(expected = NullPointerException.class)
    public void illegalArgumentExceptionThrownWhenCreatingCommandWithoutUploadLocationMultipleParams() {
        new DiagnosticsUploadSettings(null, null, null, null, null);
    }

    @Test
    public void datesShouldNotBeModifiable() {
        Date startTime = new Date();
        Date endTime = new Date();
        DiagnosticsUploadSettings settings = new DiagnosticsUploadSettings(UPLOAD_LOCATION, NUM_RETRIES, RETRY_INTERVAL, startTime, endTime);

        startTime.setTime(0);
        endTime.setTime(0);

        assertFalse(startTime.equals(settings.getPeriodStartTime()));
        assertFalse(endTime.equals(settings.getPeriodStopTime()));
    }

    @Test
    public void exposedDatesShouldNotAffectInternalRepresentation() {
        DiagnosticsUploadSettings settings = new DiagnosticsUploadSettings(UPLOAD_LOCATION, NUM_RETRIES, RETRY_INTERVAL, START_TIME, STOP_TIME);

        Date startTime = settings.getPeriodStartTime();
        assert startTime != null;
        startTime.setTime(0);
        Date stopTime = settings.getPeriodStopTime();
        assert stopTime != null;
        stopTime.setTime(0);

        assertFalse(startTime.equals(settings.getPeriodStartTime()));
        assertFalse(stopTime.equals(settings.getPeriodStopTime()));
    }


    @Test
    public void testNullParams() {
        DiagnosticsUploadSettings settings = new DiagnosticsUploadSettings(UPLOAD_LOCATION);

        assertEquals(UPLOAD_LOCATION, settings.getUploadLocation());
        assertNull(settings.getNumRetries());
        assertNull(settings.getRetryInterval());
        assertNull(settings.getPeriodStartTime());
        assertNull(settings.getPeriodStopTime());
    }

}
