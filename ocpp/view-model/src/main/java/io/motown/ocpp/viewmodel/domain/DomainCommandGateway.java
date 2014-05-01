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
package io.motown.ocpp.viewmodel.domain;

import io.motown.domain.api.chargingstation.*;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.common.annotation.MetaData;

import java.util.concurrent.TimeUnit;

interface DomainCommandGateway {

    void send(BootChargingStationCommand command);

    void send(HeartbeatCommand command);

    /**
     * Send a {@code ChangeConfigurationItemCommand}.
     *
     * @param command          the command to send.
     * @param correlationToken the correlation token.
     */
    void send(ChangeConfigurationItemCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ConfigureChargingStationCommand command);

    void send(StartTransactionCommand command);

    void send(StopTransactionCommand command);

    AuthorizationResultStatus sendAndWait(AuthorizeCommand command, long timeout, TimeUnit unit);

    void send(CreateChargingStationCommand command, CommandCallback<Object> callback);

    void send(ProcessMeterValueCommand command);

    void send(DiagnosticsFileNameReceivedCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(UpdateDiagnosticsUploadStatusCommand command);

    void send(UpdateFirmwareStatusCommand command);

    void send(AuthorizationListVersionReceivedCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ChangeAuthorizationListCommand changeChargingStationAvailabilityToOperativeCommand, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(IncomingDataTransferCommand command);

    void send(DataTransferResponseCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(StatusNotificationCommand command);

    void send(ReserveNowCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(CancelReservationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ChangeComponentAvailabilityToInoperativeCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ChangeChargingStationAvailabilityToInoperativeCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ChangeComponentAvailabilityToOperativeCommand changeComponentAvailabilityToOperativeCommand, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ChangeChargingStationAvailabilityToOperativeCommand changeChargingStationAvailabilityToOperativeCommand, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(ReceiveConfigurationItemsCommand command);

    void send(ClearCacheCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);

    void send(UnlockEvseCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken);
}

