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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(Enclosed.class)
public class DayTest {

    @RunWith(Parameterized.class)
    public static class FromValueTest {
        private Day day;
        private String value;

        public FromValueTest(Day day, String value) {
            this.day = day;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {Day.MONDAY, "Monday"},
                    {Day.TUESDAY, "Tuesday"},
                    {Day.WEDNESDAY, "Wednesday"},
                    {Day.THURSDAY, "Thursday"},
                    {Day.FRIDAY, "Friday"},
                    {Day.SATURDAY, "Saturday"},
                    {Day.SUNDAY, "Sunday"},
                    {Day.MONDAY, "MONDAY"},
                    {Day.TUESDAY, "TUESDAY"},
                    {Day.WEDNESDAY, "WEDNESDAY"},
                    {Day.THURSDAY, "THURSDAY"},
                    {Day.FRIDAY, "FRIDAY"},
                    {Day.SATURDAY, "SATURDAY"},
                    {Day.SUNDAY, "SUNDAY"}
            });
        }

        @Test
        public void testFromValue() {
            assertEquals(day, Day.fromValue(value));
        }

        @Test(expected = IllegalArgumentException.class)
        public void testIllegalValue() {
            Day.fromValue("Maandag");
        }

        @Test(expected = NullPointerException.class)
        public void testNullValue() {
            Day.fromValue(null);
        }
    }

    @RunWith(Parameterized.class)
    public static class ValueTest {
        private Day day;
        private String value;

        public ValueTest(Day day, String value) {
            this.day = day;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {Day.MONDAY, "Monday"},
                    {Day.TUESDAY, "Tuesday"},
                    {Day.WEDNESDAY, "Wednesday"},
                    {Day.THURSDAY, "Thursday"},
                    {Day.FRIDAY, "Friday"},
                    {Day.SATURDAY, "Saturday"},
                    {Day.SUNDAY, "Sunday"}
            });
        }

        @Test
        public void testValue() {
            assertTrue(day.toString().equals(value));
        }
    }
}
