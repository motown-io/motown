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

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StatusNotificationReceivedEvent} is the command which is published when a status notice from a
 * charging station has been received.
 */
public final class StatusNotificationReceivedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final ChargingStationComponent component;

    private final String componentId;

    private final ComponentStatus status;

    private final Date timeStamp;

    private final Map<String, String> attributes;

    /**
     * Creates a {@code StatusNotificationReceivedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param component         the component type
     * @param componentId       the component identifier which should be supplied if the component is not the whole chargingsation
     * @param status            the status of the component
     * @param timeStamp         the optional date and time
     * @param attributes        optional attributes
     * @throws NullPointerException if {@code chargingStationId}, {@code component}, {@code status} is {@code null}.
     */
    public StatusNotificationReceivedEvent(ChargingStationId chargingStationId, ChargingStationComponent component, String componentId, ComponentStatus status, Date timeStamp, Map<String, String> attributes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.component = checkNotNull(component);

        if(!ChargingStationComponent.CHARGING_STATION.equals(component)) {
            checkNotNull(componentId);
        }
        this.componentId = componentId;
        this.status = checkNotNull(status);
        this.timeStamp = timeStamp;
        this.attributes = attributes;
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public ChargingStationComponent getComponent() {
        return component;
    }

    public String getComponentId() {
        return componentId;
    }

    public ComponentStatus getStatus() {
        return status;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
