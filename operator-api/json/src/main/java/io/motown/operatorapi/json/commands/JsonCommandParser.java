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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.Connector;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * JSON calls that provide JSON that needs to be constructed into a Axon Command make use of this class.
 */
public class JsonCommandParser {


    /**
     * Parse given JSON and give a ConfigureChargingStationCommand
     *
     * @param chargingStationId  id of the charging station
     * @param payload configuration json object (the inner structure of configuration
     * @param gson gson parser in use
     * @return The command to be sent for configuration of a charging station.
     * @throws IllegalArgumentException when there is no valid configuration of at least connectors or settings found
     */
    public static ConfigureChargingStationCommand parseConfigureChargingStation(ChargingStationId chargingStationId, JsonObject payload, Gson gson) {
        JsonArray jsArrayOfConnectors = payload.getAsJsonArray("connectors");
        Type typeOfConnectorList = new TypeToken<Set<Connector>>() { }.getType();
        Set<Connector> connectors = gson.fromJson(jsArrayOfConnectors, typeOfConnectorList);

        // GSON conversion of a { key:value } json map to a map of string,string of configuration items
        JsonObject settingsArray = payload.getAsJsonObject("settings");
        Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
        Map<String, String> settings = gson.fromJson(settingsArray, typeOfHashMap);

        if (settings == null && connectors != null) {
            return new ConfigureChargingStationCommand(chargingStationId, connectors);
        } else if (settings != null && connectors == null) {
            return new ConfigureChargingStationCommand(chargingStationId, settings);
        } else if (settings != null && connectors != null) {
            return new ConfigureChargingStationCommand(chargingStationId, connectors, settings);
        } else {
            throw new IllegalArgumentException("Configure should at least have settings or connectors");
        }
    }

}
