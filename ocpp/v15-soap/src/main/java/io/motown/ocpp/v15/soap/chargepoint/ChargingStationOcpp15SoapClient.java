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
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.BindingProvider;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.motown.domain.api.chargingstation.IdentifyingToken.AuthenticationStatus;

@Component
public class ChargingStationOcpp15SoapClient implements ChargingStationOcpp15Client {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationOcpp15SoapClient.class);

    @Autowired
    private DomainService domainService;

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

    @Override
    public RequestStatus startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, EvseId evseId) {
        LOG.info("Requesting remote start transaction on {}", id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(identifyingToken.getToken());
        request.setConnectorId(evseId.getNumberedId());

        RemoteStartTransactionResponse response = chargePointService.remoteStartTransaction(request, id.getId());

        if (RemoteStartStopStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Remote start transaction request on {} has been accepted", id);
            return RequestStatus.SUCCESS;
        } else {
            LOG.warn("Remote start transaction request on {} has been rejected", id);
            return RequestStatus.FAILURE;
        }
    }

    @Override
    public RequestStatus stopTransaction(ChargingStationId id, int transactionId) {
        LOG.debug("Stopping transaction {} on {}", transactionId, id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionId);
        RemoteStopTransactionResponse response;

        response = chargePointService.remoteStopTransaction(request, id.getId());

        if (RemoteStartStopStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Stop transaction {} on {} has been accepted", transactionId, id);
            return RequestStatus.SUCCESS;
        } else {
            LOG.warn("Stop transaction {} on {} has been rejected", transactionId, id);
            return RequestStatus.FAILURE;
        }
    }

    @Override
    public RequestStatus softReset(ChargingStationId id) {
        LOG.info("Requesting soft reset on {}", id);

        return reset(id, ResetType.SOFT);
    }

    @Override
    public RequestStatus hardReset(ChargingStationId id) {
        LOG.info("Requesting hard reset on {}", id);

        return reset(id, ResetType.HARD);
    }

    @Override
    public RequestStatus unlockConnector(ChargingStationId id, EvseId evseId) {
        LOG.debug("Unlocking of connector {} on {}", evseId, id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        UnlockConnectorRequest request = new UnlockConnectorRequest();
        request.setConnectorId(evseId.getNumberedId());

        UnlockConnectorResponse response = chargePointService.unlockConnector(request, id.getId());

        if (UnlockStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Unlocking of connector {} on {} has been accepted", evseId, id);
            return RequestStatus.SUCCESS;
        } else {
            LOG.warn("Unlocking of connector {} on {} has been rejected", evseId, id);
            return RequestStatus.FAILURE;
        }
    }

    @Override
    public RequestStatus changeAvailabilityToInoperative(ChargingStationId id, EvseId evseId) {
        LOG.debug("Changing availability of connector {} on {} to inoperative", evseId, id);

        return changeAvailability(id, evseId, AvailabilityType.INOPERATIVE);
    }

    @Override
    public RequestStatus changeAvailabilityToOperative(ChargingStationId id, EvseId evseId) {
        LOG.debug("Changing availability of connector {} on {} to operative", evseId, id);

        return changeAvailability(id, evseId, AvailabilityType.OPERATIVE);
    }

    @Override
    public RequestStatus dataTransfer(ChargingStationId id, String vendorId, String messageId, String data) {
        LOG.debug("Data transfer to {}", id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        DataTransferRequest request = new DataTransferRequest();
        request.setVendorId(vendorId);
        request.setMessageId(messageId);
        request.setData(data);

        DataTransferResponse response = chargePointService.dataTransfer(request, id.getId());

        if (DataTransferStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Data transfer to {} has been accepted", id);
            return RequestStatus.SUCCESS;
        } else {
            String responseStatus = (response.getStatus() != null) ? response.getStatus().value() : "-unknown status-";
            LOG.warn("Data transfer to {} has failed due to {}", id, responseStatus);
            return RequestStatus.FAILURE;
        }
    }

    @Override
    public RequestStatus changeConfiguration(ChargingStationId id, String key, String value) {
        LOG.debug("Change configuration of {}", id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeConfigurationRequest request = new ChangeConfigurationRequest();
        request.setKey(key);
        request.setValue(value);

        ChangeConfigurationResponse response = chargePointService.changeConfiguration(request, id.getId());

        if (ConfigurationStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Configuration change of {} on {} has been accepted", key, id);
            return RequestStatus.SUCCESS;
        } else {
            String responseStatus = (response.getStatus() != null) ? response.getStatus().value() : "-unknown status-";
            LOG.warn("Configuration change of {} on {} has failed due to {}", key, id, responseStatus);
            return RequestStatus.FAILURE;
        }
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
    public RequestStatus clearCache(ChargingStationId id) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ClearCacheRequest request = new ClearCacheRequest();

        RequestStatus requestStatus;
        ClearCacheResponse response = chargePointService.clearCache(request, id.getId());

        if (ClearCacheStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Clear cache on {} has been accepted", id.getId());
            requestStatus = RequestStatus.SUCCESS;
        } else {
            LOG.warn("Clear cache on {} has been rejected", id.getId());
            requestStatus = RequestStatus.FAILURE;
        }

        return requestStatus;
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
    public RequestStatus sendAuthorizationList(ChargingStationId id, String hash, int listVersion, List<IdentifyingToken> identifyingTokens, AuthorizationListUpdateType updateType) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        SendLocalListRequest request = new SendLocalListRequest();
        request.setHash(hash);
        request.setListVersion(listVersion);

        //Translate the update type to the OCPP specific type
        switch (updateType) {
            case DIFFERENTIAL:
                request.setUpdateType(UpdateType.DIFFERENTIAL);
                break;
            case FULL:
                request.setUpdateType(UpdateType.FULL);
                break;
        }

        //Translate the authorization information to the OCPP specific info
        List<AuthorisationData> authorizationList = request.getLocalAuthorisationList();
        for (IdentifyingToken identifyingToken : identifyingTokens) {
            AuthorisationData authData = new AuthorisationData();
            authData.setIdTag(identifyingToken.getToken());

            //The OCPP spec describes that the IdTagInfo should not be present in case the charging station has to remove the entry from the list
            AuthenticationStatus status = identifyingToken.getAuthenticationStatus();
            if (status != null && !AuthenticationStatus.DELETED.equals(status)) {
                IdTagInfo info = new IdTagInfo();
                switch (identifyingToken.getAuthenticationStatus()) {
                    case ACCEPTED:
                        info.setStatus(AuthorizationStatus.ACCEPTED);
                        break;
                    case BLOCKED:
                        info.setStatus(AuthorizationStatus.BLOCKED);
                        break;
                    case EXPIRED:
                        info.setStatus(AuthorizationStatus.EXPIRED);
                        break;
                    case INVALID:
                        info.setStatus(AuthorizationStatus.INVALID);
                        break;
                    case CONCURRENT_TX:
                        info.setStatus(AuthorizationStatus.CONCURRENT_TX);
                        break;
                }
                authData.setIdTagInfo(info);
            }

            authorizationList.add(authData);
        }

        //TODO: Make ALL calls towards the chargingstation more robust (now can result in message processing loop of death), decide on how to achieve this; either by try catching here to force ACK, or not letting Rabbit reschedule upon exception - Ingo Pak, 03 Jan 2014
        SendLocalListResponse response = chargePointService.sendLocalList(request, id.getId());

        if (UpdateStatus.ACCEPTED.equals(response.getStatus())) {
            LOG.info("Update of local authorization list on {} has been accepted", id);
            return RequestStatus.SUCCESS;
        } else {
            String responseStatus = (response.getStatus() != null ? response.getStatus().value() : "-unknown status-");
            LOG.warn("Update of local authorization list on {} has failed due to {}", id, responseStatus);
            return RequestStatus.FAILURE;
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
        io.motown.domain.api.chargingstation.ReservationStatus result = null;

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
        }

        return result;
    }

    private RequestStatus reset(ChargingStationId id, ResetType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ResetRequest request = new ResetRequest();
        request.setType(type);

        ResetResponse response = chargePointService.reset(request, id.getId());

        if (ResetStatus.ACCEPTED.equals(response.getStatus())) {
            return RequestStatus.SUCCESS;
        } else {
            return RequestStatus.FAILURE;
        }
    }

    private RequestStatus changeAvailability(ChargingStationId id, EvseId evseId, AvailabilityType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest();
        request.setConnectorId(evseId.getNumberedId());
        request.setType(type);
        ChangeAvailabilityResponse response = chargePointService.changeAvailability(request, id.getId());

        if (AvailabilityStatus.ACCEPTED.equals(response.getStatus()) ||
            AvailabilityStatus.SCHEDULED.equals(response.getStatus())) {
            return RequestStatus.SUCCESS;
        } else {
            return RequestStatus.FAILURE;
        }
    }

    /**
     * Creates a charging station web service proxy based on the address that has been stored for this charging station identifier.
     *
     * @param id charging station identifier
     * @return charging station web service proxy
     */
    protected ChargePointService createChargingStationService(ChargingStationId id) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(ChargePointService.class);

        factory.setAddress(domainService.retrieveChargingStationAddress(id));

        SoapBindingConfiguration conf = new SoapBindingConfiguration();
        conf.setVersion(Soap12.getInstance());
        factory.setBindingConfig(conf);
        factory.getFeatures().add(new WSAddressingFeature());
        ChargePointService chargePointService = (ChargePointService) factory.create();

        //Force the use of the Async transport, even for synchronous calls
        ((BindingProvider) chargePointService).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);

        return chargePointService;
    }

}
