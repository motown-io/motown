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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.AcceptChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.CreateAndAcceptChargingStationCommand;
import io.motown.domain.api.chargingstation.identity.IdentityContext;
import io.motown.domain.api.chargingstation.identity.SimpleUserIdentity;
import io.motown.domain.api.chargingstation.identity.TypeBasedAddOnIdentity;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

class RegisterJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "Register";

    private DomainCommandGateway commandGateway;

    private Gson gson;

    private ChargingStationRepository repository;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        //TODO this should probably be passed as a parameter
        IdentityContext identityContext = new IdentityContext(new TypeBasedAddOnIdentity("OPERATOR-API", "1"), new SimpleUserIdentity("root"));

        ChargingStation chargingStation = repository.findOne(chargingStationId);
        if (chargingStation == null) {
            commandGateway.send(new CreateAndAcceptChargingStationCommand(new ChargingStationId(chargingStationId)));
        } else if (!chargingStation.isAccepted()) {
            commandGateway.send(new AcceptChargingStationCommand(new ChargingStationId(chargingStationId), identityContext));
        } else {
            throw new IllegalStateException("Charging station { %s } is already in accepted state, you can't register this station".format(chargingStationId));
        }

        if (commandObject != null) {
            JsonElement jsConfiguration = commandObject.get("configuration");
            if (jsConfiguration != null && jsConfiguration.isJsonObject()) {
                ConfigureChargingStationCommand newCommand = JsonCommandParser.parseConfigureChargingStation(new ChargingStationId(chargingStationId), jsConfiguration.getAsJsonObject(), gson);
                commandGateway.send(newCommand);
            }
        }
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}
