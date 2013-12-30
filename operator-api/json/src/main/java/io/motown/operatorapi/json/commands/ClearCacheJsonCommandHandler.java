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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.RequestClearCacheCommand;
import io.motown.domain.api.chargingstation.RequestDiagnosticsCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
class ClearCacheJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "ClearCache";

    private DomainCommandGateway commandGateway;

    private Gson gson;
    private ChargingStationRepository repository;

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

        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.isAccepted()) {
                commandGateway.send(new RequestClearCacheCommand(new ChargingStationId(chargingStationId)));
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Data transfer command not able to parse the payload, is your json correctly formatted?");
        }

    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}
