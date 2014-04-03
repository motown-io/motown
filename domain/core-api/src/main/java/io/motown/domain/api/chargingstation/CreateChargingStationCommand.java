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
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code CreateChargingStationCommand} is the command which is published when a charging station should be created.
 */
public final class CreateChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code CreateChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param identityContext the identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code identityContext} is {@code null}.
     */
    public CreateChargingStationCommand(ChargingStationId chargingStationId, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
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
        return Objects.hash(chargingStationId, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final CreateChargingStationCommand other = (CreateChargingStationCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.identityContext, other.identityContext);
    }
}
