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
import com.google.gson.JsonParseException;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.domain.api.security.UserIdentity;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;

import java.util.List;

public class JsonCommandService {

    private static final int COMMAND_ARRAY_SIZE = 2;

    private static final int COMMAND_NAME_INDEX = 0;

    private static final int COMMAND_PAYLOAD_INDEX = 1;

    private Gson gson;

    private List<JsonCommandHandler> jsonCommandHandlers;

    private static final String ADD_ON_TYPE = "OPERATOR-API";

    private String addOnId;

    public void handleCommand(String chargingStationId, String jsonCommand, UserIdentity userIdentity) throws UserIdentityUnauthorizedException {
        JsonArray commandAsArray = gson.fromJson(jsonCommand, JsonArray.class);

        if (commandAsArray.size() != COMMAND_ARRAY_SIZE) {
            throw new JsonParseException("API command must be a JSON array with two elements");
        }

        String commandName = commandAsArray.get(COMMAND_NAME_INDEX).getAsString();
        JsonObject commandPayloadAsObject = commandAsArray.get(COMMAND_PAYLOAD_INDEX).getAsJsonObject();

        JsonCommandHandler commandHandler = getCommandHandler(commandName);

        IdentityContext identityContext = new IdentityContext(new TypeBasedAddOnIdentity(ADD_ON_TYPE, addOnId), userIdentity);

        commandHandler.handle(chargingStationId, commandPayloadAsObject, identityContext);
    }

    private JsonCommandHandler getCommandHandler(String commandName) {
        for (JsonCommandHandler commandHandler : this.jsonCommandHandlers) {
            if (commandName.equals(commandHandler.getCommandName())) {
                return commandHandler;
            }
        }

        throw new JsonParseException(String.format("No command handler is configured for handling [%s].", commandName));
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setJsonCommandHandlers(List<JsonCommandHandler> jsonCommandHandlers) {
        this.jsonCommandHandlers = jsonCommandHandlers;
    }

    public void setAddOnId(String addOnId) {
        this.addOnId = addOnId;
    }
}
