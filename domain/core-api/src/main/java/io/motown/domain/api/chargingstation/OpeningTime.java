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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Denotes the opening times of a charging station.
 * The opening times exist of a day (where monday is the first day of the week), a starting time and an ending time.
 */
public final class OpeningTime {
    private final Day day;
    private final TimeOfDay timeStart;
    private final TimeOfDay timeStop;

    /**
     * Construct a new {@code OpeningTime} object using a day, timeStart and timeStop
     *
     * @param day the day of the week (where 1 is monday).
     * @param timeStart the time when the charging station is first open.
     * @param timeStop the time when the charging station closes.
     * @throws java.lang.NullPointerException when one of the parameters is {@code null}.
     */
    public OpeningTime(Day day, TimeOfDay timeStart, TimeOfDay timeStop) {
        this.day = checkNotNull(day);
        this.timeStart = checkNotNull(timeStart);
        this.timeStop = checkNotNull(timeStop);
    }

    public Day getDay() {
        return day;
    }

    public TimeOfDay getTimeStart() {
        return timeStart;
    }

    public TimeOfDay getTimeStop() {
        return timeStop;
    }
}
