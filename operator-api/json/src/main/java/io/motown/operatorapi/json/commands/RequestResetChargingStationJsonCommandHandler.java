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
import io.motown.domain.api.chargingstation.RequestHardResetChargingStationCommand;
import io.motown.domain.api.chargingstation.RequestSoftResetChargingStationCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.RequestResetChargingStationApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

class RequestResetChargingStationJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestResetChargingStation";

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
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.communicationAllowed()) {
                ChargingStationId csId = new ChargingStationId(chargingStationId);
                UserIdentity userIdentity = identityContext.getUserIdentity();

                RequestResetChargingStationApiCommand command = gson.fromJson(commandObject, RequestResetChargingStationApiCommand.class);

                if("hard".equalsIgnoreCase(command.getType())) {
                    checkAuthorization(csId, userIdentity, RequestHardResetChargingStationCommand.class);
                    commandGateway.send(new RequestHardResetChargingStationCommand(csId, identityContext), new CorrelationToken());
                } else {
                    checkAuthorization(csId, userIdentity, RequestSoftResetChargingStationCommand.class);
                    commandGateway.send(new RequestSoftResetChargingStationCommand(csId, identityContext), new CorrelationToken());
                }
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Configure command not able to parse the payload, is your json correctly formatted?", ex);
        }
    }
}
