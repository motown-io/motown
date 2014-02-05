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
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class Ocpp15RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(io.motown.ocpp.viewmodel.Ocpp15RequestHandler.class);

    @Autowired
    public DomainService domainService;

    @Autowired
    private ChargingStationOcpp15Client chargingStationOcpp15Client;

    @EventHandler
    public void handle(ConfigurationRequestedEvent event) {
        log.info("Handling ConfigurationRequestedEvent");
        HashMap<String, String> configurationItems = chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());

        domainService.configureChargingStation(event.getChargingStationId(), configurationItems);
    }

    @EventHandler
    public void handle(StopTransactionRequestedEvent event) {
        log.info("StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            RequestStatus requestStatus = chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());

            domainService.stopTransactionStatusChanged(event.getChargingStationId(), requestStatus);
        } else {
            log.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @EventHandler
    public void handle(SoftResetChargingStationRequestedEvent event) {
        log.info("SoftResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.softReset(event.getChargingStationId());

        domainService.softResetStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(HardResetChargingStationRequestedEvent event) {
        log.info("HardResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.hardReset(event.getChargingStationId());

        domainService.hardResetStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(StartTransactionRequestedEvent event) {
        log.info("StartTransactionRequestedEvent");
        RequestStatus requestStatus =  chargingStationOcpp15Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getConnectorId());

        domainService.startTransactionStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(UnlockConnectorRequestedEvent event) {
        log.info("UnlockConnectorRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.unlockConnector(event.getChargingStationId(), event.getConnectorId());

        domainService.unlockConnectorStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event) {
        log.info("ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), event.getConnectorId());

        domainService.changeAvailabilityToInoperativeStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event) {
        log.info("ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), event.getConnectorId());

        domainService.changeAvailabilityToOperativeStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(DataTransferEvent event) {
        log.info("DataTransferEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.dataTransfer(event.getChargingStationId(), event.getVendorId(), event.getMessageId(), event.getData());

        domainService.dateTransferStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ChangeConfigurationEvent event) {
        log.info("ChangeConfigurationEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.changeConfiguration(event.getChargingStationId(), event.getKey(), event.getValue());

        domainService.changeConfigurationStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(DiagnosticsRequestedEvent event) {
        log.info("DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp15Client.getDiagnostics(event.getChargingStationId(), event.getUploadLocation(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename);
    }

    @EventHandler
    public void handle(ClearCacheRequestedEvent event) {
        log.info("ClearCacheRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.clearCache(event.getChargingStationId());

        domainService.clearCacheStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(FirmwareUpdateRequestedEvent event) {
        log.info("FirmwareUpdateRequestedEvent");
        Map<String,String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if(attributes != null) {
            attrNumRetries = attributes.get("NUM_RETRIES");
            attrRetryInterval = attributes.get("RETRY_INTERVAL");
        }
        Integer numRetries = (!StringUtils.isEmpty(attrNumRetries))? Integer.parseInt(attrNumRetries): null;
        Integer retryInterval = (!StringUtils.isEmpty(attrRetryInterval))? Integer.parseInt(attrRetryInterval): null;

        chargingStationOcpp15Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    @EventHandler
    public void handle(AuthorizationListVersionRequestedEvent event) {
        log.info("AuthorizationListVersionRequestedEvent");

        int currentVersion = chargingStationOcpp15Client.getAuthorizationListVersion(event.getChargingStationId());

        domainService.authorizationListVersionReceived(event.getChargingStationId(), currentVersion);
    }

    @EventHandler
    public void handle(SendAuthorizationListRequestedEvent event) {
        log.info("SendAuthorizationListRequestedEvent");

        RequestStatus requestStatus = chargingStationOcpp15Client.sendAuthorizationList(event.getChargingStationId(), event.getAuthorizationListHash(), event.getAuthorizationListVersion(), event.getAuthorizationList(), event.getUpdateType());

        domainService.sendAuthorizationListStatusChanged(event.getChargingStationId(), requestStatus);
    }

    @EventHandler
    public void handle(ReserveNowRequestedEvent event) {
        log.info("ReserveNowRequestedEvent");

        NumberedReservationId reservationIdentifier = domainService.generateReservationIdentifier(event.getChargingStationId(), event.getProtocol());

        ReservationStatus resultStatus = chargingStationOcpp15Client.reserveNow(event.getChargingStationId(), event.getConnectorId(), event.getIdentifyingToken(), event.getExpiryDate(), event.getParentIdentifyingToken(), reservationIdentifier.getNumber());

        domainService.reservationStatusChanged(event.getChargingStationId(), reservationIdentifier, resultStatus);
    }

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
    }
}
