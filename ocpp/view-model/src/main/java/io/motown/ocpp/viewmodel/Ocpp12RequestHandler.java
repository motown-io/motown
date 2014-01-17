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
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp12Client;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class Ocpp12RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(Ocpp12RequestHandler.class);

    @Autowired
    public DomainService domainService;

    @Autowired
    private ChargingStationOcpp12Client chargingStationOcpp12Client;

    @EventHandler
    public void handle(StopTransactionRequestedEvent event) {
        log.info("OCPP 1.2 StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            RequestStatus requestStatus = chargingStationOcpp12Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());

            domainService.stopTransactionStatusChanged(event.getChargingStationId(), requestStatus);
        } else {
            log.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @EventHandler
    public void handle(SoftResetChargingStationRequestedEvent event) {
        log.info("OCPP 1.2 SoftResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.softReset(event.getChargingStationId());

        domainService.softResetStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(HardResetChargingStationRequestedEvent event) {
        log.info("OCPP 1.2 HardResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.hardReset(event.getChargingStationId());

        domainService.hardResetStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(StartTransactionRequestedEvent event) {
        log.info("OCPP 1.2 StartTransactionRequestedEvent");
        RequestStatus requestStatus =  chargingStationOcpp12Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getConnectorId());

        domainService.startTransactionStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(UnlockConnectorRequestedEvent event) {
        log.info("OCPP 1.2 UnlockConnectorRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.unlockConnector(event.getChargingStationId(), event.getConnectorId());

        domainService.unlockConnectorStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event) {
        log.info("OCPP 1.2 ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.changeAvailabilityToInoperative(event.getChargingStationId(), event.getConnectorId());

        domainService.changeAvailabilityToInoperativeStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event) {
        log.info("OCPP 1.2 ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.changeAvailabilityToOperative(event.getChargingStationId(), event.getConnectorId());

        domainService.changeAvailabilityToOperativeStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ChangeConfigurationEvent event) {
        log.info("OCPP 1.2 ChangeConfigurationEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.changeConfiguration(event.getChargingStationId(), event.getKey(), event.getValue());

        domainService.changeConfigurationStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(DiagnosticsRequestedEvent event) {
        log.info("OCPP 1.2 DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp12Client.getDiagnostics(event.getChargingStationId(), event.getUploadLocation(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename);
    }

    @EventHandler
    public void handle(ClearCacheRequestedEvent event) {
        log.info("OCPP 1.2 ClearCacheRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.clearCache(event.getChargingStationId());

        domainService.clearCacheStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(FirmwareUpdateRequestedEvent event) {
        log.info("OCPP 1.2 FirmwareUpdateRequestedEvent");
        Map<String,String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if(attributes != null) {
            attrNumRetries = attributes.get("NUM_RETRIES");
            attrRetryInterval = attributes.get("RETRY_INTERVAL");
        }
        Integer numRetries = (!StringUtils.isEmpty(attrNumRetries))? Integer.parseInt(attrNumRetries): null;
        Integer retryInterval = (!StringUtils.isEmpty(attrRetryInterval))? Integer.parseInt(attrRetryInterval): null;

        chargingStationOcpp12Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    public void setChargingStationOcpp12Client(ChargingStationOcpp12Client chargingStationOcpp12Client) {
        this.chargingStationOcpp12Client = chargingStationOcpp12Client;
    }
}
