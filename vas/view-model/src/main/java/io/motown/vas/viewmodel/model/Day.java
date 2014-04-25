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
package io.motown.vas.viewmodel.model;

import static com.google.common.base.Preconditions.checkNotNull;

public enum Day {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String value;

    private Day(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Day fromValue(String value) {
        for (Day day : Day.values()) {
            if (day.value.equalsIgnoreCase(checkNotNull(value))) {
                return day;
            }
        }
        throw new IllegalArgumentException(value);
    }

    @Override
    public String toString() {
        return value;
    }
}