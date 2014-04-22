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

package io.motown.ocpp.v12.soap.chargepoint;

import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.v12.soap.chargepoint.schema.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp12Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

public class ChargingStationOcpp12SoapClient implements ChargingStationOcpp12Client {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationOcpp12SoapClient.class);

    private DomainService domainService;

    private ChargingStationProxyFactory chargingStationProxyFactory;

    @Override
    public RequestResult startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, EvseId evseId) {
        LOG.info("Requesting remote start transaction on {}", id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(identifyingToken.getToken());
        request.setConnectorId(evseId.getNumberedId());

        RemoteStartTransactionResponse response = chargePointService.remoteStartTransaction(request, id.getId());

        if (RemoteStartStopStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Remote start transaction request on {} has been accepted", id);
            return RequestResult.SUCCESS;
        } else {
            LOG.warn("Remote start transaction request on {} has been rejected", id);
            return RequestResult.FAILURE;
        }
    }

    @Override
    public RequestResult stopTransaction(ChargingStationId id, int transactionId) {
        LOG.debug("Stopping transaction {} on {}", transactionId, id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionId);
        RemoteStopTransactionResponse response;

        response = chargePointService.remoteStopTransaction(request, id.getId());

        if (RemoteStartStopStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Stop transaction {} on {} has been accepted", transactionId, id);
            return RequestResult.SUCCESS;
        } else {
            LOG.warn("Stop transaction {} on {} has been rejected", transactionId, id);
            return RequestResult.FAILURE;
        }
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
    public RequestResult clearCache(ChargingStationId id) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ClearCacheRequest request = new ClearCacheRequest();

        RequestResult requestResult;
        ClearCacheResponse response = chargePointService.clearCache(request, id.getId());

        if (ClearCacheStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Clear cache on {} has been accepted", id.getId());
            requestResult = RequestResult.SUCCESS;
        } else {
            LOG.warn("Clear cache on {} has been rejected", id.getId());
            requestResult = RequestResult.FAILURE;
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

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setChargingStationProxyFactory(ChargingStationProxyFactory chargingStationProxyFactory) {
        this.chargingStationProxyFactory = chargingStationProxyFactory;
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
