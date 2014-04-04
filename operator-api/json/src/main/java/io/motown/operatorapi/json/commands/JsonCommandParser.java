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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.Evse;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.viewmodel.model.ConfigureChargingStationApiCommand;

import java.util.Map;
import java.util.Set;

/**
 * JSON calls that provide JSON that needs to be constructed into a Axon Command make use of this class.
 */
public final class JsonCommandParser {

    private JsonCommandParser() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    /**
     * Parse given JSON and give a ConfigureChargingStationCommand
     *
     * @param chargingStationId id of the charging station
     * @param payload           configuration json object (the inner structure of configuration
     * @param gson              gson parser in use
     * @return The command to be sent for configuration of a charging station.
     * @throws IllegalArgumentException when there is no valid configuration of at least evses or settings found
     */
    public static ConfigureChargingStationCommand parseConfigureChargingStation(ChargingStationId chargingStationId, JsonObject payload, Gson gson, IdentityContext identityContext) {
        ConfigureChargingStationApiCommand command;

        try {
            command = gson.fromJson(payload, ConfigureChargingStationApiCommand.class);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException(e);
        }

        Map<String, String> settings = command.getSettings();
        Set<Evse> evses = command.getEvses();

        ConfigureChargingStationCommand result;
        if (settings == null && evses != null) {
            result = new ConfigureChargingStationCommand(chargingStationId, evses, identityContext);
        } else if (settings != null && evses == null) {
            result = new ConfigureChargingStationCommand(chargingStationId, settings, identityContext);
        } else if (settings != null) {
            result = new ConfigureChargingStationCommand(chargingStationId, evses, settings, identityContext);
        } else {
            throw new IllegalArgumentException("Configure should at least have settings or evses");
        }

        return result;
    }

}
