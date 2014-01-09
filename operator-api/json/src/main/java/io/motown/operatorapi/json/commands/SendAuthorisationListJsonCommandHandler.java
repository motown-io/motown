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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
class SendAuthorisationListJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "SendAuthorisationList";

    private DomainCommandGateway commandGateway;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        try {
            List<IdentifyingToken> authorisationList = Lists.newArrayList();

            JsonArray items = commandObject.get("items").getAsJsonArray();
            for(JsonElement item:items) {
                String token = item.getAsJsonObject().get("token").getAsString();
                IdentifyingToken.AuthenticationStatus status = IdentifyingToken.AuthenticationStatus.valueOf(item.getAsJsonObject().get("status").getAsString());

                authorisationList.add(new TextualToken(token, status));
            }

            int listVersion = commandObject.get("listVersion").getAsInt();
            AuthorisationListUpdateType updateType = AuthorisationListUpdateType.valueOf(commandObject.get("updateType").getAsString());

            commandGateway.send(new SendAuthorisationListCommand(new ChargingStationId(chargingStationId), authorisationList, listVersion, null, updateType));
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("SendAuthorisationList command not able to parse the payload, is your json correctly formatted ?");
        }
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
