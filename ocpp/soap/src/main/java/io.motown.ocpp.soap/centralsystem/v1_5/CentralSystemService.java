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
import io.motown.ocpp.soap.centralsystem.v1_5.schema.*;
import io.motown.ocpp.soap.util.DateConverter;
import io.motown.ocpp.viewmodel.ChargingStationSubscriber;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@javax.jws.WebService(
        serviceName = "CentralSystemService",
        portName = "CentralSystemServiceSoap12",
        targetNamespace = "urn://Ocpp/Cs/2012/06/",
        wsdlLocation = "WEB-INF/wsdl/ocpp_15_centralsystem.wsdl",
        endpointInterface = "io.motown.ocpp.soap.centralsystem.v1_5.schema.CentralSystemService")
public class CentralSystemService implements io.motown.ocpp.soap.centralsystem.v1_5.schema.CentralSystemService {

    @Autowired
    private DomainService domainService;

    @Autowired
    private ObjectFactory objectFactory;

    // not using @Resource(name = "axonAmqpChargingStationSubscriber") as this causes startup issues on Glassfish. JNDI lookups vs Spring.
    @Autowired
    @Qualifier("axonAmqpChargingStationSubscriber")
    private ChargingStationSubscriber chargingStationSubscriber;

    @Override
    public DataTransferResponse dataTransfer(DataTransferRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StatusNotificationResponse statusNotification(StatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StopTransactionResponse stopTransaction(StopTransactionRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public BootNotificationResponse bootNotification(BootNotificationRequest parameters, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        chargingStationSubscriber.subscribe(chargingStationId);

        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, parameters.getChargePointVendor(), parameters.getChargePointModel());

        BootNotificationResponse response = new BootNotificationResponse();
        response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
        response.setHeartbeatInterval(result.getHeartbeatInterval());
        response.setCurrentTime(DateConverter.toXmlGregorianCalendar(result.getTimeStamp()));

        return response;
    }

    @Override
    public HeartbeatResponse heartbeat(HeartbeatRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public MeterValuesResponse meterValues(MeterValuesRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification(DiagnosticsStatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public AuthorizeResponse authorize(AuthorizeRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public FirmwareStatusNotificationResponse firmwareStatusNotification(FirmwareStatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StartTransactionResponse startTransaction(StartTransactionRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        throw new RuntimeException("Not yet implemented");
    }

}
