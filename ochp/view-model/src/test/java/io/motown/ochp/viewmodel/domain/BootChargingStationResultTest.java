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

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BootChargingStationResultTest {

    public static final int FIVE_MINUTES = 900;

    @Test
    public void testBootChargingStationResult() {
        Date now = new Date();

        BootChargingStationResult result = new BootChargingStationResult(true, FIVE_MINUTES, now);

        assertTrue(result.isAccepted());
        assertEquals(result.getHeartbeatInterval(), FIVE_MINUTES);
        assertEquals(result.getTimeStamp(), now);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();

        BootChargingStationResult result = new BootChargingStationResult(true, FIVE_MINUTES, now);
        result.getTimeStamp().setTime(FIVE_MINUTES);
        assertEquals(now, result.getTimeStamp());
    }


}
