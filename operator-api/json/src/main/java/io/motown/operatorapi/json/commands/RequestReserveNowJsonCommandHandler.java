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
import io.motown.domain.api.chargingstation.RequestReserveNowCommand;
import io.motown.domain.api.chargingstation.RequestStartTransactionCommand;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
class RequestReserveNowJsonCommandHandler implements JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestReserveNow";

    private DomainCommandGateway commandGateway;

    private ChargingStationRepository repository;

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void handle(String chargingStationId, JsonObject commandObject) {
        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.isAccepted()) {
                int connectorId = commandObject.get("connectorId").getAsInt();
                String token = commandObject.get("identifyingToken").getAsString();
                TextualToken identifyingToken = new TextualToken(token);

                String expiryDateString = commandObject.get("expiryDate").getAsString();
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date expiryDate = formatter.parseDateTime(expiryDateString).toDate();

                commandGateway.send(new RequestReserveNowCommand(new ChargingStationId(chargingStationId), connectorId, identifyingToken, expiryDate, null));
            } else {
                throw new IllegalStateException("It is not possible to request a reservation on a charging station that is not registered");
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Reserve now command is not able to parse the payload, is your json correctly formatted?");
        }
    }

    @Resource(name = "domainCommandGateway")
    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Autowired
    public void setRepository(ChargingStationRepository repository) {
        this.repository = repository;
    }
}
