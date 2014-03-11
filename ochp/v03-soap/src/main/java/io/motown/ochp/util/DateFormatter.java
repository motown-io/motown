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
package io.motown.ochp.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateFormatter {

    public static String toISO8601 (Date date){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
        return fmt.print(dateTime);
    }

    /**
     * Substracts the start from the stop date, and converts the difference into a
     * string representing hh:MM:ss
     * @param start the start date time
     * @param stop the stop date time
     * @return String representing hh:MM:ss
     */
    public static String formatDuration(Date start, Date stop) {
        long duration = 0;

        if (stop != null && start != null){
            duration = stop.getTime() - start.getTime();
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
