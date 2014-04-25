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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DataTransferResponseReceivedEvent} is the event which is published when a datatransfer response contains
 * data. This will most likely result in custom processing.
 */
public final class DataTransferResponseReceivedEvent {

    private final ChargingStationId chargingStationId;

    private String data;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DataTransferResponseReceivedEvent}.
     * @param chargingStationId the identifier of the charging station.
     * @param data              the free format data to send to the charging station.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol}, {@code vendorId} or
     *                              {@code identityContext} is {@code null}.
     */
    public DataTransferResponseReceivedEvent(ChargingStationId chargingStationId, String data, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.data = checkNotNull(data);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the free format data
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
}
