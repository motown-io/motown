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
package io.motown.identificationauthorization.app;

import io.motown.domain.api.chargingstation.AuthorizationRequestedEvent;
import io.motown.domain.api.chargingstation.DenyAuthorizationCommand;
import io.motown.domain.api.chargingstation.GrantAuthorizationCommand;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.IdentityContext;
import io.motown.domain.api.security.NullUserIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.common.annotation.MetaData;
import org.axonframework.eventhandling.annotation.EventHandler;

import java.util.Collections;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

public class AuthorizationEventListener {

    private IdentificationAuthorizationService identificationAuthorizationService;

    private AuthorizationCommandGateway commandGateway;

    private static final String ADD_ON_TYPE = "IDENTIFICATION-AUTHORIZATION";

    private AddOnIdentity addOnIdentity;

    /**
     * Listens for {@code AuthorizationRequestedEvent} and requests the {@code IdentificationAuthorizationService} to
     * execute the authorization. Sends a {@code GrantAuthorizationCommand} if identification is successful,
     * {@code DenyAuthorizationCommand} if not. The passed correlation id will be added to the outgoing command if
     * it's not null or empty.
     *
     * @param event the authorization request event.
     * @param correlationId correlation id which will be added to outgoing command if it's not null or empty.
     */
    @EventHandler
    protected void onEvent(AuthorizationRequestedEvent event,
                           @MetaData(value = "correlationId", required = false) String correlationId) {
        boolean valid = identificationAuthorizationService.isValid(event.getIdentifyingToken());

        CommandMessage commandMessage;
        IdentityContext identityContext = new IdentityContext(addOnIdentity, new NullUserIdentity());
        if (valid) {
            commandMessage = asCommandMessage(new GrantAuthorizationCommand(event.getChargingStationId(), event.getIdentifyingToken(), identityContext));
        } else {
            commandMessage = asCommandMessage(new DenyAuthorizationCommand(event.getChargingStationId(), event.getIdentifyingToken(), identityContext));
        }

        if (correlationId != null && !correlationId.isEmpty()) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap("correlationId", correlationId));
        }

        commandGateway.send(commandMessage);
    }

    public void setIdentificationAuthorizationService(IdentificationAuthorizationService identificationAuthorizationService) {
        this.identificationAuthorizationService = identificationAuthorizationService;
    }

    public void setCommandGateway(AuthorizationCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    public void setAddOnIdentity(String addOnIdentity) {
        this.addOnIdentity = new TypeBasedAddOnIdentity(ADD_ON_TYPE, addOnIdentity);
    }
}
