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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ClearCacheRequestedEvent} is the event which is published when a request has been made clear the authorisation
 * cache on the charging station.
 */
public final class ClearCacheRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    /**
     * Creates a {@code ClearCacheRequestedEvent} with an identifier and a protocol.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          protocol identifier.
     * @throws NullPointerException if {@code chargingStationId} or {@code protocol} is {@code null}.
     */
    public ClearCacheRequestedEvent(ChargingStationId chargingStationId, String protocol) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.protocol = checkNotNull(protocol);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * Gets the protocol identifier.
     *
     * @return the protocol identifier.
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

}
