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
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MeterValue} holds information on the charging progress.
 */
public final class MeterValue {

    private final Date timestamp;

    private final String value;

    /**
     * Creates a {@code MeterValue} holding a timestamp and a value.
     *
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
        return new Date(timestamp.getTime());
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MeterValue other = (MeterValue) obj;
        return Objects.equals(this.timestamp, other.timestamp) && Objects.equals(this.value, other.value);
    }
}
