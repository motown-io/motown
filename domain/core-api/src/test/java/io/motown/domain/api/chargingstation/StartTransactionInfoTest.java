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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertEquals;

public class StartTransactionInfoTest {

    @Test(expected = NullPointerException.class)
    public void nullEvseIdShouldThrowException() {
        new StartTransactionInfo(null, 0, new Date(), IDENTIFYING_TOKEN, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullTimestampShouldThrowException() {
        new StartTransactionInfo(EVSE_ID, 0, null, IDENTIFYING_TOKEN, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullIdentifyingTokenShouldThrowException() {
        new StartTransactionInfo(EVSE_ID, 0, new Date(), null, Collections.<String, String>emptyMap());
    }

    @Test(expected = NullPointerException.class)
    public void nullAttributesShouldThrowException() {
        new StartTransactionInfo(EVSE_ID, 0, new Date(), IDENTIFYING_TOKEN, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void paramsShouldBeImmutableCopy() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("key", "value");

        new StartTransactionInfo(EVSE_ID, 0, new Date(), IDENTIFYING_TOKEN, attributes).getAttributes().put("key2", "value2");
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        StartTransactionInfo startTransactionInfo = new StartTransactionInfo(EVSE_ID, 0, now, IDENTIFYING_TOKEN, Collections.<String, String>emptyMap());
        startTransactionInfo.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, startTransactionInfo.getTimestamp());
    }

}
