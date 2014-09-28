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
import io.motown.domain.commandauthorization.CommandAuthorizationService;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

import java.util.Set;

class AcceptChargingStationJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "AcceptChargingStation";

    /**
     * Set of user identities which shall be used in the {@code CreateChargingStationCommand} to indicate those users
     * are authorized to execute all commands on the created aggregate.
     */
    private Set<UserIdentity> userIdentitiesWithAllPermissions;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) throws UserIdentityUnauthorizedException {
        ChargingStation chargingStation = repository.findOne(chargingStationId);
        ChargingStationId csId = new ChargingStationId(chargingStationId);

        if (chargingStation == null) {
            // charging station doesn't exist yet, check if the user identity is allowed to create/maintain charging stations
            if (!userIdentitiesWithAllPermissions.contains(identityContext.getUserIdentity())) {
                throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), CreateAndAcceptChargingStationCommand.class);
            }

            commandGateway.send(new CreateAndAcceptChargingStationCommand(csId, userIdentitiesWithAllPermissions, identityContext));
        } else if (!chargingStation.isAccepted()) {
            if (!commandAuthorizationService.isAuthorized(csId, identityContext.getUserIdentity(), AcceptChargingStationCommand.class)) {
                throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), AcceptChargingStationCommand.class);
            }

            commandGateway.send(new AcceptChargingStationCommand(csId, identityContext));
        } else {
            throw new IllegalStateException(String.format("Charging station { %s } is already in accepted state, you can't register this station", chargingStationId));
        }
    }

    /**
     * Set of user identities which shall be used in the {@code CreateChargingStationCommand} to indicate those users
     * are authorized to execute all commands on the created aggregate.
     *
     * @param userIdentitiesWithAllPermissions set of user identities.
     */
    public void setUserIdentitiesWithAllPermissions(Set<UserIdentity> userIdentitiesWithAllPermissions) {
        this.userIdentitiesWithAllPermissions = userIdentitiesWithAllPermissions;
    }
}
