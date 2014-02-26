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

import static junit.framework.Assert.assertEquals;

public class OpeningTimeTest {
    private static final int START_HOUR = 12;
    private static final int START_MINUTES = 0;
    private static final int STOP_HOUR = 15;
    private static final int STOP_MINUTES = 24;

    @Test
    public void testConstructor() {
        Day wednesday = Day.WEDNESDAY;
        TimeOfDay start = new TimeOfDay(START_HOUR, START_MINUTES);
        TimeOfDay stop = new TimeOfDay(STOP_HOUR, STOP_MINUTES);

        OpeningTime openingTime = new OpeningTime(wednesday, start, stop);

        assertEquals(wednesday, openingTime.getDay());
        assertEquals(start, openingTime.getTimeStart());
        assertEquals(stop, openingTime.getTimeStop());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStopTimeNotAfterStartTime() {
        Day monday = Day.MONDAY;
        TimeOfDay start = new TimeOfDay(START_HOUR, START_MINUTES);
        TimeOfDay stop = new TimeOfDay(START_HOUR, START_MINUTES);

        new OpeningTime(monday, start, stop);
    }

    @Test
    public void testStopTimeInSameHour() {
        Day monday = Day.MONDAY;
        TimeOfDay start = new TimeOfDay(START_HOUR, START_MINUTES);
        TimeOfDay stop = new TimeOfDay(START_HOUR, STOP_MINUTES);

        new OpeningTime(monday, start, stop);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerExceptionBeingThrown() {
        new OpeningTime(null, null, null);
    }
}
