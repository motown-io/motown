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
package io.motown.domain.commandauthorization;

import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent;
import io.motown.domain.api.chargingstation.PermissionGrantedEvent;
import io.motown.domain.api.chargingstation.PermissionRevokedEvent;
import io.motown.domain.api.security.AllPermissions;
import io.motown.domain.api.security.UserIdentity;
import io.motown.domain.commandauthorization.repositories.CommandAuthorizationRepository;
import org.axonframework.eventhandling.annotation.EventHandler;

/**
 * Handles events related to command authorization.
 */
public class CommandAuthorizationEventHandler {

    private CommandAuthorizationRepository commandAuthorizationRepository;

    /**
     * Handles {@code ChargingStationCreatedEvent} which contains the initial authorizations. Initial authorizations
     * are updated in the command authorization repository.
     *
     * @param event contains information about command authorization.
     */
    @EventHandler
    public void handle(ChargingStationCreatedEvent event) {
        for (UserIdentity userIdentity : event.getUserIdentitiesWithAllPermissions()) {
            commandAuthorizationRepository.createOrUpdate(event.getChargingStationId().getId(), userIdentity.getId(), AllPermissions.class);
        }
    }

    /**
     * Handles {@code PermissionGrantedEvent} by calling repository to create or update the command authorization.
     *
     * @param event contains information about command authorization.
     */
    @EventHandler
    public void handle(PermissionGrantedEvent event) {
        commandAuthorizationRepository.createOrUpdate(event.getChargingStationId().getId(), event.getUserIdentity().getId(), event.getCommandClass());
    }

    /**
     * Handles {@code PermissionRevokedEvent} by calling repository to remove the command authorization.
     *
     * @param event contains information about command authorization.
     */
    @EventHandler
    public void handle(PermissionRevokedEvent event) {
        commandAuthorizationRepository.remove(event.getChargingStationId().getId(), event.getUserIdentity().getId(), event.getCommandClass());
    }

    /**
     * Sets the command authorization repository to use.
     *
     * @param commandAuthorizationRepository repository containing command authorization.
     */
    public void setCommandAuthorizationRepository(CommandAuthorizationRepository commandAuthorizationRepository) {
        this.commandAuthorizationRepository = commandAuthorizationRepository;
    }
}
