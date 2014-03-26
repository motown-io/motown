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
package io.motown.domain.api.chargingstation.identity;

/**
 * Defines access granted to all commands. This class should not be instantiated, it should be used as a command class
 * defined in the aggregate root authorizations map.
 *
 * Examples of commands which require permission to be executed are AcceptChargingStationCommand and
 * MakeChargingStationReservableCommand.
 *
 * {@code SimpleCommandAuthorization} uses this class to check if a {@code UserIdentity} is authorized for all commands.
 */
public final class AllPermissions {

    /**
     * Private constructor to prevent instantiation.
     */
    private AllPermissions() {
    }

}
