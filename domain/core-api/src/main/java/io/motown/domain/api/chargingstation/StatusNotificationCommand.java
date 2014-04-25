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
import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StatusNotificationCommand} is the command which is published when a charging station notifies Motown about
 * some status. Concrete implementations of this class will determine what the status is referring to.
 */
public abstract class StatusNotificationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final ComponentStatus status;

    private final Date timestamp;

    private final Map<String, String> attributes;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code StatusNotificationCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param status            the status of the component.
     * @param timestamp         the date and time.
     * @param attributes        optional attributes.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code status}, {@code timestamp}, {@code attributes}
     *                          or {@code identityContext} is {@code null}.
     */
    public StatusNotificationCommand(ChargingStationId chargingStationId, ComponentStatus status, Date timestamp,
                                     Map<String, String> attributes, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.status = checkNotNull(status);
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
        this.identityContext = checkNotNull(identityContext);
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
        return new Date(timestamp.getTime());
    }

    /**
     * Gets the optional attributes.
     *
     * @return the optional attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, status, timestamp, attributes, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StatusNotificationCommand other = (StatusNotificationCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.status, other.status) && Objects.equals(this.timestamp, other.timestamp) && Objects.equals(this.attributes, other.attributes) && Objects.equals(this.identityContext, other.identityContext);
    }
}
