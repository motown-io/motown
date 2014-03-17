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
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateFormatter {

    /**
     * Private no-arg constructor to prevent instantiation of this utility class.
     */
    private DateFormatter() {
    }

    private static DateTimeFormatter createISO8601Formatter() {
        return ISODateTimeFormat.dateTimeNoMillis();
    }

    /**
     * Formats a date into an ISO8601 string
     * @param date
     * @return ISO8601 string
     */
    public static String toISO8601 (Date date){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter fmt = createISO8601Formatter();
        return fmt.print(dateTime);
    }

    /**
     * Formats an ISO8601 string into a Date
     * @param dateString
     * @return Date
     */
    public static Date fromISO8601 (String dateString){
        DateTimeFormatter fmt = createISO8601Formatter();
        return fmt.parseDateTime(dateString).toDate();
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

        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % DateTimeConstants.SECONDS_PER_MINUTE;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % DateTimeConstants.MINUTES_PER_HOUR;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
