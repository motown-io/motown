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
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.RequestChangeChargingStationAvailabilityApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

class RequestChangeAvailabilityJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestChangeAvailability";

    private static final String AVAILABILITY_INOPERATIVE = "inoperative";

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

        if (chargingStation == null || !chargingStation.communicationAllowed()) {
            return;
        }

        RequestChangeChargingStationAvailabilityApiCommand command;
        try {
            command = gson.fromJson(commandObject, RequestChangeChargingStationAvailabilityApiCommand.class);
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Configure command not able to parse the payload, is your json correctly formatted?", ex);
        }

        ChargingStationId csId = new ChargingStationId(chargingStationId);
        UserIdentity userIdentity = identityContext.getUserIdentity();

        if (AVAILABILITY_INOPERATIVE.equalsIgnoreCase(command.getAvailability())) {
            if (command.getEvseId().getNumberedId() == 0) {
                checkAuthorization(csId, userIdentity, RequestChangeChargingStationAvailabilityToInoperativeCommand.class);
                commandGateway.send(new RequestChangeChargingStationAvailabilityToInoperativeCommand(new ChargingStationId(chargingStationId), identityContext), new CorrelationToken());
            } else {
                checkAuthorization(csId, userIdentity, RequestChangeComponentAvailabilityToInoperativeCommand.class);
                commandGateway.send(new RequestChangeComponentAvailabilityToInoperativeCommand(new ChargingStationId(chargingStationId), command.getEvseId(), ChargingStationComponent.EVSE, identityContext), new CorrelationToken());
            }
        } else {
            if (command.getEvseId().getNumberedId() == 0) {
                checkAuthorization(csId, userIdentity, RequestChangeChargingStationAvailabilityToOperativeCommand.class);
                commandGateway.send(new RequestChangeChargingStationAvailabilityToOperativeCommand(new ChargingStationId(chargingStationId), identityContext), new CorrelationToken());
            } else {
                checkAuthorization(csId, userIdentity, RequestChangeComponentAvailabilityToOperativeCommand.class);
                commandGateway.send(new RequestChangeComponentAvailabilityToOperativeCommand(new ChargingStationId(chargingStationId), command.getEvseId(), ChargingStationComponent.EVSE, identityContext), new CorrelationToken());
            }
        }
    }
}
