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
import io.motown.domain.api.chargingstation.RequestStartTransactionCommand;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.RequestStartTransactionApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

class RequestStartTransactionJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestStartTransaction";

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

        if (!commandAuthorizationService.isAuthorized(csId, identityContext.getUserIdentity(), RequestStartTransactionCommand.class)) {
            throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), RequestStartTransactionCommand.class);
        }

        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.communicationAllowed()) {
                RequestStartTransactionApiCommand command = gson.fromJson(commandObject, RequestStartTransactionApiCommand.class);

                commandGateway.send(new RequestStartTransactionCommand(csId, command.getIdentifyingToken(), command.getEvseId(), identityContext), new CorrelationToken());
            } else {
                throw new IllegalStateException("It is not possible to request a start transaction on a charging station that is not registered");
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Start transaction command is not able to parse the payload, is your json correctly formatted?", ex);
        }
    }
}
