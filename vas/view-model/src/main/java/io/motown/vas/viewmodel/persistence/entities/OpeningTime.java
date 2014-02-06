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
package io.motown.vas.viewmodel.persistence.entities;

import io.motown.vas.viewmodel.Day;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.util.Date;

@Entity
public class OpeningTime {

    private static final int MINUTES_PER_HOUR = 60;

    @Id
    private String id;

    private Day day;

    private Boolean closed;

    private Integer timeStart;

    private Integer timeStop;

    private OpeningTime() {
        // Private no-arg constructor for Hibernate.
    }

    @ManyToOne
    @JoinColumn(name="id", insertable = false, updatable = false)
    private ChargingStation chargingStation;

    @Transient
    public String getTimeStartString() {
        return String.format("%02d:%02d", (timeStart / MINUTES_PER_HOUR), (timeStart % MINUTES_PER_HOUR));
    }

    @Transient
    public String getTimeStopString() {
        return String.format("%02d:%02d", (timeStop / MINUTES_PER_HOUR), timeStop % MINUTES_PER_HOUR);
    }

    @Transient
    public Boolean isDuringOpeningTime(Date givenDate) {
        assert givenDate != null;

        // Both Joda Time's DateTimeConstants as well as our Day use the ISO numbering as the value for the day (i.e. 1
        // is Monday and 7 is Sunday).
        Day givenDay = Day.fromValue(new DateTime(givenDate).getDayOfWeek());
        LocalTime givenTime = new LocalTime(givenDate);

        return givenDay == day && !closed && getTimeStartAsLocalTime().isBefore(givenTime) && getTimeStopAsLocalTime().isAfter(givenTime);
    }

    private LocalTime getTimeStartAsLocalTime() {
        return getAsLocalTime(timeStart);
    }

    private LocalTime getTimeStopAsLocalTime() {
        return getAsLocalTime(timeStop);
    }

    private LocalTime getAsLocalTime(Integer time) {
        return new LocalTime(time / MINUTES_PER_HOUR, time % MINUTES_PER_HOUR);
    }

    public String getId() {
        return id;
    }

    public Day getDay() {
        return day;
    }

    public Boolean getClosed() {
        return closed;
    }

    public Integer getTimeStart() {
        return timeStart;
    }

    public Integer getTimeStop() {
        return timeStop;
    }

    public ChargingStation getChargingStation() {
        return chargingStation;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public void setTimeStart(Integer timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeStop(Integer timeStop) {
        this.timeStop = timeStop;
    }

    public void setChargingStation(ChargingStation chargingStation) {
        this.chargingStation = chargingStation;
    }
}
