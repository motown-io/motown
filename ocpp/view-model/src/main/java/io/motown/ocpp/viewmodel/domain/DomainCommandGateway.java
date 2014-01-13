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

import java.util.concurrent.TimeUnit;

interface DomainCommandGateway {

    void send(BootChargingStationCommand command);

    void send(HeartbeatCommand command);

    void send(ConfigureChargingStationCommand command);

    void send(StartTransactionCommand command);

    void send(StopTransactionCommand command);

    AuthorizationResultStatus sendAndWait(AuthorizeCommand command, long timeout, TimeUnit unit);

    void send(CreateChargingStationCommand command, CommandCallback<Object> callback);

    void send(ProcessMeterValueCommand command);

    void send(DiagnosticsFileNameReceivedCommand command);

    void send(UpdateDiagnosticsUploadStatusCommand command);

    void send(UpdateFirmwareStatusCommand command);

    void send(AuthorisationListVersionReceivedCommand command);

    void send(IncomingDataTransferCommand command);

    void send(ReservationStatusChangedCommand command);

    void send(StatusNotificationCommand command);

    void send(ClearCacheStatusChangedCommand command);
}
