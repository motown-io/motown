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

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.AuthorizationListUpdateType;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.SendAuthorizationListCommand;
import io.motown.operatorapi.viewmodel.model.SendAuthorizationListApiCommand;

import java.util.List;

class SendAuthorizationListJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "SendAuthorizationList";

    private DomainCommandGateway commandGateway;

    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        try {
            List<IdentifyingToken> authorizationList = Lists.newArrayList();

            SendAuthorizationListApiCommand command = gson.fromJson(commandObject, SendAuthorizationListApiCommand.class);

            authorizationList.addAll(command.getItems());

            AuthorizationListUpdateType updateType = AuthorizationListUpdateType.valueOf(command.getUpdateType());

            // TODO other command handlers check if a charging station exists in the repository, why is that not done here? - Mark van den Bergh, Februari 26th 2014
            // TODO enable usage of hash in API - Dennis Laumen, January 13th 2014
            commandGateway.send(new SendAuthorizationListCommand(new ChargingStationId(chargingStationId), authorizationList, command.getListVersion(), "", updateType));
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("SendAuthorizationList command not able to parse the payload, is your json correctly formatted ?", ex);
        }
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
