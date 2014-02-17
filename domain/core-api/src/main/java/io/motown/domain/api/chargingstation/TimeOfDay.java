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

public final class TimeOfDay {
    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTES = 0;
    private static final int MAX_MINUTES = 59;
    private final int hourOfDay;
    private final int minutesInHour;

    public TimeOfDay(int hourOfDay, int minutesInHour) {
        checkArgument(hourOfDay >= MIN_HOUR && hourOfDay <= MAX_HOUR);
        checkArgument(minutesInHour >= MIN_MINUTES && minutesInHour <= MAX_MINUTES);
        this.hourOfDay = hourOfDay;
        this.minutesInHour = minutesInHour;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinutesInHour() {
        return minutesInHour;
    }
}
