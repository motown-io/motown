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
import io.motown.ocpp.soaputils.async.*;
import io.motown.ocpp.soaputils.header.SoapHeaderReader;
import io.motown.ocpp.v15.soap.centralsystem.schema.*;
import io.motown.ocpp.v15.soap.centralsystem.schema.FirmwareStatus;
import io.motown.ocpp.v15.soap.chargepoint.Ocpp15RequestHandler;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
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
public class MotownCentralSystemService implements io.motown.ocpp.v15.soap.centralsystem.schema.CentralSystemService {

    private static final Logger LOG = LoggerFactory.getLogger(MotownCentralSystemService.class);

    private static final String PROTOCOL_IDENTIFIER = Ocpp15RequestHandler.PROTOCOL_IDENTIFIER;

    private int heartbeatIntervalFallback;

    /**
     * Timeout in milliseconds for the continuation suspend functionality
     */
    private int continuationTimeout;

    private DomainService domainService;

    private SoapHeaderReader soapHeaderReader;

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
        EvseId evseId = new EvseId(request.getConnectorId());
        ComponentStatus componentStatus = getComponentStatusFromChargePointStatus(request.getStatus());
        String errorCode = request.getErrorCode() != null ? request.getErrorCode().value() : null;

        //TODO timestamp is not mandatory in the WSDL, however it's mandatory in the statusNotification command! - Mark van den Bergh, Februari 21st 2014

        domainService.statusNotification(chargingStationId, evseId, errorCode, componentStatus, request.getInfo(), request.getTimestamp(), request.getVendorId(), request.getVendorErrorCode());
        return new StatusNotificationResponse();
    }

    @Override
    public StopTransactionResponse stopTransaction(StopTransactionRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        NumberedTransactionId transactionId = new NumberedTransactionId(chargingStationId, PROTOCOL_IDENTIFIER, request.getTransactionId());
        //TODO idTag is optional in the web service. TextualToken doesn't accept null/empty string. - Mark van den Bergh, Februari 13th 2014
        IdentifyingToken identifyingToken = new TextualToken(request.getIdTag());

        List<MeterValue> meterValues = new ArrayList<>();
        List<TransactionData> transactionData = request.getTransactionData();
        for (TransactionData data : transactionData) {
            for (io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue mv : data.getValues()){
                for (io.motown.ocpp.v15.soap.centralsystem.schema.MeterValue.Value value : mv.getValue()) {
                    meterValues.add(new MeterValue(mv.getTimestamp(), value.getValue()));
                }
            }
        }

        domainService.stopTransaction(chargingStationId, transactionId, identifyingToken, request.getMeterStop(), request.getTimestamp(), meterValues);
        return new StopTransactionResponse();
    }

    @Override
    public BootNotificationResponse bootNotification(BootNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        String chargingStationAddress = soapHeaderReader.getChargingStationAddress(context.getMessageContext());
        if(chargingStationAddress == null || chargingStationAddress.isEmpty()) {
            BootNotificationResponse response = new BootNotificationResponse();
            response.setStatus(RegistrationStatus.REJECTED);
            response.setHeartbeatInterval(heartbeatIntervalFallback);
            response.setCurrentTime(new Date());

            return response;
        }

        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, chargingStationAddress, request.getChargePointVendor(), request.getChargePointModel(), PROTOCOL_IDENTIFIER,
                request.getChargePointSerialNumber(), request.getChargeBoxSerialNumber(), request.getFirmwareVersion(), request.getIccid(), request.getImsi(), request.getMeterType(), request.getMeterSerialNumber());

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

        domainService.meterValues(chargingStationId, transactionId, new EvseId(request.getConnectorId()), meterValues);

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

        final AuthorizationFutureEventCallback future = new AuthorizationFutureEventCallback();
        final ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        FutureRequestHandler<AuthorizeResponse, AuthorizationResult> handler = new FutureRequestHandler<>(context.getMessageContext(), continuationTimeout);

        return handler.handle(future, new CallInitiator() {
            @Override
            public void initiateCall() {
                domainService.authorize(chargingStationId, request.getIdTag(), future);
            }
        }, new AuthorizeResponseFactory(), new ResponseFactory<AuthorizeResponse>() {
            @Override
            public AuthorizeResponse createResponse() {
                LOG.error("Error while handling 'authorize' request, returning invalid for idTag: {}", request.getIdTag());

                AuthorizeResponse response = new AuthorizeResponse();
                IdTagInfo tagInfo = new IdTagInfo();
                tagInfo.setStatus(AuthorizationStatus.INVALID);
                response.setIdTagInfo(tagInfo);

                return response;
            }
        });
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

        int transactionId = domainService.startTransaction(chargingStationId, new EvseId(parameters.getConnectorId()), new TextualToken(parameters.getIdTag()),
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

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    public void setContinuationTimeout(int continuationTimeout) {
        this.continuationTimeout = continuationTimeout;
    }

    public void setHeartbeatIntervalFallback(int heartbeatIntervalFallback) {
        this.heartbeatIntervalFallback = heartbeatIntervalFallback;
    }

    public void setSoapHeaderReader(SoapHeaderReader soapHeaderReader) {
        this.soapHeaderReader = soapHeaderReader;
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

    private static class AuthorizeResponseFactory implements FutureResponseFactory<AuthorizeResponse, AuthorizationResult> {
        @Override
        public AuthorizeResponse createResponse(AuthorizationResult futureResponse) {
            AuthorizeResponse response = new AuthorizeResponse();
            IdTagInfo tagInfo = new IdTagInfo();
            switch (futureResponse.getStatus()) {
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
    }
}
