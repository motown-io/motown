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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code BootChargingStationCommand} is the command which is published when a charging station has booted.
 */
public class AuthorizeCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String idTag;

    /**
     * Creates a {@code AuthorizeCommand}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param idTag the identifier that needs to be authorized.
     * @throws NullPointerException if {@code chargingStationId} or {@code idTag} is {@code null}.
     */
    public AuthorizeCommand(ChargingStationId chargingStationId, String idTag) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.idTag = checkNotNull(idTag);
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
     * Gets the identifier that needs to be authorized.
     *
     * @return the identifier that needs to be authorized.
     */
    public String getIdTag() {
        return idTag;
    }
}
