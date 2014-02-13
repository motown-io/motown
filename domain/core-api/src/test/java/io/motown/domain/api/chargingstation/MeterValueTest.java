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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MeterValueTest {

    @Test(expected = NullPointerException.class)
    public void createMeterValueWithNullTimestampThrowsNullPointerException() {
        new MeterValue(null, "123");
    }

    @Test(expected = NullPointerException.class)
    public void createMeterValueWithNullDateThrowsNullPointerException() {
        new MeterValue(new Date(), null);
    }

    @Test
    public void getTimestampShouldReturnDefensiveCopy() {
        Date now = new Date();
        MeterValue meterValue = new MeterValue(now, "123");

        // If this method returns a defensive copy, changing date should affect the internal timestamp.
        meterValue.getTimestamp().setYear(81);

        assertEquals(now, meterValue.getTimestamp());
    }

    @Test
    public void equalsAndHashcodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(MeterValue.class).verify();
    }
}
