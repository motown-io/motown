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
package io.motown.operatorapi.viewmodel.persistence.entities;

import io.motown.domain.api.chargingstation.Day;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class OpeningTime {
    private Day day;

    @Temporal(TemporalType.TIME)
    private Date timeStart;

    @Temporal(TemporalType.TIME)
    private Date timeStop;

    public OpeningTime() {
    }

    public OpeningTime(Day day, Date timeStart, Date timeStop) {
        this.day = day;
        this.timeStart = new Date(timeStart.getTime());
        this.timeStop = new Date(timeStop.getTime());
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Date getTimeStart() {
        return new Date(timeStart.getTime());
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = new Date(timeStart.getTime());
    }

    public Date getTimeStop() {
        return new Date(timeStop.getTime());
    }

    public void setTimeStop(Date timeStop) {
        this.timeStop = new Date(timeStop.getTime());
    }
}
