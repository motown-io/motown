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
import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.NumberedTransactionId;
import io.motown.domain.api.chargingstation.RequestStopTransactionCommand;
import io.motown.domain.api.chargingstation.TransactionId;
import io.motown.operatorapi.viewmodel.model.RequestStopTransactionApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
class RequestStopTransactionJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestStopTransaction";

    private DomainCommandGateway commandGateway;

    private ChargingStationRepository repository;

    private Gson gson;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        try {
            RequestStopTransactionApiCommand command = gson.fromJson(commandObject, RequestStopTransactionApiCommand.class);

            ChargingStation chargingStation = repository.findOne(chargingStationId);
            ChargingStationId chargingStationIdObject = new ChargingStationId(chargingStationId);
            // TODO assuming a NumberedTransactionId, needs to be fixed - Dennis Laumen, December 20th 2013
            TransactionId transactionId = new NumberedTransactionId(chargingStationIdObject, chargingStation.getProtocol(), Integer.parseInt(command.getId()));
            commandGateway.send(new RequestStopTransactionCommand(chargingStationIdObject, transactionId));
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Configure command not able to parse the payload, is your json correctly formatted ?");
        }
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}
