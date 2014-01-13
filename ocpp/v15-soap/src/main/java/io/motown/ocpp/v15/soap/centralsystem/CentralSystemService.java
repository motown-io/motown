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

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.chargingstation.MeterValue;
import io.motown.ocpp.v15.soap.async.RequestHandler;
import io.motown.ocpp.v15.soap.async.ResponseFactory;
import io.motown.ocpp.v15.soap.centralsystem.schema.*;
import io.motown.ocpp.v15.soap.centralsystem.schema.FirmwareStatus;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
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
import java.util.ArrayList;
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

    private static final String PROTOCOL_IDENTIFIER = "OCPPS15";

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
    public DataTransferResponse dataTransfer(DataTransferRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        domainService.dataTransfer(chargingStationId, request.getData(), request.getVendorId(), request.getMessageId());

        DataTransferResponse response = new DataTransferResponse();
        response.setStatus(DataTransferStatus.ACCEPTED);
        return response;
    }

    @Override
    public StatusNotificationResponse statusNotification(StatusNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        ConnectorId connectorId = new ConnectorId(request.getConnectorId());
        ComponentStatus componentStatus = getComponentStatusFromChargePointStatus(request.getStatus());
        String errorCode = request.getErrorCode() != null ? request.getErrorCode().value() : null;

        domainService.statusNotification(chargingStationId, connectorId, errorCode, componentStatus, request.getInfo(), request.getTimestamp(), request.getVendorId(), request.getVendorErrorCode());
        return new StatusNotificationResponse();
    }

    @Override
    public StopTransactionResponse stopTransaction(StopTransactionRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        NumberedTransactionId transactionId = new NumberedTransactionId(chargingStationId, PROTOCOL_IDENTIFIER, request.getTransactionId());
        IdentifyingToken identifyingToken = new TextualToken(request.getIdTag());

        List<MeterValue> meterValues = new ArrayList<>();
        List<TransactionData> transactionData = request.getTransactionData();
        for (TransactionData data : transactionData) {
            for (io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue mv : data.getValues()){
                meterValues.add(new MeterValue(mv.getTimestamp(), mv.getValue().toString()));
            }
        }

        domainService.stopTransaction(chargingStationId, transactionId, identifyingToken, request.getMeterStop(), request.getTimestamp(), meterValues);
        return new StopTransactionResponse();
    }

    @Override
    public BootNotificationResponse bootNotification(BootNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        String chargingStationAddress = getChargingStationAddress(context.getMessageContext());
        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, chargingStationAddress, request.getChargePointVendor(), request.getChargePointModel(),
                request.getChargePointSerialNumber(), request.getFirmwareVersion(), request.getIccid(), request.getImsi(), request.getMeterType(), request.getMeterSerialNumber(), PROTOCOL);

        BootNotificationResponse response = new BootNotificationResponse();
        response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
        response.setHeartbeatInterval(result.getHeartbeatInterval());
        response.setCurrentTime(result.getTimeStamp());

        return response;
    }

    @Override
    public HeartbeatResponse heartbeat(HeartbeatRequest parameters, String chargeBoxIdentity) {
        domainService.heartbeat(new ChargingStationId(chargeBoxIdentity));

        HeartbeatResponse response = new HeartbeatResponse();
        response.setCurrentTime(new Date());
        return response;
    }

    @Override
    public MeterValuesResponse meterValues(MeterValuesRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        TransactionId transactionId = new NumberedTransactionId(chargingStationId, PROTOCOL_IDENTIFIER, request.getTransactionId());

        List<MeterValue> meterValues = new ArrayList<>();
        for (io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue mv : request.getValues()) {
            meterValues.add(new MeterValue(mv.getTimestamp(), mv.getValue().toString()));
        }

        domainService.meterValues(chargingStationId, transactionId, new ConnectorId(request.getConnectorId()), meterValues);

        return new MeterValuesResponse();
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification(DiagnosticsStatusNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        DiagnosticsStatus diagnosticsStatus = request.getStatus();

        domainService.diagnosticsUploadStatusUpdate(chargingStationId, DiagnosticsStatus.UPLOADED.equals(diagnosticsStatus));

        return new DiagnosticsStatusNotificationResponse();
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
                        switch (result.getStatus()) {
                            case ACCEPTED:
                                tagInfo.setStatus(AuthorizationStatus.ACCEPTED);
                                break;
                            case BLOCKED:
                                tagInfo.setStatus(AuthorizationStatus.BLOCKED);
                                break;
                            case EXPIRED:
                                tagInfo.setStatus(AuthorizationStatus.EXPIRED);
                                break;
                            case INVALID:
                                tagInfo.setStatus(AuthorizationStatus.INVALID);
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
    public FirmwareStatusNotificationResponse firmwareStatusNotification(FirmwareStatusNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        FirmwareStatus status = request.getStatus();

        io.motown.domain.api.chargingstation.FirmwareStatus firmwareStatus;

        if (FirmwareStatus.INSTALLED.equals(status)) {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.INSTALLED;

        } else if (FirmwareStatus.DOWNLOADED.equals(status)) {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.DOWNLOADED;

        } else if (FirmwareStatus.INSTALLATION_FAILED.equals(status)) {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.INSTALLATION_FAILED;

        } else {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.DOWNLOAD_FAILED;

        }

        domainService.firmwareStatusUpdate(chargingStationId, firmwareStatus);

        return new FirmwareStatusNotificationResponse();
    }

    @Override
    public StartTransactionResponse startTransaction(final StartTransactionRequest parameters, final String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        ReservationId reservationId = null;
        if (parameters.getReservationId() != null) {
            reservationId = new NumberedReservationId(chargingStationId, PROTOCOL_IDENTIFIER, parameters.getReservationId());
        }

        int transactionId = domainService.startTransaction(chargingStationId, new ConnectorId(parameters.getConnectorId()), new TextualToken(parameters.getIdTag()),
                parameters.getMeterStart(), parameters.getTimestamp(), reservationId, PROTOCOL_IDENTIFIER);

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

    /**
     * Gets the {@code ComponentStatus} from a given {@code ChargePointStatus}.
     *
     * @param status the {@code ChargePointStatus}.
     * @return the {@code ComponentStatus}.
     */
    private ComponentStatus getComponentStatusFromChargePointStatus(ChargePointStatus status) {
        String value = status.value();

        if ("Unavailable".equalsIgnoreCase(value)) {
            value = "Inoperative";
        }

        return ComponentStatus.fromValue(value);
    }
}
