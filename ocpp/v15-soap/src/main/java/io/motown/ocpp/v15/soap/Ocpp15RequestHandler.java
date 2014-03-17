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
package io.motown.ocpp.v15.soap;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Ocpp15RequestHandler implements OcppRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(Ocpp15RequestHandler.class);

    private DomainService domainService;

    private ChargingStationOcpp15Client chargingStationOcpp15Client;

    public static final String PROTOCOL_IDENTIFIER = "OCPPS15";

    @Override
    public void handle(ConfigurationRequestedEvent event) {
        LOG.info("Handling ConfigurationRequestedEvent");
        Map<String, String> configurationItems = chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());

        domainService.configureChargingStation(event.getChargingStationId(), configurationItems);
    }

    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            RequestStatus requestStatus = chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());

            domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
        } else {
            LOG.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("SoftResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.softReset(event.getChargingStationId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("HardResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.hardReset(event.getChargingStationId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("StartTransactionRequestedEvent");
        RequestStatus requestStatus =  chargingStationOcpp15Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("UnlockEvseRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.unlockConnector(event.getChargingStationId(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(DataTransferEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("DataTransferEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.dataTransfer(event.getChargingStationId(), event.getVendorId(), event.getMessageId(), event.getData());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(ChangeConfigurationEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeConfigurationEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.changeConfiguration(event.getChargingStationId(), event.getKey(), event.getValue());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp15Client.getDiagnostics(event.getChargingStationId(), event.getUploadLocation(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename, statusCorrelationToken);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ClearCacheRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp15Client.clearCache(event.getChargingStationId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("FirmwareUpdateRequestedEvent");
        Map<String,String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if(attributes != null) {
            attrNumRetries = attributes.get("NUM_RETRIES");
            attrRetryInterval = attributes.get("RETRY_INTERVAL");
        }
        Integer numRetries = (attrNumRetries != null && !"".equals(attrNumRetries))? Integer.parseInt(attrNumRetries): null;
        Integer retryInterval = (attrRetryInterval != null && !"".equals(attrRetryInterval))? Integer.parseInt(attrRetryInterval): null;

        chargingStationOcpp15Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("AuthorizationListVersionRequestedEvent");

        int currentVersion = chargingStationOcpp15Client.getAuthorizationListVersion(event.getChargingStationId());

        domainService.authorizationListVersionReceived(event.getChargingStationId(), currentVersion, statusCorrelationToken);
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("SendAuthorizationListRequestedEvent");

        RequestStatus requestStatus = chargingStationOcpp15Client.sendAuthorizationList(event.getChargingStationId(), event.getAuthorizationListHash(), event.getAuthorizationListVersion(), event.getAuthorizationList(), event.getUpdateType());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ReserveNowRequestedEvent");

        NumberedReservationId reservationIdentifier = domainService.generateReservationIdentifier(event.getChargingStationId(), event.getProtocol());

        ReservationStatus reservationStatus = chargingStationOcpp15Client.reserveNow(event.getChargingStationId(), event.getEvseId(), event.getIdentifyingToken(), event.getExpiryDate(), event.getParentIdentifyingToken(), reservationIdentifier.getNumber());
        String reservationStatusMessage = (reservationStatus != null) ? reservationStatus.name() : "";

        RequestStatus requestStatus = ReservationStatus.ACCEPTED.equals(reservationStatus) ? RequestStatus.SUCCESS : RequestStatus.FAILURE;

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, reservationStatusMessage);
    }

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
