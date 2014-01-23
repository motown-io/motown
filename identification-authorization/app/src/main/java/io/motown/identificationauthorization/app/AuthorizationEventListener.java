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
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.common.annotation.MetaData;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@Component
public class AuthorizationEventListener {

    @Autowired
    private IdentificationAuthorizationService service;

    @Resource(name = "authorizationCommandGateway")
    private AuthorizationCommandGateway commandGateway;

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
        boolean valid = service.isValid(event.getIdentifyingToken());

        CommandMessage commandMessage;
        if (valid) {
            commandMessage = asCommandMessage(new GrantAuthorizationCommand(event.getChargingStationId(), event.getIdentifyingToken()));
        } else {
            commandMessage = asCommandMessage(new DenyAuthorizationCommand(event.getChargingStationId(), event.getIdentifyingToken()));
        }

        if (correlationId != null && !correlationId.isEmpty()) {
            commandMessage = commandMessage.andMetaData(Collections.singletonMap("correlationId", correlationId));
        }

        commandGateway.send(commandMessage);
    }

    public void setService(IdentificationAuthorizationService service) {
        this.service = service;
    }

    public void setCommandGateway(AuthorizationCommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }
}
