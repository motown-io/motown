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

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChangeComponentAvailabilityToOperativeCommand} is the command which is published when a charging station's
 * components' availability has changed to operative.
 */
public final class ChangeComponentAvailabilityToOperativeCommand extends ChangeAvailabilityToOperativeCommand {

    private final ComponentId componentId;

    private final ChargingStationComponent component;

    /**
     * Creates a {@code ChangeComponentAvailabilityToOperativeCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param componentId       the identifier of the component.
     * @param component         the component type.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code componentId}, {@code component}, or {@code identityContext} is {@code null}.
     */
    public ChangeComponentAvailabilityToOperativeCommand(ChargingStationId chargingStationId, ComponentId componentId, ChargingStationComponent component, IdentityContext identityContext) {
        super(chargingStationId, identityContext);
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
     * Gets the component type.
     *
     * @return the component type.
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
        final ChangeComponentAvailabilityToOperativeCommand other = (ChangeComponentAvailabilityToOperativeCommand) obj;
        return Objects.equals(this.componentId, other.componentId) && Objects.equals(this.component, other.component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("chargingStationId", getChargingStationId())
                .add("componentId", componentId)
                .add("component", component)
                .add("identityContext", getIdentityContext())
                .toString();
    }
}
