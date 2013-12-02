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
package io.motown.ocpp.viewmodel.domain;

import com.google.common.collect.Maps;
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.persistence.entities.ChargingStation;
import io.motown.ocpp.viewmodel.persistence.repostories.ChargingStationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainService.class);

    @Resource(name = "domainCommandGateway")
    private DomainCommandGateway commandGateway;

    @Autowired
    private ChargingStationRepository repository;

    public BootChargingStationResult bootChargingStation(ChargingStationId chargingStationId, String chargingStationAddress, String vendor, String model) {

        // Check if we know the charging station, in order to determine if it is registered or not
        ChargingStation chargingStation = repository.findOne(chargingStationId.getId());
        if(chargingStation == null) {
            chargingStation = new ChargingStation(chargingStationId.getId());
        }

        // Keep track of the address on which we can reach the charging station
        chargingStation.setIpAddress(chargingStationAddress);
        repository.save(chargingStation);

        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("vendor", vendor);
        attributes.put("model", model);
        attributes.put("address", chargingStationAddress);

        commandGateway.send(new BootChargingStationCommand(chargingStationId, attributes));

        // TODO: Where should the heartbeat-interval (60) come from? - Mark van den Bergh, November 15th 2013
        return new BootChargingStationResult(chargingStation.isRegistered(), 60, new Date());
    }

    public AuthorizationResult authorize(ChargingStationId chargingStationId, String idTag){
        AuthorizeCommand command = new AuthorizeCommand(chargingStationId, idTag);
        AuthorizationResultStatus resultStatus = commandGateway.sendAndWait(command, 60, TimeUnit.SECONDS);

        AuthorizationResult result = new AuthorizationResult(idTag, resultStatus);
        return result;
    }
    
    public void configureChargingStation(ChargingStationId chargingStationId, Map<String, String> configurationItems) {
        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(chargingStationId, configurationItems);
        commandGateway.send(command);
    }

    public DomainCommandGateway getCommandGateway() {
        return commandGateway;
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public String retrieveChargingStationAddress(ChargingStationId id) {
        ChargingStation chargingStation = repository.findOne(id.getId());

        return chargingStation != null? chargingStation.getIpAddress() : "";
    }

}
