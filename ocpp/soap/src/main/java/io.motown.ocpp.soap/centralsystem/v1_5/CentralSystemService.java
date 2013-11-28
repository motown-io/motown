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
import io.motown.ocpp.soap.async.RequestHandler;
import io.motown.ocpp.soap.async.ResponseFactory;
import io.motown.ocpp.soap.centralsystem.v1_5.schema.*;
import io.motown.ocpp.viewmodel.ChargingStationSubscriber;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.Date;

@javax.jws.WebService(
        serviceName = "CentralSystemService",
        portName = "CentralSystemServiceSoap12",
        targetNamespace = "urn://Ocpp/Cs/2012/06/",
        wsdlLocation = "WEB-INF/wsdl/ocpp_15_centralsystem.wsdl",
        endpointInterface = "io.motown.ocpp.soap.centralsystem.v1_5.schema.CentralSystemService")
public class CentralSystemService implements io.motown.ocpp.soap.centralsystem.v1_5.schema.CentralSystemService {

    private static final Logger log = LoggerFactory.getLogger(CentralSystemService.class);

    //TODO make this configurable
    /**
     * Heartbeat interval which will be returned to the client if handling the bootNotification failed
     */
    private final int HEARTBEAT_INTERVAL_FALLBACK = 900;

    @Autowired
    private DomainService domainService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Resource
    private WebServiceContext context;

    // not using @Resource(name = "axonAmqpChargingStationSubscriber") for this bean as it causes startup issues on Glassfish. JNDI lookups vs Spring.
    @Autowired
    @Qualifier("axonAmqpChargingStationSubscriber")
    private ChargingStationSubscriber chargingStationSubscriber;

    @Override
    public DataTransferResponse dataTransfer(DataTransferRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [dataTransfer] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StatusNotificationResponse statusNotification(StatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [statusNotification] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StopTransactionResponse stopTransaction(StopTransactionRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [stopTransaction] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public BootNotificationResponse bootNotification(final BootNotificationRequest request, final String chargeBoxIdentity) {
        RequestHandler<BootNotificationResponse> requestHandler = new RequestHandler<>(context.getMessageContext(), taskExecutor);
        return requestHandler.handle(
            new ResponseFactory<BootNotificationResponse>() {
                @Override
                public BootNotificationResponse createResponse() {
                    ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

                    chargingStationSubscriber.subscribe(chargingStationId);

                    //TODO: Read the 'From' ws address header that holds the ipaddress of the chargingstation - Ingo Pak, 27 nov 2013
                    String chargingStationAddress = "";

                    BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, chargingStationAddress, request.getChargePointVendor(), request.getChargePointModel());

                    BootNotificationResponse response = new BootNotificationResponse();
                    response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
                    response.setHeartbeatInterval(result.getHeartbeatInterval());
                    response.setCurrentTime(result.getTimeStamp());

                    return response;
                }
            },
            new ResponseFactory<BootNotificationResponse>() {
                @Override
                public BootNotificationResponse createResponse() {
                    BootNotificationResponse response = new BootNotificationResponse();
                    response.setHeartbeatInterval(HEARTBEAT_INTERVAL_FALLBACK);
                    response.setStatus(RegistrationStatus.REJECTED);
                    response.setCurrentTime(new Date());
                    return response;
                }
            }
        );
    }

    @Override
    public HeartbeatResponse heartbeat(HeartbeatRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [heartbeat] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public MeterValuesResponse meterValues(MeterValuesRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [meterValues] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification(DiagnosticsStatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [diagnosticsStatusNotification] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public AuthorizeResponse authorize(AuthorizeRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [authorize] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public FirmwareStatusNotificationResponse firmwareStatusNotification(FirmwareStatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [firmwareStatusNotification] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StartTransactionResponse startTransaction(StartTransactionRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [startTransaction] called.");
        throw new RuntimeException("Not yet implemented");
    }
}
