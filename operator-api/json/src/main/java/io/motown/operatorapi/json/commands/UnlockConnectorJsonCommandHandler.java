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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.RequestUnlockConnectorCommand;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
class UnlockConnectorJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "UnlockConnector";

    private static final String CONNECTOR_ID_FIELD = "connectorId";

    private DomainCommandGateway commandGateway;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        int connectorId = commandObject.get(CONNECTOR_ID_FIELD).getAsInt();

        commandGateway.send(new RequestUnlockConnectorCommand(new ChargingStationId(chargingStationId), connectorId));
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
