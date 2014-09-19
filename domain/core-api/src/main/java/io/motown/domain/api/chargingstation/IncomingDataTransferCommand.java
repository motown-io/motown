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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code IncomingDataTransferCommand} is the command which is published when the charging station sends a custom data
 * message to the central system.
 */
public final class IncomingDataTransferCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final DataTransferMessage dataTransferMessage;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code IncomingDataTransferCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param dataTransferMessage the data transfer message.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code dataTransferMessage}, or
     *                              {@code identityContext} is {@code null}.
     */
    public IncomingDataTransferCommand(ChargingStationId chargingStationId, DataTransferMessage dataTransferMessage, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.dataTransferMessage = checkNotNull(dataTransferMessage);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    public DataTransferMessage getDataTransferMessage() {
        return dataTransferMessage;
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
        return Objects.hash(chargingStationId, dataTransferMessage, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final IncomingDataTransferCommand other = (IncomingDataTransferCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.dataTransferMessage, other.dataTransferMessage) && Objects.equals(this.identityContext, other.identityContext);
    }
}
