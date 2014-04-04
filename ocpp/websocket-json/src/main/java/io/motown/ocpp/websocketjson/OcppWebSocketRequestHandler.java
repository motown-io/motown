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
package io.motown.ocpp.websocketjson;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OcppWebSocketRequestHandler implements OcppRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(OcppWebSocketRequestHandler.class);

    private OcppJsonService ocppJsonService;

    // TODO ocppj 1.2?
    public static final String PROTOCOL_IDENTIFIER = "OCPPJ15";

    @Override
    public void handle(ConfigurationRequestedEvent event) {
        LOG.info("Handling ConfigurationRequestedEvent");
        ocppJsonService.getConfiguration(event.getChargingStationId());
    }

    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("StopTransactionRequestedEvent");
        ocppJsonService.remoteStopTransaction(event.getChargingStationId(), event.getTransactionId(), statusCorrelationToken);
    }

    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("SoftResetChargingStationRequestedEvent");
        ocppJsonService.softReset(event.getChargingStationId(), statusCorrelationToken);
    }

    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("HardResetChargingStationRequestedEvent");
        ocppJsonService.hardReset(event.getChargingStationId(), statusCorrelationToken);
    }

    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("StartTransactionRequestedEvent");
        ocppJsonService.remoteStartTransaction(event.getChargingStationId(), event.getEvseId(), event.getIdentifyingToken(), statusCorrelationToken);
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("UnlockEvseRequestedEvent");
        ocppJsonService.unlockEvse(event.getChargingStationId(), event.getEvseId(), statusCorrelationToken);
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeChargingStationAvailabilityToOperativeRequestedEvent");
    }

    @Override
    public void handle(DataTransferEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("DataTransferEvent");
    }

    @Override
    public void handle(ChangeConfigurationEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeConfigurationEvent");
        ocppJsonService.changeConfiguration(event.getChargingStationId(), event.getKey(), event.getValue(), statusCorrelationToken);
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("DiagnosticsRequestedEvent");
        ocppJsonService.getDiagnostics(event.getChargingStationId(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime(), event.getUploadLocation(), statusCorrelationToken);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ClearCacheRequestedEvent");
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("FirmwareUpdateRequestedEvent");
        ocppJsonService.updateFirmware(event.getChargingStationId(), event.getRetrieveDate(), event.getAttributes(), event.getUpdateLocation());
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("AuthorizationListVersionRequestedEvent");
        ocppJsonService.getLocalListVersion(event.getChargingStationId(), statusCorrelationToken);
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("SendAuthorizationListRequestedEvent");
        ocppJsonService.sendLocalList(event.getChargingStationId(), event.getUpdateType(), event.getAuthorizationList(), event.getAuthorizationListVersion(), event.getAuthorizationListHash(), statusCorrelationToken);
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ReserveNowRequestedEvent");
    }

    public void setOcppJsonService(OcppJsonService ocppJsonService) {
        this.ocppJsonService = ocppJsonService;
    }
}
