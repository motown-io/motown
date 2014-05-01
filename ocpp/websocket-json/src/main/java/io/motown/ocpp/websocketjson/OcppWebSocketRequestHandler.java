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
import io.motown.ocpp.websocketjson.schema.generated.v15.Changeavailability;
import org.axonframework.common.annotation.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OcppWebSocketRequestHandler implements OcppRequestHandler {

    public static final String PROTOCOL_IDENTIFIER = "OCPPJ15";
    private static final Logger LOG = LoggerFactory.getLogger(OcppWebSocketRequestHandler.class);
    private OcppJsonService ocppJsonService;

    @Override
    public void handle(ConfigurationItemsRequestedEvent event) {
        LOG.info("Handling ConfigurationItemsRequestedEvent");
        ocppJsonService.getConfiguration(event.getChargingStationId());
    }

    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("StopTransactionRequestedEvent");
        ocppJsonService.remoteStopTransaction(event.getChargingStationId(), event.getTransactionId(), correlationToken);
    }

    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("SoftResetChargingStationRequestedEvent");
        ocppJsonService.softReset(event.getChargingStationId(), correlationToken);
    }

    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("HardResetChargingStationRequestedEvent");
        ocppJsonService.hardReset(event.getChargingStationId(), correlationToken);
    }

    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("StartTransactionRequestedEvent");
        ocppJsonService.remoteStartTransaction(event.getChargingStationId(), event.getEvseId(), event.getIdentifyingToken(), correlationToken);
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("UnlockEvseRequestedEvent");
        ocppJsonService.unlockEvse(event.getChargingStationId(), event.getEvseId(), correlationToken);
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken correlationToken) {
        ocppJsonService.changeAvailability(event.getChargingStationId(), new EvseId(0), Changeavailability.Type.INOPERATIVE, correlationToken);
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {
        ocppJsonService.changeAvailability(event.getChargingStationId(), new EvseId(0), Changeavailability.Type.OPERATIVE, correlationToken);
    }

    @Override
    public void handle(ChangeComponentAvailabilityToInoperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken correlationToken) {
        ocppJsonService.changeAvailability(event.getChargingStationId(), (EvseId) event.getComponentId(), Changeavailability.Type.INOPERATIVE, correlationToken);
    }

    @Override
    public void handle(ChangeComponentAvailabilityToOperativeRequestedEvent event, CorrelationToken correlationToken) {
        ocppJsonService.changeAvailability(event.getChargingStationId(), (EvseId) event.getComponentId(), Changeavailability.Type.OPERATIVE, correlationToken);
    }

    @Override
    public void handle(DataTransferRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("DataTransferRequestedEvent");
        ocppJsonService.dataTransfer(event.getChargingStationId(), event.getVendorId(), event.getMessageId(), event.getData(), correlationToken);
    }

    @Override
    public void handle(ChangeConfigurationItemRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("ChangeConfigurationItemRequestedEvent");
        ocppJsonService.changeConfiguration(event.getChargingStationId(), event.getConfigurationItem(), correlationToken);
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("DiagnosticsRequestedEvent");
        ocppJsonService.getDiagnostics(event.getChargingStationId(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime(), event.getUploadLocation(), correlationToken);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("ClearCacheRequestedEvent");
        ocppJsonService.clearCache(event.getChargingStationId(), correlationToken);
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("FirmwareUpdateRequestedEvent");
        ocppJsonService.updateFirmware(event.getChargingStationId(), event.getRetrieveDate(), event.getAttributes(), event.getUpdateLocation());
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("AuthorizationListVersionRequestedEvent");
        ocppJsonService.getLocalListVersion(event.getChargingStationId(), correlationToken);
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("SendAuthorizationListRequestedEvent");
        ocppJsonService.sendLocalList(event.getChargingStationId(), event.getUpdateType(), event.getAuthorizationList(), event.getAuthorizationListVersion(), event.getAuthorizationListHash(), correlationToken);
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("ReserveNowRequestedEvent");
        ocppJsonService.reserveNow(event.getChargingStationId(), event.getEvseId(), event.getIdentifyingToken(), event.getParentIdentifyingToken(), event.getExpiryDate(), correlationToken);
    }

    @Override
    public void handle(CancelReservationRequestedEvent event, CorrelationToken correlationToken) {
        LOG.info("CancelReservationRequestedEvent");
        ocppJsonService.cancelReservation(event.getChargingStationId(), (NumberedReservationId) event.getReservationId(), correlationToken);
    }

    public void setOcppJsonService(OcppJsonService ocppJsonService) {
        this.ocppJsonService = ocppJsonService;
    }
}
