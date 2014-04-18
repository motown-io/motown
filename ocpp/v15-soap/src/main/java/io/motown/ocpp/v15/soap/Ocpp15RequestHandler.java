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
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.ocpp.viewmodel.OcppRequestHandler;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import org.axonframework.common.annotation.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Ocpp15RequestHandler implements OcppRequestHandler {

    public static final String PROTOCOL_IDENTIFIER = "OCPPS15";
    public static final String ADD_ON_TYPE = "OCPPS15";
    private static final Logger LOG = LoggerFactory.getLogger(Ocpp15RequestHandler.class);
    private DomainService domainService;
    private ChargingStationOcpp15Client chargingStationOcpp15Client;
    private AddOnIdentity addOnIdentity;

    @Override
    public void handle(ConfigurationItemsRequestedEvent event) {
        LOG.info("Handling ConfigurationItemsRequestedEvent");
        Map<String, String> configurationItems = chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());

        domainService.receiveConfigurationItems(event.getChargingStationId(), configurationItems, addOnIdentity);
    }

    @Override
    public void handle(StopTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("StopTransactionRequestedEvent");

        if (event.getTransactionId() instanceof NumberedTransactionId) {
            NumberedTransactionId transactionId = (NumberedTransactionId) event.getTransactionId();
            RequestResult requestResult = chargingStationOcpp15Client.stopTransaction(event.getChargingStationId(), transactionId.getNumber());

            domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
        } else {
            LOG.warn("StopTransactionRequestedEvent does not contain a NumberedTransactionId. Event: {}", event);
        }
    }

    @Override
    public void handle(SoftResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("SoftResetChargingStationRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.softReset(event.getChargingStationId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(HardResetChargingStationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("HardResetChargingStationRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.hardReset(event.getChargingStationId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(StartTransactionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("StartTransactionRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.startTransaction(event.getChargingStationId(), event.getIdentifyingToken(), event.getEvseId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(UnlockEvseRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("UnlockEvseRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.unlockConnector(event.getChargingStationId(), event.getEvseId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToInoperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.5 ChangeChargingStationAvailabilityToInoperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), chargingStationEvseId);

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeChargingStationAvailabilityToInoperative(event.getChargingStationId(), statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of chargingstation {} to inoperative", event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeChargingStationAvailabilityToOperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.5 ChangeChargingStationAvailabilityToOperativeRequestedEvent");
        EvseId chargingStationEvseId = new EvseId(0);
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), chargingStationEvseId);

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeChargingStationAvailabilityToOperative(event.getChargingStationId(), statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of chargingstation {} to operative", event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToInoperativeRequestedEvent event, @MetaData(CorrelationToken.KEY) CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.5 ChangeComponentAvailabilityToInoperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToInoperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeComponentAvailabilityToInoperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of evse {} on chargingstation {} to inoperative", event.getComponentId().getId(), event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeComponentAvailabilityToOperativeRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("OCPP 1.5 ChangeComponentAvailabilityToOperativeRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.changeAvailabilityToOperative(event.getChargingStationId(), (EvseId) event.getComponentId());

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.changeComponentAvailabilityToOperative(event.getChargingStationId(), event.getComponentId(), ChargingStationComponent.EVSE, statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to set availability of evse {} on chargingstation {} to operative", event.getComponentId().getId(), event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(DataTransferRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("DataTransferRequestedEvent");
        DataTransferRequestResult result = chargingStationOcpp15Client.dataTransfer(event.getChargingStationId(), event.getVendorId(), event.getMessageId(), event.getData());

        if (result.isSuccessfull()) {
            String responseData = result.getData();
            //In case data has been returned, we treat that as if it was incoming data
            if (responseData != null) {
                domainService.informDataTransferResponse(event.getChargingStationId(), responseData, statusCorrelationToken, addOnIdentity);
            }
        } else {
            LOG.error("Failed to request datatransfer to chargingstation {}", event.getChargingStationId().getId());
        }
    }

    @Override
    public void handle(ChangeConfigurationItemRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ChangeConfigurationItemRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.changeConfiguration(event.getChargingStationId(), event.getKey(), event.getValue());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(DiagnosticsRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("DiagnosticsRequestedEvent");
        String diagnosticsFilename = chargingStationOcpp15Client.getDiagnostics(event.getChargingStationId(), event.getUploadLocation(), event.getNumRetries(), event.getRetryInterval(), event.getPeriodStartTime(), event.getPeriodStopTime());

        domainService.diagnosticsFileNameReceived(event.getChargingStationId(), diagnosticsFilename, statusCorrelationToken, addOnIdentity);
    }

    @Override
    public void handle(ClearCacheRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ClearCacheRequestedEvent");
        RequestResult requestResult = chargingStationOcpp15Client.clearCache(event.getChargingStationId());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(FirmwareUpdateRequestedEvent event) {
        LOG.info("FirmwareUpdateRequestedEvent");
        Map<String, String> attributes = event.getAttributes();

        String attrNumRetries = null;
        String attrRetryInterval = null;
        if (attributes != null) {
            attrNumRetries = attributes.get(FirmwareUpdateAttributeKey.NUM_RETRIES);
            attrRetryInterval = attributes.get(FirmwareUpdateAttributeKey.RETRY_INTERVAL);
        }
        Integer numRetries = (attrNumRetries != null && !"".equals(attrNumRetries)) ? Integer.parseInt(attrNumRetries) : null;
        Integer retryInterval = (attrRetryInterval != null && !"".equals(attrRetryInterval)) ? Integer.parseInt(attrRetryInterval) : null;

        chargingStationOcpp15Client.updateFirmware(event.getChargingStationId(), event.getUpdateLocation(), event.getRetrieveDate(), numRetries, retryInterval);
    }

    @Override
    public void handle(AuthorizationListVersionRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("AuthorizationListVersionRequestedEvent");

        int currentVersion = chargingStationOcpp15Client.getAuthorizationListVersion(event.getChargingStationId());

        domainService.authorizationListVersionReceived(event.getChargingStationId(), currentVersion, statusCorrelationToken, addOnIdentity);
    }

    @Override
    public void handle(SendAuthorizationListRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("SendAuthorizationListRequestedEvent");

        RequestResult requestResult = chargingStationOcpp15Client.sendAuthorizationList(event.getChargingStationId(), event.getAuthorizationListHash(), event.getAuthorizationListVersion(), event.getAuthorizationList(), event.getUpdateType());

        domainService.informRequestResult(event.getChargingStationId(), requestResult, statusCorrelationToken, "", addOnIdentity);
    }

    @Override
    public void handle(ReserveNowRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("ReserveNowRequestedEvent");

        NumberedReservationId reservationIdentifier = domainService.generateReservationIdentifier(event.getChargingStationId(), event.getProtocol());

        ReservationStatus reservationStatus = chargingStationOcpp15Client.reserveNow(event.getChargingStationId(), event.getEvseId(), event.getIdentifyingToken(), event.getExpiryDate(), event.getParentIdentifyingToken(), reservationIdentifier.getNumber());

        if (ReservationStatus.ACCEPTED.equals(reservationStatus)) {
            domainService.informReserved(event.getChargingStationId(), reservationIdentifier, event.getEvseId(), event.getExpiryDate(), statusCorrelationToken, addOnIdentity);
        } else {
            String reservationStatusMessage = (reservationStatus != null) ? reservationStatus.name() : "";
            LOG.error("Failed to reserve evse {} on charging station {}: {}", event.getEvseId().getId(), event.getChargingStationId().getId(), reservationStatusMessage);
        }
    }

    @Override
    public void handle(CancelReservationRequestedEvent event, CorrelationToken statusCorrelationToken) {
        LOG.info("CancelReservationRequestedEvent");

        RequestResult requestResult = chargingStationOcpp15Client.cancelReservation(event.getChargingStationId(), ((NumberedReservationId) event.getReservationId()).getNumber());

        if (RequestResult.SUCCESS.equals(requestResult)) {
            domainService.informReservationCancelled(event.getChargingStationId(), event.getReservationId(), statusCorrelationToken, addOnIdentity);
        } else {
            LOG.error("Failed to cancel reservation with reservationId {}", event.getReservationId().getId());
        }
    }

    public void setChargingStationOcpp15Client(ChargingStationOcpp15Client chargingStationOcpp15Client) {
        this.chargingStationOcpp15Client = chargingStationOcpp15Client;
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
