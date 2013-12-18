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
import io.motown.ocpp.viewmodel.domain.TransactionIdentifierTranslator;
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
        chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), TransactionIdentifierTranslator.toInt(event.getTransactionId()));
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

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
    }
}
