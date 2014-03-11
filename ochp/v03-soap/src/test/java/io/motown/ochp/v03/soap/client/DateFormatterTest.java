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
package io.motown.ochp.v03.soap.client;

import io.motown.ochp.util.DateFormatter;
import junit.framework.Assert;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.jgroups.util.Util.assertEquals;

public class DateFormatterTest {

    @Test
    public void formatDateToISO8601() {
        int year=2014;
        int month=2;
        int day=21;
        int hour=17;
        int minute=18;
        int seconds=9;

        GregorianCalendar cal = new GregorianCalendar();
        cal.set(year, month, day, hour, minute, seconds);

        //Fixate the default JodaTime timezone for this test
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/Amsterdam"));

        String formattedDate = DateFormatter.toISO8601(cal.getTime());
        System.out.println("Formatted: " + formattedDate);
        assertEquals(formattedDate, "2014-03-21T17:18:09+01:00");
    }

    @Test
    public void testDurationCalculation() {
        int hours = 10;
        int minutes = 31;
        int seconds = 9;
        long duration = (1000 * (seconds + (minutes * 60) + (hours * 60 * 60) ));

        Date now = new Date();
        Date past = new Date(now.getTime() -  duration);

        String formattedDuration = DateFormatter.formatDuration(past, now);
        System.out.println("duration="+duration + " formatted="+formattedDuration);
        Assert.assertEquals(formattedDuration, "10:31:09");
    }

    @Test
    public void testZeroDurationCalculation() {
        long duration = 0;

        Date now = new Date();
        Date past = new Date(now.getTime() -  duration);

        String formattedDuration = DateFormatter.formatDuration(past, now);
        System.out.println("duration="+duration + " formatted="+formattedDuration);
        Assert.assertEquals(formattedDuration, "00:00:00");
    }

    @Test
    public void testMoreThan99hrsDurationCalculation() {
        int hours = 100;
        int minutes = 31;
        int seconds = 9;
        long duration = (1000 * (seconds + (minutes * 60) + (hours * 60 * 60) ));

        Date now = new Date();
        Date past = new Date(now.getTime() -  duration);

        String formattedDuration = DateFormatter.formatDuration(past, now);
        System.out.println("duration="+duration + " formatted="+formattedDuration);
        Assert.assertEquals(formattedDuration, "100:31:09");
    }
}
