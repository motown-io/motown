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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code ChangeConfigurationEvent} is the event which is published when a configuration change has been requested.
 */
public final class ChangeConfigurationEvent implements CommunicationWithChargingStationRequestedEvent  {

    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final String key;

    private final String value;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DataTransferEvent} with an identifier, vendor identifier, message identifier, free format data and identity context.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol identifier.
     * @param key               the key to be changed.
     * @param value             the new value.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code key}, {@code value} or
     *                          {@code identityContext} is {@code null}.
     */
    public ChangeConfigurationEvent(ChargingStationId chargingStationId, String protocol, String key, String value, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.key = checkNotNull(key);
        this.value = checkNotNull(value);
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * @return the charging station identifier
     */
    @Override
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the protocol identifier
     */
    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * @return the key.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the new value for the key.
     */
    public String getValue() {
        return value;
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
