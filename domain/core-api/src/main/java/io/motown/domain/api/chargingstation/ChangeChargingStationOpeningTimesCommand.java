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

public abstract class ChangeChargingStationOpeningTimesCommand {
    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;
    private final Set<OpeningTime> openingTimes;

    protected ChangeChargingStationOpeningTimesCommand(ChargingStationId chargingStationId, Set<OpeningTime> openingTimes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.openingTimes = checkNotNull(openingTimes);
    }

    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

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
