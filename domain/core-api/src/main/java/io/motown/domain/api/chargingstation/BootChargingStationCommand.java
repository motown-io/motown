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

import com.google.common.collect.ImmutableMap;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code BootChargingStationCommand} is the command which should be sent when a charging station boots.
 */
public class BootChargingStationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final Map<String, String> attributes;

    /**
     * Creates a {@code BootChargingStationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @throws NullPointerException if {@code chargingStationId} is {@code null}.
     */
    public BootChargingStationCommand(ChargingStationId chargingStationId) {
        checkArgument(chargingStationId != null);

        this.chargingStationId = chargingStationId;
        this.attributes = ImmutableMap.of();
    }

    /**
     * Creates a {@code BootChargingStationCommand} with an identifier and a {@link java.util.Map} of attributes.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information provided by
     *                          the charging station when it booted but which are not required by Motown. Because
     *                          {@link java.util.Map} implementations are potentially mutable a defensive copy is made.
     * @throws NullPointerException if {@code chargingStationId} or {@code attributes} is {@code null}.
     */
    public BootChargingStationCommand(ChargingStationId chargingStationId, Map<String, String> attributes) {
        checkArgument(chargingStationId != null);
        checkArgument(attributes != null);

        this.chargingStationId = chargingStationId;
        this.attributes = ImmutableMap.copyOf(attributes);
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
     * Gets the attributes associated with the boot.
     *
     * These attributes are additional information provided by the charging station when it booted but which are not
     * required by Motown.
     *
     * @return an immutable {@link java.util.Map} of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
}
