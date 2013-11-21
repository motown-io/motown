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
import org.apache.cxf.continuations.Continuation;
import org.apache.cxf.continuations.ContinuationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
     * Timeout in milliseconds for the continuation suspend functionality
     */
    private final int CONTINUATION_TIMEOUT = 5000;

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
    public BootNotificationResponse bootNotification(final BootNotificationRequest parameters, final String chargeBoxIdentity) {
        final Continuation continuation = getContinuation();

        if (continuation == null) {
            log.error("Failed to get continuation, falling back to synchronous request handling. Make sure async-supported is set to true on the CXF servlet (web.xml)");
            //TODO implement fallback - Mark van den Bergh, November 21st 2013
            throw new RuntimeException("Failed to get continuation");
        }

        synchronized (continuation) {
            if(continuation.isNew()) {
                FutureTask futureResponse = new FutureTask<>(new Callable<BootNotificationResponse>() {
                    @Override
                    public BootNotificationResponse call() throws Exception {
                        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

                        chargingStationSubscriber.subscribe(chargingStationId);

                        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, parameters.getChargePointVendor(), parameters.getChargePointModel());

                        BootNotificationResponse response = new BootNotificationResponse();
                        response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
                        response.setHeartbeatInterval(result.getHeartbeatInterval());
                        response.setCurrentTime(DateConverter.toXmlGregorianCalendar(result.getTimeStamp()));

                        // the blocking call has finished, we resume the transport thread
                        continuation.resume();

                        return response;
                    }
                });
                taskExecutor.execute(futureResponse);
                continuation.setObject(futureResponse);

                // suspend the transport thread so it can handle other requests
                continuation.suspend(CONTINUATION_TIMEOUT);
                return null;
            } else {
                FutureTask futureTask = (FutureTask) continuation.getObject();
                if(futureTask.isDone()) {
                    try {
                        return (BootNotificationResponse) futureTask.get();
                    } catch (InterruptedException | ExecutionException e) {
                        // handling the bootNotification has (probably) failed, log the error and return REJECTED

                        log.error(e.getClass().getName() + " while waiting for response on boot notification, returning REJECTED to client.");
                        e.printStackTrace();

                        BootNotificationResponse response = new BootNotificationResponse();
                        response.setHeartbeatInterval(HEARTBEAT_INTERVAL_FALLBACK);
                        response.setStatus(RegistrationStatus.REJECTED);
                        response.setCurrentTime(DateConverter.toXmlGregorianCalendar(new Date()));
                        return response;
                    }
                } else {
                    //TODO should the timeout decrease every time the task is not yet done? - Mark van den Bergh, November 21st 2013
                    continuation.suspend(CONTINUATION_TIMEOUT);
                }
            }
        }
        // unreachable
        return null;
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

    /**
     * Gets a continuation object from the provider which is present in message context
     *
     * @return continuation
     */
    private Continuation getContinuation() {
        ContinuationProvider provider = (ContinuationProvider) context.getMessageContext().get(ContinuationProvider.class.getName());
        return provider.getContinuation();
    }

}
