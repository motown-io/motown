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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.IdentifyingToken;
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
import java.util.HashMap;

public class ChargingStationOcpp15SoapClient implements ChargingStationOcpp15Client {

    private static final Logger log = LoggerFactory.getLogger(ChargingStationOcpp15SoapClient.class);

    @Autowired
    private DomainService domainService;

    public void getConfiguration(ChargingStationId id) {
        log.info("Retrieving configuration of charging station {}", id);

        ChargePointService chargePointService = this.createChargingStationService(id);

        GetConfigurationResponse response =  chargePointService.getConfiguration(new GetConfigurationRequest(), id.getId());

        HashMap<String, String> configurationItems = Maps.newHashMap();
        for (KeyValue keyValue : response.getConfigurationKey()){
            configurationItems.put(keyValue.getKey(), keyValue.getValue());
        }

        domainService.configureChargingStation(id, configurationItems);
    }

    @Override
    public void startTransaction(ChargingStationId id, IdentifyingToken identifyingToken, int connectorId) {
        log.info("Starting transaction");

        ChargePointService chargePointService = this.createChargingStationService(id);

        RemoteStartTransactionRequest request = new RemoteStartTransactionRequest();
        request.setIdTag(identifyingToken.getToken());
        request.setConnectorId(connectorId);

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
        response =  chargePointService.remoteStopTransaction(request, id.getId());

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
    public void unlockConnector(ChargingStationId id, int connectorId) {
        log.debug("Unlocking of connector {} on {}", connectorId, id);
        ChargePointService chargePointService = this.createChargingStationService(id);

        UnlockConnectorRequest request = new UnlockConnectorRequest();
        request.setConnectorId(connectorId);
        UnlockConnectorResponse response = chargePointService.unlockConnector(request, id.getId());
        String responseStatus = (response.getStatus() != null) ? response.getStatus().value() : "Undetermined";

        log.info("Unlocking of connector {} on {} has been {}", connectorId, id, responseStatus);
    }

    @Override
    public void changeAvailabilityToInoperative(ChargingStationId id, int connectorId) {
        log.debug("Changing availability of connector {} on {} to inoperative", connectorId, id);

        changeAvailability(id, connectorId, AvailabilityType.INOPERATIVE);
    }

    @Override
    public void changeAvailabilityToOperative(ChargingStationId id, int connectorId) {
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

    private void reset(ChargingStationId id, ResetType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ResetRequest request = new ResetRequest();
        request.setType(type);
        chargePointService.reset(request, id.getId());
    }

    private void changeAvailability(ChargingStationId id, int connectorId, AvailabilityType type) {
        ChargePointService chargePointService = this.createChargingStationService(id);

        ChangeAvailabilityRequest request = new ChangeAvailabilityRequest();
        request.setConnectorId(connectorId);
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
        ((BindingProvider)chargePointService).getRequestContext().put("use.async.http.conduit", Boolean.TRUE);

        return chargePointService;
    }

}
