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

import javax.persistence.*;

@Entity
public class OpeningTime {

    private static final int MINUTES_PER_HOUR = 60;
    private static final String TIME_FORMAT = "%02d:%02d";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Day day;

    private Integer timeStart;

    private Integer timeStop;

    @Transient
    public String getTimeStartString() {
        return String.format(TIME_FORMAT, timeStart / MINUTES_PER_HOUR, timeStart % MINUTES_PER_HOUR);
    }

    @Transient
    public String getTimeStopString() {
        return String.format(TIME_FORMAT, timeStop / MINUTES_PER_HOUR, timeStop % MINUTES_PER_HOUR);
    }

    public Long getId() {
        return id;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Integer getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Integer timeStart) {
        this.timeStart = timeStart;
    }

    public Integer getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(Integer timeStop) {
        this.timeStop = timeStop;
    }

}
