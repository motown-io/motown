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
import io.motown.domain.api.chargingstation.RevokePermissionCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.SimpleUserIdentity;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.viewmodel.model.RevokePermissionApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

class RevokePermissionJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "RevokePermission";

    private DomainCommandGateway commandGateway;

    private ChargingStationRepository repository;

    private Gson gson;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) {
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.isAccepted()) {
                RevokePermissionApiCommand command = gson.fromJson(commandObject, RevokePermissionApiCommand.class);

                Class commandClass = Class.forName(command.getCommandClass());
                UserIdentity userIdentity = new SimpleUserIdentity(command.getUserIdentity());

                commandGateway.send(new RevokePermissionCommand(new ChargingStationId(chargingStationId), userIdentity, commandClass, identityContext));
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Revoke permission command not able to parse the payload, is your json correctly formatted?", ex);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Revoke permission command cannot instantiate class object for command class, is the passed command class value a valid class?", e);
        }
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

}
