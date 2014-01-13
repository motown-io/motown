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
import io.motown.domain.api.chargingstation.AuthorisationListUpdateType;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.ConnectorId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.RequestStatus;
import io.motown.ocpp.v15.soap.chargepoint.schema.*;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp15Client;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.binding.soap.SoapBindingConfiguration;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.ws.BindingProvider;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static io.motown.domain.api.chargingstation.IdentifyingToken.AuthenticationStatus;

public class ChargingStationOcpp15SoapClient implements ChargingStationOcpp15Client {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationOcpp15SoapClient.class);

    @Autowired
    private DomainService domainService;

    public void getConfiguration(ChargingStationId id) {
        log.info("Retrieving configuration of charging station {}", id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        GetConfigurationResponse response = chargePointService.getConfiguration(new GetConfigurationRequest(), id.getId());

        HashMap<String, String> configurationItems = Maps.newHashMap();
        for (KeyValue keyValue : response.getConfigurationKey()) {
            configurationItems.put(keyValue.getKey(), keyValue.getValue());
        }

        domainService.configureChargingStation(id, configurationItems);
    }

    @Override
    public void startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, ConnectorId connectorId) {
        log.info("Starting transaction");

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(identifyingToken.getToken());
        request.setConnectorId(connectorId.getNumberedId());

        RemoteStartTransactionResponse response = chargePointService.remoteStartTransaction(request, id.getId());

        log.info("Start transaction request has been {}", response.getStatus().value());
    }

    @Override
    public void stopTransaction(ChargingStationId id, int transactionId) {
        log.debug("Stopping transaction {} on {}", transactionId, id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStopTransactionRequest request = new RemoteStopTransactionRequest();
        request.setTransactionId(transactionId);
        RemoteStopTransactionResponse response;
        response = chargePointService.remoteStopTransaction(request, id.getId());

        log.info("Stop transaction {} on {} has been {}", transactionId, id, response.getStatus().value());
    }

    @Override
    public void softReset(ChargingStationId id) {
        log.info("Requesting soft reset on {}", id);

        reset(id, ResetType.SOFT);
    }

    @Override
    public void hardReset(ChargingStationId id) {
        log.info("Requesting hard reset on {}", id);

        reset(id, ResetType.HARD);
    }

    @Override
    public void unlockConnector(ChargingStationId id, ConnectorId connectorId) {
        log.debug("Unlocking of connector {} on {}", connectorId, id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        UnlockConnectorRequest request = new UnlockConnectorRequest();
        request.setConnectorId(connectorId.getNumberedId());
        UnlockConnectorResponse response = chargePointService.unlockConnector(request, id.getId());
        String responseStatus = (response.getStatus() != null) ? response.getStatus().value() : "Undetermined";

        log.info("Unlocking of connector {} on {} has been {}", connectorId, id, responseStatus);
    }

    @Override
    public void changeAvailabilityToInoperative(ChargingStationId id, ConnectorId connectorId) {
        log.debug("Changing availability of connector {} on {} to inoperative", connectorId, id);

        changeAvailability(id, connectorId, AvailabilityType.INOPERATIVE);
    }

    @Override
    public void changeAvailabilityToOperative(ChargingStationId id, ConnectorId connectorId) {
        log.debug("Changing availability of connector {} on {} to operative", connectorId, id);

        changeAvailability(id, connectorId, AvailabilityType.OPERATIVE);
    }

    @Override
    public void dataTransfer(ChargingStationId id, String vendorId, String messageId, String data) {
        log.debug("Data transfer to {}", id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        DataTransferRequest request = new DataTransferRequest();
        request.setVendorId(vendorId);
        request.setMessageId(messageId);
        request.setData(data);

        DataTransferResponse response = chargePointService.dataTransfer(request, id.getId());
        String responseStatus = (response.getStatus() != null) ? response.getStatus().value() : "Undetermined";
        log.info("Data transfer to {} was {}", id, responseStatus);
    }

    @Override
    public void changeConfiguration(ChargingStationId id, String key, String value) {
        log.debug("Change configuration of {}", id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeConfigurationRequest request = new ChangeConfigurationRequest();
        request.setKey(key);
        request.setValue(value);

        ChangeConfigurationResponse response = chargePointService.changeConfiguration(request, id.getId());
        String responseStatus = (response.getStatus() != null) ? response.getStatus().value() : "Undetermined";
        log.info("Configuration change of {} on {} has been {}", key, id, responseStatus);
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
        try{
            ClearCacheResponse response = chargePointService.clearCache(request, id.getId());

            if (ClearCacheStatus.ACCEPTED.equals(response.getStatus())) {
                log.info("Clear cache on {} has been accepted", id.getId());
                requestStatus = RequestStatus.SUCCESS;
            }
            else {
                log.warn("Clear cache on {} has been rejected", id.getId());
                requestStatus = RequestStatus.FAILURE;
            }
        } catch(Exception e){
            log.error("Clear cache on {} has failed", id.getId(), e);
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

        try {
            chargePointService.updateFirmware(request, id.getId());

            //The charging station will respond with an async 'firmware status update' message
            log.info("Update firmware on {} has been requested", id.getId());
        } catch (Exception e) {
            log.error("Unable to request update firmware for {}", id, e);
        }
    }

    @Override
    public int getAuthorisationListVersion(ChargingStationId id) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        GetLocalListVersionResponse response = chargePointService.getLocalListVersion(new GetLocalListVersionRequest(), id.getId());
        int currentWhitelistVersion = response.getListVersion();

        log.info("At the moment {} has authorisationlist version {}", id.getId(), currentWhitelistVersion);
        return currentWhitelistVersion;
    }

    @Override
    public void sendAuthorisationList(ChargingStationId id, String hash, int listVersion, List<IdentifyingToken> identifyingTokens, AuthorisationListUpdateType updateType) {
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

        //Translate the authorisation information to the OCPP specific info
        List<AuthorisationData> authorisationList = request.getLocalAuthorisationList();
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

            authorisationList.add(authData);
        }

        //TODO: Make ALL calls towards the chargingstation more robust (now can result in message processing loop of death), decide on how to achieve this; either by try catching here to force ACK, or not letting Rabbit reschedule upon exception - Ingo Pak, 03 Jan 2014
        SendLocalListResponse response = chargePointService.sendLocalList(request, id.getId());
        String responseStatus = (response.getStatus() != null ? response.getStatus().value() : "Undetermined");
        log.info("Update of local authorisation list on {} has been {}", id, responseStatus);
    }

    @Override
    public io.motown.domain.api.chargingstation.ReservationStatus reserveNow(ChargingStationId id, ConnectorId connectorId, IdentifyingToken identifyingToken, Date expiryDate, IdentifyingToken parentIdentifyingToken, int reservationId) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ReserveNowRequest request = new ReserveNowRequest();
        request.setConnectorId(connectorId.getNumberedId());
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

    private void reset(ChargingStationId id, ResetType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ResetRequest request = new ResetRequest();
        request.setType(type);
        chargePointService.reset(request, id.getId());
    }

    private void changeAvailability(ChargingStationId id, ConnectorId connectorId, AvailabilityType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest();
        request.setConnectorId(connectorId.getNumberedId());
        request.setType(type);
        chargePointService.changeAvailability(request, id.getId());
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
