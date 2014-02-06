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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDomainCommandGateway implements DomainCommandGateway {

    private static final Logger LOG = LoggerFactory.getLogger(TestDomainCommandGateway.class);

    @Override
    public void send(RequestUnlockEvseCommand command) {
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
    public void send(RequestStartTransactionCommand command){
        LOG.debug("RequestStartTransactionCommand:" + command.toString());
    }

    @Override
    public void send(RequestStopTransactionCommand command){
        LOG.debug("RequestStopTransactionCommand:" + command.toString());
    }

    @Override
    public void send(RequestSoftResetChargingStationCommand command) {
        LOG.debug("RequestSoftResetChargingStationCommand: " + command.toString());
    }

    @Override
    public void send(RequestHardResetChargingStationCommand command) {
        LOG.debug("RequestHardResetChargingStationCommand: " + command.toString());
    }

    @Override
    public void send(RequestChangeChargingStationAvailabilityToInoperativeCommand command) {
        LOG.debug("RequestChangeChargingStationAvailabilityToInoperativeCommand: " + command.toString());
    }

    @Override
    public void send(RequestChangeChargingStationAvailabilityToOperativeCommand command) {
        LOG.debug("RequestChangeChargingStationAvailabilityToOperativeCommand: " + command.toString());
    }

    @Override
    public void send(DataTransferCommand command) {
        LOG.debug("DataTransferCommand: " + command.toString());
    }

    @Override
    public void send(ChangeConfigurationCommand command) {
        LOG.debug("ChangeConfigurationCommand: " + command.toString());
    }

    @Override
    public void send(RequestDiagnosticsCommand command) {
        LOG.debug("RequestDiagnosticsCommand: " + command.toString());
    }

    @Override
    public void send(RequestClearCacheCommand command) {
        LOG.debug("ClearCacheCommand: " + command.toString());
    }

    @Override
    public void send(RequestFirmwareUpdateCommand command) {
        LOG.debug("RequestFirmwareUpdateCommand: " + command.toString());
    }

    @Override
    public void send(RequestAuthorizationListVersionCommand command) {
        LOG.debug("RequestAuthorizationListVersionCommand: " + command.toString());
    }

    @Override
    public void send(SendAuthorizationListCommand command) {
        LOG.debug("SendAuthorizationListCommand: " + command.toString());
    }

    @Override
    public void send(RequestReserveNowCommand command) {
        LOG.debug("RequestReserveNowCommand: " + command.toString());
    }

}
