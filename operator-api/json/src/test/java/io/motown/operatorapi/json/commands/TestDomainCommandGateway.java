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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDomainCommandGateway implements DomainCommandGateway {

    private static final Logger LOG = LoggerFactory.getLogger(TestDomainCommandGateway.class);

    @Override
    public void send(RequestUnlockEvseCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestUnlockEvseCommand:" + command.toString());
    }

    @Override
    public void send(ConfigureChargingStationCommand command) {
        LOG.debug("ConfigureChargingStationCommand:" + command.toString());
    }

    @Override
    public void send(CreateAndAcceptChargingStationCommand command) {
        LOG.debug("CreateChargingStationCommand:" + command.toString());
    }

    @Override
    public void send(AcceptChargingStationCommand command) {
        LOG.debug("AcceptChargingStationCommand:" + command.toString());
    }

    @Override
    public void send(RequestStartTransactionCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestStartTransactionCommand:" + command.toString());
    }

    @Override
    public void send(RequestStopTransactionCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestStopTransactionCommand:" + command.toString());
    }

    @Override
    public void send(RequestSoftResetChargingStationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestSoftResetChargingStationCommand: " + command.toString());
    }

    @Override
    public void send(RequestHardResetChargingStationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestHardResetChargingStationCommand: " + command.toString());
    }

    @Override
    public void send(RequestChangeChargingStationAvailabilityToInoperativeCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestChangeChargingStationAvailabilityToInoperativeCommand: " + command.toString());
    }

    @Override
    public void send(RequestChangeChargingStationAvailabilityToOperativeCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestChangeChargingStationAvailabilityToOperativeCommand: " + command.toString());
    }

    @Override
    public void send(DataTransferCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("DataTransferCommand: " + command.toString());
    }

    @Override
    public void send(ChangeConfigurationItemCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("ChangeConfigurationItemCommand: " + command.toString());
    }

    @Override
    public void send(RequestDiagnosticsCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestDiagnosticsCommand: " + command.toString());
    }

    @Override
    public void send(RequestClearCacheCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("ClearCacheCommand: " + command.toString());
    }

    @Override
    public void send(RequestFirmwareUpdateCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestFirmwareUpdateCommand: " + command.toString());
    }

    @Override
    public void send(RequestAuthorizationListVersionCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestAuthorizationListVersionCommand: " + command.toString());
    }

    @Override
    public void send(SendAuthorizationListCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("SendAuthorizationListCommand: " + command.toString());
    }

    @Override
    public void send(RequestReserveNowCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestReserveNowCommand: " + command.toString());
    }

    @Override
    public void send(RequestCancelReservationCommand command, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.debug("RequestCancelReservationCommand: " + command.toString());
    }

    @Override
    public void send(RequestConfigurationItemsCommand command) {
        LOG.debug("RequestConfigurationItemsCommand: " + command.toString());
    }

    @Override
    public void send(PlaceChargingStationCommand command) {
        LOG.debug("PlaceChargingStationCommand: " + command.toString());
    }

    @Override
    public void send(ImproveChargingStationLocationCommand command) {
        LOG.debug("ImproveChargingStationLocationCommand: " + command.toString());
    }

    @Override
    public void send(MoveChargingStationCommand command) {
        LOG.debug("MoveChargingStationCommand: " + command.toString());
    }

    @Override
    public void send(MakeChargingStationReservableCommand command) {
        LOG.debug("MakeChargingStationReservableCommand: " + command.toString());
    }

    @Override
    public void send(MakeChargingStationNotReservableCommand command) {
        LOG.debug("MakeChargingStationNotReservableCommand: " + command.toString());
    }

    @Override
    public void send(SetChargingStationOpeningTimesCommand command) {
        LOG.debug("SetChargingStationOpeningTimesCommand: " + command.toString());
    }

    @Override
    public void send(AddChargingStationOpeningTimesCommand command) {
        LOG.debug("AddChargingStationOpeningTimesCommand: " + command.toString());
    }

    @Override
    public void send(GrantPermissionCommand command) {
        LOG.debug("GrantPermissionCommand: " + command.toString());
    }

    @Override
    public void send(RevokePermissionCommand command) {
        LOG.debug("RevokePermissionCommand: " + command.toString());
    }

}
