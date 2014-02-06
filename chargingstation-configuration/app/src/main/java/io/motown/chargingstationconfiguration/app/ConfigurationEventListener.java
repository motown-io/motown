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
package io.motown.chargingstationconfiguration.app;

import io.motown.chargingstationconfiguration.viewmodel.domain.DomainService;
import io.motown.domain.api.chargingstation.ConfigureChargingStationCommand;
import io.motown.domain.api.chargingstation.Evse;
import io.motown.domain.api.chargingstation.UnconfiguredChargingStationBootedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Component
public class ConfigurationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationEventListener.class);

    @Resource(name = "configurationCommandGateway")
    private ConfigurationCommandGateway commandGateway;

    @Autowired
    private DomainService domainService;

    @EventHandler
    protected void onEvent(UnconfiguredChargingStationBootedEvent event) {
        LOG.info("Handling UnconfiguredChargingStationBootedEvent");

        Map<String,String> attributes = event.getAttributes();
        Set<Evse> evses = domainService.getEvses(attributes.get("vendor"), attributes.get("model"));

        if (evses != null && evses.size() > 0) {
            ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(event.getChargingStationId(), evses);
            commandGateway.send(command);
        } else {
            LOG.info("No Evses found for vender {} and model {}", attributes.get("vendor"), attributes.get("model"));
        }
    }

    public void setCommandGateway(ConfigurationCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


}
