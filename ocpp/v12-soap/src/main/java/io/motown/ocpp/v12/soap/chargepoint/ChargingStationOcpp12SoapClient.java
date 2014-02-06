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

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.EvseId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
import io.motown.domain.api.chargingstation.RequestStatus;
import io.motown.ocpp.v12.soap.chargepoint.schema.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.ocpp.ChargingStationOcpp12Client;
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

@Component
public class ChargingStationOcpp12SoapClient implements ChargingStationOcpp12Client {

    private static final Logger LOG = LoggerFactory.getLogger(ChargingStationOcpp12SoapClient.class);

    @Autowired
    private DomainService domainService;

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
