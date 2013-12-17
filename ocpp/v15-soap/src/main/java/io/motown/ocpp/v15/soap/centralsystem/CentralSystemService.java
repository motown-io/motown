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
package io.motown.ocpp.v15.soap.centralsystem;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.TextualToken;
import io.motown.ocpp.v15.soap.async.RequestHandler;
import io.motown.ocpp.v15.soap.async.ResponseFactory;
import io.motown.ocpp.v15.soap.centralsystem.schema.*;
import io.motown.ocpp.v15.soap.centralsystem.schema.AuthorizationStatus;
import io.motown.ocpp.viewmodel.domain.*;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.w3c.dom.Element;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@javax.jws.WebService(
        serviceName = "CentralSystemService",
        portName = "CentralSystemServiceSoap12",
        targetNamespace = "urn://Ocpp/Cs/2012/06/",
        wsdlLocation = "WEB-INF/wsdl/ocpp_15_centralsystem.wsdl",
        endpointInterface = "io.motown.ocpp.v15.soap.centralsystem.schema.CentralSystemService")
public class CentralSystemService implements io.motown.ocpp.v15.soap.centralsystem.schema.CentralSystemService {

    private static final Logger log = LoggerFactory.getLogger(CentralSystemService.class);

    /**
     * Timeout in milliseconds for the continuation suspend functionality
     */
    @Value("${io.motown.ocpp.v15.soap.cxf.continuation.timeout}")
    private int CONTINUATION_TIMEOUT;

    /**
     * Heartbeat interval which will be returned to the client if handling the bootNotification failed
     */
    @Value("${io.motown.ocpp.v15.soap.heartbeat.interval.fallback}")
    private int HEARTBEAT_INTERVAL_FALLBACK;

    @Value("${io.motown.ocpp.v15.soap.protocol.identifier}")
    private String PROTOCOL;

    @Autowired
    private DomainService domainService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Resource
    private WebServiceContext context;

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

        return new StatusNotificationResponse();
    }

    @Override
    public StopTransactionResponse stopTransaction(StopTransactionRequest request, String chargeBoxIdentity) {
        domainService.stopTransaction(new ChargingStationId(chargeBoxIdentity), request.getTransactionId(), request.getIdTag(), request.getMeterStop(), request.getTimestamp());
        return new StopTransactionResponse();
    }

    @Override
    public BootNotificationResponse bootNotification(BootNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        String chargingStationAddress = getChargingStationAddress(context.getMessageContext());
        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, chargingStationAddress, request.getChargePointVendor(), request.getChargePointModel(), PROTOCOL);

        BootNotificationResponse response = new BootNotificationResponse();
        response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
        response.setHeartbeatInterval(result.getHeartbeatInterval());
        response.setCurrentTime(result.getTimeStamp());

        return response;
    }

    @Override
    public HeartbeatResponse heartbeat(HeartbeatRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [heartbeat] called.");

        HeartbeatResponse response = new HeartbeatResponse();
        response.setCurrentTime(new Date());
        return response;
    }

    @Override
    public MeterValuesResponse meterValues(MeterValuesRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [meterValues] called.");

        return new MeterValuesResponse();
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification(DiagnosticsStatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [diagnosticsStatusNotification] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public AuthorizeResponse authorize(final AuthorizeRequest request, final String chargeBoxIdentity) {
        final MessageContext messageContext = context.getMessageContext();

        RequestHandler<AuthorizeResponse> requestHandler = new RequestHandler<>(context.getMessageContext(), taskExecutor, CONTINUATION_TIMEOUT);
        return requestHandler.handle(
                new ResponseFactory<AuthorizeResponse>() {
                    @Override
                    public AuthorizeResponse createResponse() {
                        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

                        AuthorizationResult result = domainService.authorize(chargingStationId, request.getIdTag());

                        AuthorizeResponse response = new AuthorizeResponse();
                        IdTagInfo tagInfo = new IdTagInfo();
                        switch(result.getStatus()){
                            case ACCEPTED: tagInfo.setStatus(AuthorizationStatus.ACCEPTED);
                                break;
                            case BLOCKED: tagInfo.setStatus(AuthorizationStatus.BLOCKED);
                                break;
                            case EXPIRED: tagInfo.setStatus(AuthorizationStatus.EXPIRED);
                                break;
                            case INVALID: tagInfo.setStatus(AuthorizationStatus.INVALID);
                                break;
                        }
                        response.setIdTagInfo(tagInfo);

                        return response;
                    }
                },
                new ResponseFactory<AuthorizeResponse>() {
                    @Override
                    public AuthorizeResponse createResponse() {
                        AuthorizeResponse response = new AuthorizeResponse();
                        IdTagInfo tagInfo = new IdTagInfo();
                        tagInfo.setStatus(AuthorizationStatus.INVALID);
                        return response;
                    }
                }
        );
    }

    @Override
    public FirmwareStatusNotificationResponse firmwareStatusNotification(FirmwareStatusNotificationRequest parameters, String chargeBoxIdentity) {
        //FIXME implement me
        log.error("Unimplemented method [firmwareStatusNotification] called.");
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public StartTransactionResponse startTransaction(final StartTransactionRequest parameters, final String chargeBoxIdentity) {
        int transactionId = domainService.startTransaction(new ChargingStationId(chargeBoxIdentity), parameters.getConnectorId(), new TextualToken(parameters.getIdTag()), parameters.getMeterStart(), parameters.getTimestamp());
        log.debug("TransactionId: " + transactionId);

        // TODO locally store identifications, so we can use these in the response. - Dennis Laumen, December 16th 2013
        IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setStatus(AuthorizationStatus.ACCEPTED);
        idTagInfo.setParentIdTag(parameters.getIdTag());

        GregorianCalendar expDate = new GregorianCalendar();
        expDate.add(GregorianCalendar.YEAR, 1);
        idTagInfo.setExpiryDate(expDate.getTime());

        StartTransactionResponse response = new StartTransactionResponse();
        response.setIdTagInfo(idTagInfo);
        response.setTransactionId(transactionId);
        return response;
    }

    /**
     * Gets the charging station address from the SOAP "From" header.
     *
     * @param messageContext the message context
     * @return charging station address, or empty string if From header is empty or doesn't exist.
     */
    private String getChargingStationAddress(MessageContext messageContext) {
        if (messageContext == null || !(messageContext instanceof WrappedMessageContext)) {
            log.warn("Unable to get message context, or message context is not the right type.");
            return "";
        }

        Message message = ((WrappedMessageContext) messageContext).getWrappedMessage();
        List<Header> headers = CastUtils.cast((List<?>) message.get(Header.HEADER_LIST));

        for (Header h : headers) {
            Element n = (Element) h.getObject();

            if (n.getLocalName().equals("From")) {
                return n.getTextContent();
            }
        }

        log.warn("No 'From' header found in request. Not able to determine charging station address.");
        return "";
    }
}
