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

@RunWith(Enclosed.class)
public class TimeOfDayTest {
    public static class TestConstructor {
        private static final int HOUR = 12;
        private static final int MINUTES = 0;

        @Test
        public void testConstructor() {
            TimeOfDay timeOfDay = new TimeOfDay(HOUR, MINUTES);

            assertEquals(HOUR, timeOfDay.getHourOfDay());
            assertEquals(MINUTES, timeOfDay.getMinutesInHour());
        }
    }

    @RunWith(Parameterized.class)
    public static class InvalidArgumentsTest {
        private int hour;
        private int minutes;

        public InvalidArgumentsTest(int hour, int minutes) {
            this.hour = hour;
            this.minutes = minutes;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {-1, 0},
                    {24, 0},
                    {0, -1},
                    {0, 60}
            });
        }

        @Test(expected = IllegalArgumentException.class)
        public void testTimeOfDay() {
            new TimeOfDay(hour, minutes);
        }
    }
}