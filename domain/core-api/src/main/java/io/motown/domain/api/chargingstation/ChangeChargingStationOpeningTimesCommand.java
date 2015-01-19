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

import com.google.common.collect.ImmutableSet;
import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic class for all commands that change the opening times.
 */
public abstract class ChangeChargingStationOpeningTimesCommand {
    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final Set<OpeningTime> openingTimes;
    private final IdentityContext identityContext;

    /**
     * Creates a command object that changes the opening times of a charging station.
     *
     * @param chargingStationId The identifier of the charging station.
     * @param openingTimes      The opening times of the charging station.
     * @param identityContext   the identity context.
     * @throws java.lang.NullPointerException if either {@code chargingStationId}, {@code openingTimes} or {@code identityContext} is {@code null}.
     */
    protected ChangeChargingStationOpeningTimesCommand(ChargingStationId chargingStationId, Set<OpeningTime> openingTimes, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.openingTimes = ImmutableSet.copyOf(checkNotNull(openingTimes));
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
     * Gets the opening times of the charging station.
     *
     * @return the opening times.
     */
    public Set<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, openingTimes, identityContext);
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
        final ChangeChargingStationOpeningTimesCommand other = (ChangeChargingStationOpeningTimesCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.openingTimes, other.openingTimes) && Objects.equals(this.identityContext, other.identityContext);
    }
}
