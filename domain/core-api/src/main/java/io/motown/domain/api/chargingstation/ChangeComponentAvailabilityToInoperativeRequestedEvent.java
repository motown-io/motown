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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChangeComponentAvailabilityToInoperativeRequestedEvent} is the event which is published when a request has
 * been made to change the availability of a charging station to inoperative. Protocol add-ons should respond to this
 * event (if applicable) and request a charging station to change its availability.
 */
public final class ChangeComponentAvailabilityToInoperativeRequestedEvent extends ChangeAvailabilityToInoperativeRequestedEvent {

    private final ComponentId componentId;

    private final ChargingStationComponent component;

    /**
     * Creates a {@code ChangeChargingStationAvailabilityToInoperativeRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @param componentId       component's id.
     * @param component         the component's type.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol} or {@code evseId} is {@code null}.
     */
    public ChangeComponentAvailabilityToInoperativeRequestedEvent(ChargingStationId chargingStationId, String protocol, ComponentId componentId, ChargingStationComponent component, IdentityContext identityContext) {
        super(chargingStationId, protocol, identityContext);
        this.componentId = checkNotNull(componentId);
        this.component = checkNotNull(component);
    }

    /**
     * Gets the component's id.
     *
     * @return the component's id.
     */
    public ComponentId getComponentId() {
        return componentId;
    }

    /**
     * Gets the component's type.
     *
     * @return the component's type.
     */
    public ChargingStationComponent getComponent() {
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(componentId, component);
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
        if (!super.equals(obj)) {
            return false;
        }
        final ChangeComponentAvailabilityToInoperativeRequestedEvent other = (ChangeComponentAvailabilityToInoperativeRequestedEvent) obj;
        return Objects.equals(this.componentId, other.componentId) && Objects.equals(this.component, other.component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("chargingStationId", getChargingStationId())
                .add("protocol", getProtocol())
                .add("componentId", componentId)
                .add("component", component)
                .add("identityContext", getIdentityContext())
                .toString();
    }
}
