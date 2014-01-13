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

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MeterValue} holds information on the charging progress.
 */
public class MeterValue {

    private Date timestamp;

    private String value;

    /**
     * Creates a {@code MeterValue} holding a timestamp and a value.
     * @param timestamp
     * @param value
     * @throws NullPointerException if {@code timestamp}, or {@code value} is {@code null}.
     */
    public MeterValue(Date timestamp, String value) {
        this.timestamp = checkNotNull(timestamp);
        this.value = checkNotNull(value);
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeterValue that = (MeterValue) o;

        if (!timestamp.equals(that.timestamp)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
