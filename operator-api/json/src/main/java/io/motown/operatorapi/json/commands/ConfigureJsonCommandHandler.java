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

/**
 * Json command handler for the 'Configure' command.
 */
class ConfigureJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "Configure";

    private DomainCommandGateway commandGateway;

    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * Configure a charging stations in such a way that it able to operate in the network.
     *
     * @param chargingStationId unique identifier of the charging station
     * @param commandObject expects a string like:
     * "'configuration' : {
            'evses' : [{
                'evseId' : 1,
                'connectors' : [{
                    'maxAmp': 32,
                    'phase': 3,
                    'voltage': 230,
                    'chargingProtocol': 'MODE3',
                    'current': 'AC',
                    'connectorType': 'TESLA'
                }]
            }],
            'settings' : {
                'key':'value',
                'key2':'value2'
            }
        }"
     */
    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        try {
            ConfigureChargingStationCommand newCommand = JsonCommandParser.parseConfigureChargingStation(new ChargingStationId(chargingStationId), commandObject, gson);
            commandGateway.send(newCommand);
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Configure command not able to parse the payload, is your json correctly formatted ?", ex);
        }
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
