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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.NullUserIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class ConfigurationEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationEventListener.class);

    private ConfigurationCommandGateway commandGateway;

    private DomainService domainService;

    public static final String ADD_ON_TYPE = "CHARGINGSTATION-CONFIGURATION";

    private AddOnIdentity addOnIdentity;

    /**
     * Handles {@code UnconfiguredChargingStationBootedEvent}s by requesting the domainService for information about
     * the vendor and model code which should be present in the event attributes.
     *
     * @param event
     */
    @EventHandler
    protected void onEvent(UnconfiguredChargingStationBootedEvent event) {
        LOG.info("Handling UnconfiguredChargingStationBootedEvent");

        Map<String,String> attributes = event.getAttributes();
        Set<Evse> evses = domainService.getEvses(attributes.get("vendor"), attributes.get("model"));

        if (evses != null && !evses.isEmpty()) {
            IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());

            ConfigureChargingStationCommand command = new ConfigureChargingStationCommand(event.getChargingStationId(), evses, identityContext);
            commandGateway.send(command);
        } else {
            LOG.info("No Evses found for vender {} and model {}", attributes.get("vendor"), attributes.get("model"));
        }
    }

    /**
     * Sets the configuration gateway to use when sending out commands.
     *
     * @param commandGateway the command gateway.
     */
    public void setCommandGateway(ConfigurationCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    /**
     * Sets the domain service to use.
     *
     * @param domainService the domain service.
     */
    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * Sets the add-on id. The add-on is hardcoded, the add-on id should be different for every instance (in a distributed configuration)
     * to be able to differentiate between add-on instances.
     *
     * @param id add-on id.
     */
    public void setAddOnId(String id) {
        addOnIdentity = new TypeBasedAddOnIdentity(ADD_ON_TYPE, id);
    }

}
