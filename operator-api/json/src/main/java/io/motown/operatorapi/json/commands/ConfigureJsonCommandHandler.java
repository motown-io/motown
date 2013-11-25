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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Map;

@Component
class ConfigureJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "Configure";

    private DomainCommandGateway commandGateway;

    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, String jsonCommand) {
        JsonArray command = gson.fromJson(jsonCommand, JsonArray.class);
        if (command != null && command.size() != 2) {
            throw new IllegalArgumentException("The given JSON command is not well formed");
        }
        if (!COMMAND_NAME.equals(command.get(0).getAsString())) {
            throw new IllegalArgumentException("The given JSON command is not supported by this command handler.");
        }

        // GSON conversion of a { key:value } json map to a map of string,string
        Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
        Map<String, String> configuration = gson.fromJson(command.get(1), typeOfHashMap);
        if (configuration == null) {
            throw new IllegalArgumentException("The given configuration for command " + COMMAND_NAME + " could not be parsed");
        }

        commandGateway.send(new ConfigureChargingStationCommand(new ChargingStationId(chargingStationId), configuration));
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
