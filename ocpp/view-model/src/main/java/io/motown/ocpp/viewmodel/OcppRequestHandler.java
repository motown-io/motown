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
package io.motown.ocpp.viewmodel;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OcppRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(io.motown.ocpp.viewmodel.OcppRequestHandler.class);

    @Autowired
    private ChargingStationOcpp15Client chargingStationOcpp15Client;

    @EventHandler
    public void handle(ConfigurationRequestedEvent event) {
        log.info("Handling ConfigurationRequestedEvent");
        chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());
    }

    @EventHandler
    public void handle(StopTransactionRequestedEvent event) {
        log.info("StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());
        } else {
            log.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @EventHandler
    public void handle(SoftResetChargingStationRequestedEvent event) {
        log.info("SoftResetChargingStationRequestedEvent");
        chargingStationOcpp15Client.softReset(event.getChargingStationId());
    }

    @EventHandler
    public void handle(HardResetChargingStationRequestedEvent event) {
        log.info("HardResetChargingStationRequestedEvent");
        chargingStationOcpp15Client.hardReset(event.getChargingStationId());
    }

    @EventHandler
    public void handle(StartTransactionRequestedEvent event) {
        log.info("StartTransactionRequestedEvent");
        chargingStationOcpp15Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getConnectorId());
    }

    @EventHandler
    public void handle(UnlockConnectorRequestedEvent event) {
        log.info("UnlockConnectorRequestedEvent");
        chargingStationOcpp15Client.unlockConnector(event.getChargingStationId(), event.getConnectorId());
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event) {
        log.info("ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), event.getConnectorId());
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event) {
        log.info("ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), event.getConnectorId());
    }

    @EventHandler
    public void handle(DataTransferEvent event) {
        log.info("DataTransferEvent");
        chargingStationOcpp15Client.dataTransfer(event.getChargingStationId(), event.getVendorId(), event.getMessageId(), event.getData());
    }

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
    }
}
