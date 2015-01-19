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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Denotes a time of the day, split into the hour field (0-23) and a minute field (0-59).
 */
public final class TimeOfDay {
    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTES = 0;
    private static final int MAX_MINUTES = 59;
    private final int hourOfDay;
    private final int minutesInHour;

    /**
     * Constructs a {@code TimeOfDay} object using the hour and minute field.
     *
     * @param hourOfDay the hour of the day (0-23).
     * @param minutesInHour the minute in that specific hour (0-59).
     * @throws java.lang.IllegalArgumentException if the hour isn't between 0 and 23 (inclusive) or the minute field isn't between 0 and 59 (inclusive).
     */
    public TimeOfDay(int hourOfDay, int minutesInHour) {
        checkArgument(hourOfDay >= MIN_HOUR && hourOfDay <= MAX_HOUR);
        checkArgument(minutesInHour >= MIN_MINUTES && minutesInHour <= MAX_MINUTES);
        this.hourOfDay = hourOfDay;
        this.minutesInHour = minutesInHour;
    }

    /**
     * Gets the hour field.
     * @return the hour of the day.
     */
    public int getHourOfDay() {
        return hourOfDay;
    }

    /**
     * Gets the minute field.
     * @return the minutes in the hour specified by {@code hourOfDay}.
     */
    public int getMinutesInHour() {
        return minutesInHour;
    }
}
