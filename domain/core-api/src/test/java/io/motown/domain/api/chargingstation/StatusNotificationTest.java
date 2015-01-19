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

import io.motown.domain.api.chargingstation.test.ChargingStationTestUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class StatusNotificationTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullStatus() {
        new StatusNotification(null, new Date(), Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullDate() {
        new StatusNotification(ComponentStatus.AVAILABLE, null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullAttributes() {
        new StatusNotification(ComponentStatus.AVAILABLE, new Date(), null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        StatusNotification statusNotification = new StatusNotification(ComponentStatus.AVAILABLE, now, Collections.<String, String>emptyMap());
        statusNotification.getTimeStamp().setTime(ChargingStationTestUtils.TWO_MINUTES_AGO.getTime());
        assertEquals(now, statusNotification.getTimeStamp());
    }

}
