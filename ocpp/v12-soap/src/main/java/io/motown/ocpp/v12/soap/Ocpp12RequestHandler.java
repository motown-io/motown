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
package io.motown.ocpp.v12.soap;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp12Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Ocpp12RequestHandler implements OcppRequestHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(Ocpp12RequestHandler.class);

    private DomainService domainService;

    private ChargingStationOcpp12Client chargingStationOcpp12Client;

    @Override
    public void handle(ConfigurationRequestedEvent event) {
        // no implementation in OCPP 1.2
    }

    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            RequestStatus requestStatus = chargingStationOcpp12Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());

            domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
        } else {
            LOG.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 SoftResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.softReset(event.getChargingStationId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 HardResetChargingStationRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.hardReset(event.getChargingStationId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 StartTransactionRequestedEvent");
        RequestStatus requestStatus =  chargingStationOcpp12Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 UnlockEvseRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.unlockConnector(event.getChargingStationId(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.changeAvailabilityToInoperative(event.getChargingStationId(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.changeAvailabilityToOperative(event.getChargingStationId(), event.getEvseId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(DataTransferEvent event, CorrelationToken statusCorrelationToken) {
        // no implementation in OCPP 1.2
    }

    @Override
    public void handle(ChangeConfigurationEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeConfigurationEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.changeConfiguration(event.getChargingStationId(), event.getKey(), event.getValue());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp12Client.getDiagnostics(event.getChargingStationId(), event.getUploadLocation(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename, statusCorrelationToken);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ClearCacheRequestedEvent");
        RequestStatus requestStatus = chargingStationOcpp12Client.clearCache(event.getChargingStationId());

        domainService.statusChanged(event.getChargingStationId(), requestStatus, statusCorrelationToken, "");
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("OCPP 1.2 FirmwareUpdateRequestedEvent");
        Map<String,String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if(attributes != null) {
            attrNumRetries = attributes.get("NUM_RETRIES");
            attrRetryInterval = attributes.get("RETRY_INTERVAL");
        }
        Integer numRetries = (attrNumRetries != null && !"".equals(attrNumRetries))? Integer.parseInt(attrNumRetries): null;
        Integer retryInterval = (attrRetryInterval != null && !"".equals(attrRetryInterval))? Integer.parseInt(attrRetryInterval): null;

        chargingStationOcpp12Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        // no implementation in OCPP 1.2
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken statusCorrelationToken) {
        // no implementation in OCPP 1.2
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken statusCorrelationToken) {
        // no implementation in OCPP 1.2
    }

    public void setChargingStationOcpp12Client(ChargingStationOcpp12Client chargingStationOcpp12Client) {
        this.chargingStationOcpp12Client = chargingStationOcpp12Client;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }
}
