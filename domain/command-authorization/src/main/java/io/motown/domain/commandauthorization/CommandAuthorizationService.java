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

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.security.AllPermissions;
import io.motown.domain.api.security.UserIdentity;
import io.motown.domain.commandauthorization.repositories.CommandAuthorizationRepository;

public class CommandAuthorizationService {

    private CommandAuthorizationRepository commandAuthorizationRepository;

    /**
     * Checks if a user identity has access to a command class for a certain charging station.
     *
     * @param chargingStationId    charging station identification.
     * @param userIdentity         user identity.
     * @param commandClass         command class.
     * @return true if the user is authorized to execute the command for the charging station, false if not.
     */
    public boolean isAuthorized(ChargingStationId chargingStationId, UserIdentity userIdentity, Class commandClass) {
        // first search for this specific authorization
        boolean isAuthorized = commandAuthorizationRepository.find(chargingStationId.getId(), userIdentity.getId(), commandClass) != null;

        if (!isAuthorized) {
            // maybe the user identity has access to 'allPermissions'
            isAuthorized = commandAuthorizationRepository.find(chargingStationId.getId(), userIdentity.getId(), AllPermissions.class) != null;
        }

        return isAuthorized;
    }

    /**
     * Sets the repository to use.
     *
     * @param commandAuthorizationRepository command authorization repository.
     */
    public void setCommandAuthorizationRepository(CommandAuthorizationRepository commandAuthorizationRepository) {
        this.commandAuthorizationRepository = commandAuthorizationRepository;
    }
}
