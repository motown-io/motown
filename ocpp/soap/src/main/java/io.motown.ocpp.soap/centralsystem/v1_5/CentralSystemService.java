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

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationRequest;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.BootNotificationResponse;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.ObjectFactory;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.RegistrationStatus;
import io.motown.ocpp.viewmodel.ChargingStationSubscriber;
import io.motown.ocpp.viewmodel.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

@Endpoint
public class CentralSystemService {
    @Autowired
    private DomainService domainService;
    @Autowired
    private ObjectFactory objectFactory;
    @Resource(name = "axonAmqpChargingStationSubscriber")
    private ChargingStationSubscriber chargingStationSubscriber;

    @PayloadRoot(namespace = "urn://Ocpp/Cs/2012/06/", localPart = "bootNotificationRequest")
    @ResponsePayload
    public JAXBElement<BootNotificationResponse> handle(@RequestPayload JAXBElement<BootNotificationRequest> bootNotificationRequest, @SoapHeader("{urn://Ocpp/Cs/2012/06/}chargeBoxIdentity") SoapHeaderElement chargeBoxIdentityHeader) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentityHeader.getText());
        String chargePointVendor = bootNotificationRequest.getValue().getChargePointVendor();
        String chargePointModel = bootNotificationRequest.getValue().getChargePointModel();

        chargingStationSubscriber.subscribe(chargingStationId);

        // TODO: this (results in Maps) is only placeholder code and should be removed as soon as we properly implement BootNotifications. - Dennis Laumen, November 13th 2013
        Map<String, Serializable> result = domainService.bootChargingStation(chargingStationId, chargePointVendor, chargePointModel);

        BootNotificationResponse response = objectFactory.createBootNotificationResponse();

        if ("ACCEPTED".equalsIgnoreCase((String) result.get("registrationStatus"))) {
            response.setStatus(RegistrationStatus.ACCEPTED);
        } else if ("REJECTED".equalsIgnoreCase((String) result.get("registrationStatus"))) {
            response.setStatus(RegistrationStatus.REJECTED);
        }

        response.setHeartbeatInterval((Integer) result.get("heartbeatInterval"));

        GregorianCalendar now = new GregorianCalendar();
        now.setTime((Date) result.get("timestamp"));
        response.setCurrentTime(new XMLGregorianCalendarImpl(now));

        return objectFactory.createBootNotificationResponse(response);
    }
}
