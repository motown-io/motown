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

import com.google.common.collect.ImmutableMap;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StatusNotificationCommand} is the command which is published when a charging station notifies Motown about
 * some status. Concrete implementations of this class will determine what the status is referring to.
 */
public abstract class StatusNotificationCommand {

    @TargetAggregateIdentifier
    protected final ChargingStationId chargingStationId;

    protected final ComponentStatus status;

    protected final Date timestamp;

    protected final Map<String, String> attributes;

    /**
     * Creates a {@code StatusNotificationCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param status            the status of the component
     * @param timestamp         the date and time
     * @param attributes        optional attributes
     * @throws NullPointerException if {@code chargingStationId}, {@code status}, {@code timestamp} or {@code attributes} is {@code null}.
     */
    public StatusNotificationCommand(ChargingStationId chargingStationId, ComponentStatus status, Date timestamp, Map<String, String> attributes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.status = checkNotNull(status);
        this.timestamp = checkNotNull(timestamp);
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the status.
     *
     * @return the status.
     */
    public ComponentStatus getStatus() {
        return status;
    }

    /**
     * Gets the timestamp at which the status notification occurred.
     *
     * @return the timestamp at which the status notification occurred.
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the optional attributes.
     *
     * @return the optional attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
}
