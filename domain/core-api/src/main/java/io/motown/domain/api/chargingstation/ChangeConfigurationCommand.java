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
 * {@code ChangeConfigurationCommand} is the command which is published when a single configuration parameter is to
 * be changed on the charging station.
 */
public final class ChangeConfigurationCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private String key;

    private String value;

    /**
     * Creates a {@code ChangeConfigurationCommand} with an identifier.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param key               the key to change.
     * @param value             the new value.
     * @throws NullPointerException if {@code key} or {@code value} is {@code null}.
     */
    public ChangeConfigurationCommand(ChargingStationId chargingStationId, String key, String value) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.key = checkNotNull(key);
        this.value = checkNotNull(value);
    }

    /**
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return the configuration key to change.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the new configuration value.
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangeConfigurationCommand that = (ChangeConfigurationCommand) o;

        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (!key.equals(that.key)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
