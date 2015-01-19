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
import com.google.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestDataTransferCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.DataTransferApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

/**
 * Handles the DataTransfer JSON Command.
 */
class RequestDataTransferJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestDataTransfer";

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
        ChargingStationId csId = new ChargingStationId(chargingStationId);

        if (!commandAuthorizationService.isAuthorized(csId, identityContext.getUserIdentity(), RequestDataTransferCommand.class)) {
            throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), RequestDataTransferCommand.class);
        }

        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);

            if (chargingStation != null && chargingStation.communicationAllowed()) {
                DataTransferApiCommand command = gson.fromJson(commandObject, DataTransferApiCommand.class);

                commandGateway.send(new RequestDataTransferCommand(csId, command.getDataTransferMessage(), identityContext), new CorrelationToken());
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Data transfer command not able to parse the payload, is your json correctly formatted?", ex);
        }

    }
}
