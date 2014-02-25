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
        private int value;

        public FromValueTest(Day day, int value) {
            this.day = day;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {Day.MONDAY, 1},
                    {Day.TUESDAY, 2},
                    {Day.WEDNESDAY, 3},
                    {Day.THURSDAY, 4},
                    {Day.FRIDAY, 5},
                    {Day.SATURDAY, 6},
                    {Day.SUNDAY, 7}
            });
        }

        @Test
        public void testFromValue() {
            assertEquals(day, Day.fromValue(value));
        }

        @Test(expected = IllegalArgumentException.class)
        public void testIllegalValue() {
            Day.fromValue(8);
        }
    }

    @RunWith(Parameterized.class)
    public static class ValueTest {
        private Day day;
        private int value;

        public ValueTest(Day day, int value) {
            this.day = day;
            this.value = value;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {Day.MONDAY, 1},
                    {Day.TUESDAY, 2},
                    {Day.WEDNESDAY, 3},
                    {Day.THURSDAY, 4},
                    {Day.FRIDAY, 5},
                    {Day.SATURDAY, 6},
                    {Day.SUNDAY, 7}
            });
        }

        @Test
        public void testValue() {
            assertTrue(day.value() == value);
        }
    }
}
