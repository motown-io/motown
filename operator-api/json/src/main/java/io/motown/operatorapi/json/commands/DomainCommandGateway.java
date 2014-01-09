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
package io.motown.operatorapi.json.commands;

import io.motown.domain.api.chargingstation.*;

interface DomainCommandGateway {
    void send(RequestUnlockConnectorCommand command);
    void send(ConfigureChargingStationCommand command);
    void send(CreateAndAcceptChargingStationCommand command);
    void send(AcceptChargingStationCommand command);
    void send(RequestStartTransactionCommand command);
    void send(RequestStopTransactionCommand command);
    void send(RequestSoftResetChargingStationCommand command);
    void send(RequestHardResetChargingStationCommand command);
    void send(RequestChangeChargingStationAvailabilityToInoperativeCommand command);
    void send(RequestChangeChargingStationAvailabilityToOperativeCommand command);
    void send(DataTransferCommand command);
    void send(ChangeConfigurationCommand command);
    void send(RequestDiagnosticsCommand command);
    void send(RequestClearCacheCommand command);
    void send(RequestFirmwareUpdateCommand command);
    void send(RequestAuthorisationListVersionCommand command);
    void send(SendAuthorisationListCommand command);
    void send(RequestReserveNowCommand command);
}
