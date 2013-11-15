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

import io.motown.domain.api.chargingstation.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

@Service
public class DomainService {

    @Resource(name = "domainCommandGateway")
    private DomainCommandGateway commandGateway;

    public BootChargingStationResult bootChargingStation(ChargingStationId chargingStationId, String vendor, String model) {
        Connector connector = new Connector(1, "DEFAULT", 32);
        List<Connector> connectors = new ArrayList<Connector>();
        connectors.add(connector);

        // TODO: Use Guava's collections for a more fluent interface. - Dennis Laumen, November 14th 2013
        Map<String, String> attributes = new HashMap<>();
        attributes.put("vendor", vendor);
        attributes.put("model", model);

        BootChargingStationCommand command = new BootChargingStationCommand(chargingStationId, attributes);

        // TODO: Timeout should be configurable. - Mark van den Bergh, November 15th 2013
        long timeout = 2000;
        ChargingStationRegistrationStatus result = commandGateway.sendAndWait(command, timeout, TimeUnit.MILLISECONDS);

        // TODO: Where should the interval come from? - Mark van den Bergh, November 15th 2013
        return new BootChargingStationResult(ChargingStationRegistrationStatus.REGISTERED.equals(result), 60, new Date());
    }

    public DomainCommandGateway getCommandGateway() {
        return commandGateway;
    }

    public void setCommandGateway(DomainCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

}
