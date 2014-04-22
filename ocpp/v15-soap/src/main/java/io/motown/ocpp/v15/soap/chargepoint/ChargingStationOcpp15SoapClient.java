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
package io.motown.ocpp.v15.soap.chargepoint;

import com.google.common.collect.Maps;
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.v15.soap.chargepoint.schema.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChargingStationOcpp15SoapClient implements ChargingStationOcpp15Client {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationOcpp15SoapClient.class);
    private static final String UNKNOWN_STATUS = "[unknown status]";

    private DomainService domainService;

    private ChargingStationProxyFactory chargingStationProxyFactory;

    private IdentifyingTokenConverterService identifyingTokenConverterService;

    public Map<String, String> getConfiguration(ChargingStationId id) {
        LOG.info("Retrieving configuration for {}", id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        GetConfigurationResponse response = chargePointService.getConfiguration(new GetConfigurationRequest(), id.getId());

        Map<String, String> configurationItems = Maps.newHashMap();
        for (KeyValue keyValue : response.getConfigurationKey()) {
            configurationItems.put(keyValue.getKey(), keyValue.getValue());
        }

        return configurationItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, EvseId evseId) {
        LOG.info("Requesting remote start transaction on {}", id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(identifyingToken.getToken());
        request.setConnectorId(evseId.getNumberedId());

        RemoteStartTransactionResponse response = chargePointService.remoteStartTransaction(request, id.getId());

        boolean willTransactionStart;

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Remote start transaction request on {} has been accepted", id);
                willTransactionStart = true;
                break;
            case REJECTED:
                LOG.info("Remote start transaction request on {} has been rejected", id);
                willTransactionStart = false;
                break;
            default:
                throw new AssertionError("Start transaction returned unknown response status " + response.getStatus());
        }

        return willTransactionStart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopTransaction(ChargingStationId id, int transactionId) {
        LOG.debug("Stopping transaction {} on {}", transactionId, id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionId);

        RemoteStopTransactionResponse response = chargePointService.remoteStopTransaction(request, id.getId());

        boolean willTransactionStop;

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Remote stop transaction request on {} has been accepted", id);
                willTransactionStop = true;
                break;
            case REJECTED:
                LOG.info("Remote stop transaction request on {} has been rejected", id);
                willTransactionStop = false;
                break;
            default:
                throw new AssertionError("Stop transaction returned unknown response status " + response.getStatus());
        }

        return willTransactionStop;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean softReset(ChargingStationId id) {
        LOG.info("Requesting soft reset on {}", id);
        return reset(id, ResetType.SOFT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hardReset(ChargingStationId id) {
        LOG.info("Requesting hard reset on {}", id);
        return reset(id, ResetType.HARD);
    }

    @Override
    public RequestResult unlockConnector(ChargingStationId id, EvseId evseId) {
        LOG.debug("Unlocking of connector {} on {}", evseId, id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        UnlockConnectorRequest request = new UnlockConnectorRequest();
        request.setConnectorId(evseId.getNumberedId());

        UnlockConnectorResponse response = chargePointService.unlockConnector(request, id.getId());

        if (UnlockStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Unlocking of connector {} on {} has been accepted", evseId, id);
            return RequestResult.SUCCESS;
        } else {
            LOG.warn("Unlocking of connector {} on {} has been rejected", evseId, id);
            return RequestResult.FAILURE;
        }
    }

    @Override
    public RequestResult changeAvailabilityToInoperative(ChargingStationId id, EvseId evseId) {
        LOG.debug("Changing availability of connector {} on {} to inoperative", evseId, id);

        return changeAvailability(id, evseId, AvailabilityType.INOPERATIVE);
    }

    @Override
    public RequestResult changeAvailabilityToOperative(ChargingStationId id, EvseId evseId) {
        LOG.debug("Changing availability of connector {} on {} to operative", evseId, id);

        return changeAvailability(id, evseId, AvailabilityType.OPERATIVE);
    }

    @Override
    public DataTransferRequestResult dataTransfer(ChargingStationId id, String vendorId, String messageId, String data) {
        LOG.debug("Data transfer to {}", id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        DataTransferRequest request = new DataTransferRequest();
        request.setVendorId(vendorId);
        request.setMessageId(messageId);
        request.setData(data);

        DataTransferResponse response = chargePointService.dataTransfer(request, id.getId());

        if (response.getStatus() == DataTransferStatus.UNKNOWN_MESSAGE_ID) {
            LOG.error("Unknown message id {} for datatransfer request", messageId);
        } else if(response.getStatus() == DataTransferStatus.UNKNOWN_VENDOR_ID) {
            LOG.error("Unknown vendor id {} for datatransfer request", vendorId);
        }

        RequestResult requestResult = DataTransferStatus.ACCEPTED.equals(response.getStatus()) ? RequestResult.SUCCESS : RequestResult.FAILURE;
        return new DataTransferRequestResult(requestResult, response.getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean changeConfiguration(ChargingStationId id, ConfigurationItem configurationItem) {
        checkNotNull(id);
        checkNotNull(configurationItem);

        final String key = configurationItem.getKey();
        final String value = configurationItem.getValue();

        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeConfigurationRequest request = new ChangeConfigurationRequest();
        request.setKey(key);
        request.setValue(value);

        ChangeConfigurationResponse response = chargePointService.changeConfiguration(request, id.getId());

        boolean hasConfigurationChanged;

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Configuration change of {} on {} has been accepted", key, id);
                hasConfigurationChanged = true;
                break;
            case REJECTED:
                LOG.info("Configuration change of {} on {} was rejected", key, id);
                hasConfigurationChanged = false;
                break;
            case NOT_SUPPORTED:
                LOG.info("Configuration change of {} on {} was not supported", key, id);
                hasConfigurationChanged = false;
                break;
            default:
                throw new AssertionError("Configuration change returned unknown response status " + response.getStatus());
        }

        return hasConfigurationChanged;
    }

    @Override
    public String getDiagnostics(ChargingStationId id, String uploadLocation, Integer numRetries, Integer retryInterval, Date periodStartTime, Date periodStopTime) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        GetDiagnosticsRequest request = new GetDiagnosticsRequest();
        request.setLocation(uploadLocation);
        request.setRetries(numRetries);
        request.setRetryInterval(retryInterval);
        request.setStartTime(periodStartTime);
        request.setStopTime(periodStopTime);

        GetDiagnosticsResponse response = chargePointService.getDiagnostics(request, id.getId());
        return response.getFileName();
    }

    @Override
    public boolean clearCache(ChargingStationId id) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ClearCacheRequest request = new ClearCacheRequest();

        boolean requestResult;
        ClearCacheResponse response = chargePointService.clearCache(request, id.getId());

        if (ClearCacheStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Clear cache on {} has been accepted", id.getId());
            requestResult = true;
        } else {
            LOG.warn("Clear cache on {} has been rejected", id.getId());
            requestResult = false;
        }

        return requestResult;
    }

    @Override
    public void updateFirmware(ChargingStationId id, String downloadLocation, Date retrieveDate, Integer numRetries, Integer retryInterval) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        UpdateFirmwareRequest request = new UpdateFirmwareRequest();
        request.setLocation(downloadLocation);
        request.setRetrieveDate(retrieveDate);
        request.setRetries(numRetries);
        request.setRetryInterval(retryInterval);

        chargePointService.updateFirmware(request, id.getId());

        //The charging station will respond with an async 'firmware status update' message
        LOG.info("Update firmware on {} has been requested", id.getId());
    }

    @Override
    public int getAuthorizationListVersion(ChargingStationId id) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        GetLocalListVersionResponse response = chargePointService.getLocalListVersion(new GetLocalListVersionRequest(), id.getId());
        int currentWhitelistVersion = response.getListVersion();

        LOG.info("At the moment {} has authorizationlist version {}", id.getId(), currentWhitelistVersion);
        return currentWhitelistVersion;
    }

    @Override
    public RequestResult sendAuthorizationList(ChargingStationId id, String hash, int listVersion, List<IdentifyingToken> identifyingTokens, AuthorizationListUpdateType updateType) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        SendLocalListRequest request = new SendLocalListRequest();
        request.setHash(hash);
        request.setListVersion(listVersion);

        // Translate the update type to the OCPP specific type
        switch (updateType) {
            case DIFFERENTIAL:
                request.setUpdateType(UpdateType.DIFFERENTIAL);
                break;
            case FULL:
                request.setUpdateType(UpdateType.FULL);
                break;
            default:
                throw new AssertionError(String.format("Unknown update type [%s].", updateType));
        }

        // Translate the authorization information to the OCPP specific info
        request.getLocalAuthorisationList().addAll(identifyingTokenConverterService.convertIdentifyingTokenList(identifyingTokens));

        SendLocalListResponse response = chargePointService.sendLocalList(request, id.getId());

        if (UpdateStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Update of local authorization list on {} has been accepted", id);
            return RequestResult.SUCCESS;
        } else {
            String responseStatus = response.getStatus() != null ? response.getStatus().value() : UNKNOWN_STATUS;
            LOG.warn("Update of local authorization list on {} has failed due to {}", id, responseStatus);
            return RequestResult.FAILURE;
        }
    }

    @Override
    public io.motown.domain.api.chargingstation.ReservationStatus reserveNow(ChargingStationId id, EvseId evseId, IdentifyingToken identifyingToken, Date expiryDate, IdentifyingToken parentIdentifyingToken, int reservationId) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ReserveNowRequest request = new ReserveNowRequest();
        request.setConnectorId(evseId.getNumberedId());
        request.setExpiryDate(expiryDate);
        request.setIdTag(identifyingToken.getToken());
        request.setParentIdTag(parentIdentifyingToken != null ? parentIdentifyingToken.getToken() : null);
        request.setReservationId(reservationId);

        io.motown.ocpp.v15.soap.chargepoint.schema.ReservationStatus responseStatus = chargePointService.reserveNow(request, id.getId()).getStatus();
        io.motown.domain.api.chargingstation.ReservationStatus result;

        switch (responseStatus) {
            case ACCEPTED:
                result = io.motown.domain.api.chargingstation.ReservationStatus.ACCEPTED;
                break;
            case FAULTED:
                result = io.motown.domain.api.chargingstation.ReservationStatus.FAULTED;
                break;
            case OCCUPIED:
                result = io.motown.domain.api.chargingstation.ReservationStatus.OCCUPIED;
                break;
            case REJECTED:
                result = io.motown.domain.api.chargingstation.ReservationStatus.REJECTED;
                break;
            case UNAVAILABLE:
                result = io.motown.domain.api.chargingstation.ReservationStatus.UNAVAILABLE;
                break;
            default:
                throw new AssertionError(String.format("Unknown response status [%s].", responseStatus));
        }

        return result;
    }

    @Override
    public RequestResult cancelReservation(ChargingStationId id, int reservationId) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        CancelReservationRequest cancelReservationRequest = new CancelReservationRequest();
        cancelReservationRequest.setReservationId(reservationId);

        CancelReservationResponse response = chargePointService.cancelReservation(cancelReservationRequest, id.getId());

        return CancelReservationStatus.ACCEPTED.equals(response.getStatus()) ? RequestResult.SUCCESS : RequestResult.FAILURE;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setChargingStationProxyFactory(ChargingStationProxyFactory chargingStationProxyFactory) {
        this.chargingStationProxyFactory = chargingStationProxyFactory;
    }

    public void setIdentifyingTokenConverterService(IdentifyingTokenConverterService identifyingTokenConverterService) {
        this.identifyingTokenConverterService = identifyingTokenConverterService;
    }

    /**
     * Reset a charging station.
     *
     * @param id   the charging station's id.
     * @param type the type of reset (i.e. soft or hard).
     * @return true if the charging station has reset, false if it hasn't.
     */
    private boolean reset(ChargingStationId id, ResetType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ResetRequest request = new ResetRequest();
        request.setType(type);

        ResetResponse response = chargePointService.reset(request, id.getId());

        boolean hasReset;

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Reset was accepted");
                hasReset = true;
                break;
            case REJECTED:
                LOG.info("Reset was rejected");
                hasReset = false;
                break;
            default:
                throw new AssertionError("Unknown ResetStatus: " + response.getStatus());
        }

        return hasReset;
    }

    private RequestResult changeAvailability(ChargingStationId id, EvseId evseId, AvailabilityType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest();
        request.setConnectorId(evseId.getNumberedId());
        request.setType(type);
        ChangeAvailabilityResponse response = chargePointService.changeAvailability(request, id.getId());

        if (AvailabilityStatus.ACCEPTED.equals(response.getStatus()) ||
                AvailabilityStatus.SCHEDULED.equals(response.getStatus())) {
            return RequestResult.SUCCESS;
        } else {
            return RequestResult.FAILURE;
        }
    }

    /**
     * Creates a charging station web service proxy based on the address that has been stored for this charging station identifier.
     *
     * @param id charging station identifier
     * @return charging station web service proxy
     */
    private ChargePointService createChargingStationService(ChargingStationId id) {
        return chargingStationProxyFactory.createChargingStationService(domainService.retrieveChargingStationAddress(id));
    }

}
