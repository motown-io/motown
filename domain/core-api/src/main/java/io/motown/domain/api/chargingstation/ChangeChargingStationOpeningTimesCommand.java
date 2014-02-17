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

import com.google.common.base.Objects;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic class for all commands that change the opening times.
 */
public abstract class ChangeChargingStationOpeningTimesCommand {
    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final Set<OpeningTime> openingTimes;

    /**
     * Creates a command object that changes the opening times of a charging station.
     *
     * @param chargingStationId The identifier of the charging station.
     * @param openingTimes The opening times of the charging station.
     * @throws java.lang.NullPointerException if either {@code chargingStationId} or {@code openingTimes} is {@code null}.
     */
    protected ChangeChargingStationOpeningTimesCommand(ChargingStationId chargingStationId, Set<OpeningTime> openingTimes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.openingTimes = checkNotNull(openingTimes);
    }

    /**
     * Gets the charging station identifier.
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the opening times of the charging station.
     * @return the opening times.
     */
    public Set<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chargingStationId, openingTimes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChangeChargingStationOpeningTimesCommand other = (ChangeChargingStationOpeningTimesCommand) obj;
        return Objects.equal(this.chargingStationId, other.chargingStationId) && Objects.equal(this.openingTimes, other.openingTimes);
    }
}
