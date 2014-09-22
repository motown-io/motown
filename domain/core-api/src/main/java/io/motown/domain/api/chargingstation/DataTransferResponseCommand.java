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
 * {@code DataTransferResponseCommand} is the command for processing the optional data a charging station can return
 * upon a datatransfer towards the charging station.
 */
public final class DataTransferResponseCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private String data;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DataTransferResponseCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param data              the data to transfer (use an empty string to signify no value).
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code data}, or {@code identityContext} is
     *                              {@code null}.
     */
    public DataTransferResponseCommand(ChargingStationId chargingStationId, String data, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.data = checkNotNull(data);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the data.
     */
    public String getData() {
        return data;
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
        return Objects.hash(chargingStationId, data, identityContext);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DataTransferResponseCommand other = (DataTransferResponseCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.data, other.data) && Objects.equals(this.identityContext, other.identityContext);
    }
}
