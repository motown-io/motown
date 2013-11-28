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
package io.motown.ocpp.viewmodel.configuration;

import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.UnconfiguredChargingStationBootedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class ConfigurationHandler {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationHandler.class);

    @Resource(name = "configurationCommandGateway")
    private ConfigurationCommandGateway commandGateway;

    @EventHandler
    public void handle(UnconfiguredChargingStationBootedEvent event) {
        log.info("Handling UnconfiguredChargingStationBootedEvent");

        Map<String,String> attributes = event.getAttributes();
        int numberOfConnectors = getNumberOfConnectors(attributes.get("vendor"), attributes.get("model"));

        ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(event.getChargingStationId(), numberOfConnectors);
        commandGateway.send(command);
    }

    public int getNumberOfConnectors(String vendor, String model) {
        // TODO implement
        return 2;
    }

}
