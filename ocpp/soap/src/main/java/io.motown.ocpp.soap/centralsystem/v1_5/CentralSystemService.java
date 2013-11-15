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
package io.motown.ocpp.soap.centralsystem.v1_5;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationRequest;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationResponse;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.ObjectFactory;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.RegistrationStatus;
import io.motown.ocpp.soap.util.DateConverter;
import io.motown.ocpp.viewmodel.ChargingStationSubscriber;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

@Endpoint
public class CentralSystemService {

    private static final String WS_NAMESPACE = "urn://Ocpp/Cs/2012/06/";

    private static final String CB_IDENTITY_NAMESPACE = "{urn://Ocpp/Cs/2012/06/}chargeBoxIdentity";

    @Autowired
    private DomainService domainService;

    @Autowired
    private ObjectFactory objectFactory;

    @Resource(name = "axonAmqpChargingStationSubscriber")
    private ChargingStationSubscriber chargingStationSubscriber;

    @PayloadRoot(namespace = WS_NAMESPACE, localPart = "bootNotificationRequest")
    @ResponsePayload
    public JAXBElement<BootNotificationResponse> handle(@RequestPayload JAXBElement<BootNotificationRequest> bootNotificationRequest, @SoapHeader(CB_IDENTITY_NAMESPACE) SoapHeaderElement chargeBoxIdentityHeader) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentityHeader.getText());
        String chargePointVendor = bootNotificationRequest.getValue().getChargePointVendor();
        String chargePointModel = bootNotificationRequest.getValue().getChargePointModel();

        chargingStationSubscriber.subscribe(chargingStationId);

        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, chargePointVendor, chargePointModel);
        BootNotificationResponse response = objectFactory.createBootNotificationResponse();

        response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
        response.setHeartbeatInterval(result.getHeartbeatInterval());
        response.setCurrentTime(DateConverter.toXmlGregorianCalendar(result.getTimeStamp()));

        return objectFactory.createBootNotificationResponse(response);
    }
}
