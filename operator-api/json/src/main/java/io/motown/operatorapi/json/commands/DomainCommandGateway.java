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
import org.axonframework.common.annotation.MetaData;

interface DomainCommandGateway {
    void send(RequestUnlockEvseCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(ConfigureChargingStationCommand command);
    void send(CreateAndAcceptChargingStationCommand command);
    void send(AcceptChargingStationCommand command);
    void send(RequestStartTransactionCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestStopTransactionCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestSoftResetChargingStationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestHardResetChargingStationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestChangeChargingStationAvailabilityToInoperativeCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestChangeChargingStationAvailabilityToOperativeCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(DataTransferCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(ChangeConfigurationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestDiagnosticsCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestClearCacheCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestFirmwareUpdateCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestAuthorizationListVersionCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(SendAuthorizationListCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestReserveNowCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestCancelReservationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken);
    void send(RequestConfigurationCommand command);
    void send(PlaceChargingStationCommand command);
    void send(ImproveChargingStationLocationCommand command);
    void send(MoveChargingStationCommand command);
    void send(MakeChargingStationReservableCommand command);
    void send(MakeChargingStationNotReservableCommand command);
    void send(SetChargingStationOpeningTimesCommand command);
    void send(AddChargingStationOpeningTimesCommand command);
    void send(GrantPermissionCommand command);
    void send(RevokePermissionCommand command);
}
