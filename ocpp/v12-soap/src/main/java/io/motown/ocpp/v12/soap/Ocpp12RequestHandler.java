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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp12Client;
import org.axonframework.common.annotation.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Ocpp12RequestHandler implements OcppRequestHandler {

    public static final String ADD_ON_TYPE = "OCPPS12";
    private static final Logger LOG = LoggerFactory.getLogger(Ocpp12RequestHandler.class);
    private DomainService domainService;
    private ChargingStationOcpp12Client chargingStationOcpp12Client;
    private AddOnIdentity addOnIdentity;

    @Override
    public void handle(ConfigurationItemsRequestedEvent event) {
        // no implementation in OCPP 1.2
    }

    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            RequestResult requestResult = chargingStationOcpp12Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());

            domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
        } else {
            LOG.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 SoftResetChargingStationRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.softReset(event.getChargingStationId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 HardResetChargingStationRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.hardReset(event.getChargingStationId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 StartTransactionRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getEvseId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 UnlockEvseRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.unlockConnector(event.getChargingStationId(), event.getEvseId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToInoperative(event.getChargingStationId(), chargingStationEvseId);

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeChargingStationAvailabilityToInoperative(event.getChargingStationId(), statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of chargingstation {} to inoperative", event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToOperative(event.getChargingStationId(), chargingStationEvseId);

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeChargingStationAvailabilityToOperative(event.getChargingStationId(), statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of chargingstation {} to operative", event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToInoperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeComponentAvailabilityToInoperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToInoperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeComponentAvailabilityToInoperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of evse {} on chargingstation {} to inoperative", event.getComponentId().getId(), event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToOperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeComponentAvailabilityToOperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.changeAvailabilityToOperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeComponentAvailabilityToOperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of evse {} on chargingstation {} to operative", event.getComponentId().getId(), event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(DataTransferRequestedEvent event, CorrelationToken statusCorrelationToken) {
        // no implementation in OCPP 1.2
    }

    @Override
    public void handle(ChangeConfigurationItemRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ChangeConfigurationItemRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.changeConfiguration(event.getChargingStationId(), event.getConfigurationItem().getKey(), event.getConfigurationItem().getValue());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp12Client.getDiagnostics(event.getChargingStationId(), event.getUploadLocation(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename, statusCorrelationToken, addOnIdentity);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.2 ClearCacheRequestedEvent");
        RequestResult requestResult = chargingStationOcpp12Client.clearCache(event.getChargingStationId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("OCPP 1.2 FirmwareUpdateRequestedEvent");
        Map<String, String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if (attributes != null) {
            attrNumRetries = attributes.get(FirmwareUpdateAttributeKey.NUM_RETRIES);
            attrRetryInterval = attributes.get(FirmwareUpdateAttributeKey.RETRY_INTERVAL);
        }
        Integer numRetries = (attrNumRetries != null && !"".equals(attrNumRetries)) ? Integer.parseInt(attrNumRetries) : null;
        Integer retryInterval = (attrRetryInterval != null && !"".equals(attrRetryInterval)) ? Integer.parseInt(attrRetryInterval) : null;

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

    @Override
    public void handle(CancelReservationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        // no implementation in OCPP 1.2
    }

    public void setChargingStationOcpp12Client(ChargingStationOcpp12Client chargingStationOcpp12Client) {
        this.chargingStationOcpp12Client = chargingStationOcpp12Client;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    /**
     * Sets the add-on id. The add-on is hardcoded, the add-on id should be different for every instance (in a distributed configuration)
     * to be able to differentiate between add-on instances.
     *
     * @param id add-on id.
     */
    public void setAddOnId(String id) {
        addOnIdentity = new TypeBasedAddOnIdentity(ADD_ON_TYPE, id);
    }

}
