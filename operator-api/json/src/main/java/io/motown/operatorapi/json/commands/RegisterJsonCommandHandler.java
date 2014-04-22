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

import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.AcceptChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CreateAndAcceptChargingStationCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

import java.util.Set;

class RegisterJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "Register";

    private DomainCommandGateway commandGateway;

    private ChargingStationRepository repository;

    /**
     * Set of user identities which shall be used in the {@code CreateChargingStationCommand} to indicate those users
     * are authorized to execute all commands on the created aggregate.
     */
    private Set<UserIdentity> userIdentitiesWithAllPermissions;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) {
        ChargingStation chargingStation = repository.findOne(chargingStationId);
        if (chargingStation == null) {
            commandGateway.send(new CreateAndAcceptChargingStationCommand(new ChargingStationId(chargingStationId), userIdentitiesWithAllPermissions, identityContext));
        } else if (!chargingStation.isAccepted()) {
            commandGateway.send(new AcceptChargingStationCommand(new ChargingStationId(chargingStationId), identityContext));
        } else {
            throw new IllegalStateException("Charging station { %s } is already in accepted state, you can't register this station".format(chargingStationId));
        }
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }

    public void setUserIdentitiesWithAllPermissions(Set<UserIdentity> userIdentitiesWithAllPermissions) {
        this.userIdentitiesWithAllPermissions = userIdentitiesWithAllPermissions;
    }
}
