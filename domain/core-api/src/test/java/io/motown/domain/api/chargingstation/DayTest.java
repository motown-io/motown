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

public class DayTest {
    private static final int MONDAY = 1;
    private static final int TUESDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int THURSDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;
    private static final int SUNDAY = 7;

    @Test
    public void testDays() {
        Day monday = Day.MONDAY;
        Day tuesday = Day.TUESDAY;
        Day wednesday = Day.WEDNESDAY;
        Day thursday = Day.THURSDAY;
        Day friday = Day.FRIDAY;
        Day saturday = Day.SATURDAY;
        Day sunday = Day.SUNDAY;

        assertEquals(monday.value(), MONDAY);
        assertEquals(tuesday.value(), TUESDAY);
        assertEquals(wednesday.value(), WEDNESDAY);
        assertEquals(thursday.value(), THURSDAY);
        assertEquals(friday.value(), FRIDAY);
        assertEquals(saturday.value(), SATURDAY);
        assertEquals(sunday.value(), SUNDAY);
        assertEquals(Day.fromValue(MONDAY), Day.MONDAY);
    }
}
