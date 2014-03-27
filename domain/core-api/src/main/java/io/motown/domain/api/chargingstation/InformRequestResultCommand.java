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

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code InformRequestResultCommand} serves as a base class for commands which will inform about the resulting status of a request
 * that has been sent to the charging station.
 */
public final class InformRequestResultCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final RequestResult status;

    private final String statusMessage;

    /**
     * Creates a {@code InformRequestResultCommand} with an identifier and new status.
     *
     * @param chargingStationId   the identifier of the charging station.
     * @param status              the resulting status of the request
     * @param statusMessage       status message, to primarily inform about the cause of a failure
     * @throws NullPointerException if {@code chargingStationId}, {@code status} or {@code statusMessage} is {@code null}.
     */
    public InformRequestResultCommand(ChargingStationId chargingStationId, RequestResult status, String statusMessage) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.status = checkNotNull(status);
        this.statusMessage = checkNotNull(statusMessage);
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
     * Gets the resulting status of the request.
     *
     * @return the request status
     */
    public RequestResult getStatus() {
        return status;
    }

    /**
     * Gets the resulting protocol specific status message.
     *
     * @return the message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chargingStationId, status, statusMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final InformRequestResultCommand other = (InformRequestResultCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.status, other.status) && Objects.equals(this.statusMessage, other.statusMessage);
    }
}
