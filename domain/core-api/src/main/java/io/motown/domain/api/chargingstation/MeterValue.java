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

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MeterValue} holds information on the charging progress.
 */
public final class MeterValue {

    private final Date timestamp;

    private final String value;

    private final ReadingContext context;

    private final ValueFormat format;

    private final Measurand measurand;

    private final Location location;

    private final UnitOfMeasure unit;

    /**
     * Creates a new {@code MeterValue}.
     *
     * @param timestamp the timestamp.
     * @param value     the value.
     * @throws NullPointerException if {@code timestamp} or {@code value} is null.
     */
    public MeterValue(Date timestamp, String value) {
        this(timestamp, value, ReadingContext.PERIODIC_SAMPLE, ValueFormat.RAW, Measurand.IMPORTED_ACTIVE_ENERGY_REGISTER, Location.OUTLET, UnitOfMeasure.WATT_HOUR);
    }

    /**
     * Creates a new {@code MeterValue}.
     *
     * @param timestamp the timestamp.
     * @param value     the value.
     * @param context   the reading context.
     * @param format    the format.
     * @param measurand the measurand.
     * @param location  the location.
     * @param unit      the unit of measure.
     * @throws NullPointerException if {@code timestamp}, {@code value}, {@code context}, {@code format}, {@code measurand}, {@code location}, or {@code unit}.
     */
    public MeterValue(Date timestamp, String value, ReadingContext context, ValueFormat format, Measurand measurand, Location location, UnitOfMeasure unit) {
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
        this.value = checkNotNull(value);
        this.context = checkNotNull(context);
        this.format = checkNotNull(format);
        this.measurand = checkNotNull(measurand);
        this.location = checkNotNull(location);
        this.unit = checkNotNull(unit);
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    /**
     * Gets the value.
     *
     * @return the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the context.
     *
     * @return the context.
     */
    public ReadingContext getContext() {
        return context;
    }

    /**
     * Gets the format.
     *
     * @return the format.
     */
    public ValueFormat getFormat() {
        return format;
    }

    /**
     * Gets the measurand.
     *
     * @return the measurand.
     */
    public Measurand getMeasurand() {
        return measurand;
    }

    /**
     * Gets the location.
     *
     * @return the location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the unit of measure.
     *
     * @return the unit of measure.
     */
    public UnitOfMeasure getUnit() {
        return unit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(timestamp, value, context, format, measurand, location, unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MeterValue other = (MeterValue) obj;
        return Objects.equals(this.timestamp, other.timestamp) && Objects.equals(this.value, other.value) && Objects.equals(this.context, other.context) && Objects.equals(this.format, other.format) && Objects.equals(this.measurand, other.measurand) && Objects.equals(this.location, other.location) && Objects.equals(this.unit, other.unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("timestamp", timestamp)
                .add("value", value)
                .add("context", context)
                .add("format", format)
                .add("measurand", measurand)
                .add("location", location)
                .add("unit", unit)
                .toString();
    }
}
