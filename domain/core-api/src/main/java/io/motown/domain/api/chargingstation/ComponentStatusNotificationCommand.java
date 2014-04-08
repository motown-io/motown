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

import io.motown.domain.api.security.IdentityContext;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ComponentStatusNotificationCommand} is the command which is published when a charging station notifies Motown
 * about the status of one of its components.
 */
public final class ComponentStatusNotificationCommand extends StatusNotificationCommand {

    private final ChargingStationComponent component;

    private final ComponentId componentId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code ComponentStatusNotificationCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param component         the component type.
     * @param componentId       the component identifier.
     * @param status            the status of the component.
     * @param timeStamp         the optional date and time.
     * @param attributes        optional attributes.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code component}, {@code componentId}, {@code status} or {@code attributes} or is
     *                             {@code identityContext} is {@code null}.
     */
    public ComponentStatusNotificationCommand(ChargingStationId chargingStationId, ChargingStationComponent component, ComponentId componentId,
                                              ComponentStatus status, Date timeStamp, Map<String, String> attributes, IdentityContext identityContext) {
        super(chargingStationId, status, timeStamp, attributes);
        this.component = checkNotNull(component);
        this.componentId = checkNotNull(componentId);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the type of the component whose status has changed.
     *
     * @return the type of the component whose status has changed.
     */
    public ChargingStationComponent getComponent() {
        return component;
    }

    /**
     * Gets the component identifier of the component whose status has changed.
     *
     * @return the component identifier of the component whose status has changed.
     */
    public ComponentId getComponentId() {
        return componentId;
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
        return 31 * super.hashCode() + Objects.hash(component, componentId, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final ComponentStatusNotificationCommand other = (ComponentStatusNotificationCommand) obj;
        return Objects.equals(this.component, other.component) && Objects.equals(this.componentId, other.componentId) && Objects.equals(this.identityContext, other.identityContext);
    }
}
