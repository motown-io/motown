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

import com.google.common.collect.ImmutableList;
import io.motown.domain.api.security.IdentityContext;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code RequestConfigurationItemsCommand} is the command which is published when a charging station's configuration should
 * be requested from the charging station.
 */
public final class RequestConfigurationItemsCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final List<String> keys;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code RequestConfigurationItemsCommand} with an identifier and identity context.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param keys              an optional list of keys to retrieve, or all keys in case this list is empty
     * @param identityContext   the identity context.
     * @throws NullPointerException if {@code chargingStationId} or {@code identityContext} is {@code null}.
     */
    public RequestConfigurationItemsCommand(ChargingStationId chargingStationId, List<String> keys, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.keys = ImmutableList.copyOf(checkNotNull(keys));
        this.identityContext = checkNotNull(identityContext);
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
     * The optional list of keys to be retrieved. The retrieved list is immutable.
     *
     * @return optional list of keys
     */
    public List<String> getKeys() {
        return keys;
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
        return Objects.hash(chargingStationId, keys, identityContext);
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
        final RequestConfigurationItemsCommand other = (RequestConfigurationItemsCommand) obj;
        return Objects.equals(this.chargingStationId, other.chargingStationId) && Objects.equals(this.keys, other.keys) && Objects.equals(this.identityContext, other.identityContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringHelper(this)
                .add("chargingStationId", chargingStationId)
                .add("keys", keys)
                .add("identityContext", identityContext)
                .toString();
    }
}
