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
package io.motown.ocpp.viewmodel;

import io.motown.domain.api.chargingstation.BootChargingStationCommand;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.Connector;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

@Service
public class DomainService {
    @Resource(name = "domainCommandGateway")
    private DomainCommandGateway commandGateway;

    public Map<String, Serializable> bootChargingStation(ChargingStationId chargingStationId, String vendor, String model) {
        Connector connector = new Connector(1, "DEFAULT", 32);
        List<Connector> connectors = new ArrayList<Connector>();
        connectors.add(connector);

        BootChargingStationCommand command = new BootChargingStationCommand(chargingStationId, model, connectors);

        commandGateway.send(command);

        // TODO: this (results in Maps) is only placeholder code and should be removed as soon as we properly implement BootNotifications. - Dennis Laumen, November 13th 2013
        Map<String, Serializable> map = new LinkedHashMap<String, Serializable>(3);
        map.put("registrationStatus", "ACCEPTED");
        map.put("heartbeatInterval", 60);
        map.put("timestamp", new Date());
        return map;
    }

    public DomainCommandGateway getCommandGateway() {
        return commandGateway;
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
