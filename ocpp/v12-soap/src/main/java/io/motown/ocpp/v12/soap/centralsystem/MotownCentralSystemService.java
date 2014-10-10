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
package io.motown.ocpp.v12.soap.centralsystem;

import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.chargingstation.MeterValue;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.domain.utils.AttributeMap;
import io.motown.domain.utils.AttributeMapKeys;
import io.motown.ocpp.soaputils.async.*;
import io.motown.ocpp.v12.soap.Ocpp12RequestHandler;
import io.motown.ocpp.v12.soap.centralsystem.schema.*;
import io.motown.ocpp.v12.soap.centralsystem.schema.FirmwareStatus;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.soaputils.async.CallInitiator;
import io.motown.soaputils.async.FutureRequestHandler;
import io.motown.soaputils.async.FutureResponseFactory;
import io.motown.soaputils.async.ResponseFactory;
import io.motown.soaputils.header.SoapHeaderReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@javax.jws.WebService(
        serviceName = "CentralSystemService",
        portName = "CentralSystemServiceSoap12",
        targetNamespace = "urn://Ocpp/Cs/2010/08/",
        wsdlLocation = "WEB-INF/wsdl/ocpp_12_centralsystem.wsdl",
        endpointInterface = "io.motown.ocpp.v12.soap.centralsystem.schema.CentralSystemService")
public class MotownCentralSystemService implements CentralSystemService {

    private static final Logger LOG = LoggerFactory.getLogger(MotownCentralSystemService.class);

    private static final String PROTOCOL_IDENTIFIER = "OCPPS12";

    private static final String ADD_ON_TYPE = Ocpp12RequestHandler.ADD_ON_TYPE;

    private int heartbeatIntervalFallback;

    /**
     * Timeout in milliseconds for the continuation suspend functionality
     */
    private int continuationTimeout;

    private DomainService domainService;

    private SoapHeaderReader soapHeaderReader;

    @Resource
    private WebServiceContext context;

    private AddOnIdentity addOnIdentity;

    @Override
    public AuthorizeResponse authorize(final AuthorizeRequest request, final String chargeBoxIdentity) {

        final AuthorizationFutureEventCallback future = new AuthorizationFutureEventCallback();
        final ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        FutureRequestHandler<AuthorizeResponse, AuthorizationResult> handler = new FutureRequestHandler<>(context.getMessageContext(), continuationTimeout);

        return handler.handle(future, new CallInitiator() {
                    @Override
                    public void initiateCall() {
                        domainService.authorize(chargingStationId, request.getIdTag(), future, addOnIdentity);
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
                }
        );
    }

    @Override
    public BootNotificationResponse bootNotification(BootNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        String chargingStationAddress = soapHeaderReader.getChargingStationAddress(context.getMessageContext());
        if (chargingStationAddress == null || chargingStationAddress.isEmpty()) {
            BootNotificationResponse response = new BootNotificationResponse();
            response.setStatus(RegistrationStatus.REJECTED);
            response.setHeartbeatInterval(heartbeatIntervalFallback);
            response.setCurrentTime(new Date());

            return response;
        }

        AttributeMap<String, String> attributes = new AttributeMap<String, String>().
                putIfValueNotNull(AttributeMapKeys.CHARGING_STATION_ADDRESS, chargingStationAddress).
                putIfValueNotNull(AttributeMapKeys.VENDOR_ID, request.getChargePointVendor()).
                putIfValueNotNull(AttributeMapKeys.MODEL, request.getChargePointModel()).
                putIfValueNotNull(AttributeMapKeys.CHARGING_STATION_SERIAL_NUMBER, request.getChargePointSerialNumber()).
                putIfValueNotNull(AttributeMapKeys.CHARGE_BOX_SERIAL_NUMBER, request.getChargeBoxSerialNumber()).
                putIfValueNotNull(AttributeMapKeys.FIRMWARE_VERSION, request.getFirmwareVersion()).
                putIfValueNotNull(AttributeMapKeys.ICCID, request.getIccid()).
                putIfValueNotNull(AttributeMapKeys.IMSI, request.getImsi()).
                putIfValueNotNull(AttributeMapKeys.METER_TYPE, request.getMeterType()).
                putIfValueNotNull(AttributeMapKeys.METER_SERIAL_NUMBER, request.getMeterSerialNumber());

        BootChargingStationResult result = domainService.bootChargingStation(chargingStationId, PROTOCOL_IDENTIFIER,
                attributes, addOnIdentity);

        BootNotificationResponse response = new BootNotificationResponse();
        response.setStatus(result.isAccepted() ? RegistrationStatus.ACCEPTED : RegistrationStatus.REJECTED);
        response.setHeartbeatInterval(result.getHeartbeatInterval());
        response.setCurrentTime(result.getTimeStamp());

        return response;
    }

    @Override
    public DiagnosticsStatusNotificationResponse diagnosticsStatusNotification(DiagnosticsStatusNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        DiagnosticsStatus diagnosticsStatus = request.getStatus();

        domainService.diagnosticsUploadStatusUpdate(chargingStationId, DiagnosticsStatus.UPLOADED.equals(diagnosticsStatus), addOnIdentity);

        return new DiagnosticsStatusNotificationResponse();
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

        domainService.firmwareStatusUpdate(chargingStationId, firmwareStatus, addOnIdentity);

        return new FirmwareStatusNotificationResponse();
    }

    @Override
    public HeartbeatResponse heartbeat(HeartbeatRequest parameters, String chargeBoxIdentity) {
        domainService.heartbeat(new ChargingStationId(chargeBoxIdentity), addOnIdentity);

        HeartbeatResponse response = new HeartbeatResponse();
        response.setCurrentTime(new Date());
        return response;
    }

    @Override
    public MeterValuesResponse meterValues(MeterValuesRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        //OCPP 1.2 does not contain transactionId references in it's metervalue communication
        TransactionId transactionId = null;

        List<MeterValue> meterValues = new ArrayList<>();
        for (io.motown.ocpp.v12.soap.centralsystem.schema.MeterValue mv : request.getValues()) {
            meterValues.add(new MeterValue(mv.getTimestamp(), Integer.toString(mv.getValue())));
        }

        domainService.meterValues(chargingStationId, transactionId, new EvseId(request.getConnectorId()), meterValues, addOnIdentity);

        return new MeterValuesResponse();
    }

    @Override
    public StartTransactionResponse startTransaction(final StartTransactionRequest parameters, final String chargeBoxIdentity) {
        final ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);

        StartTransactionInfo startTransactionInfo = new StartTransactionInfo(new EvseId(parameters.getConnectorId()),
                parameters.getMeterStart(), parameters.getTimestamp(), new TextualToken(parameters.getIdTag()), Collections.<String, String>emptyMap());
        final StartTransactionFutureEventCallback future = new StartTransactionFutureEventCallback(domainService,
                chargingStationId, PROTOCOL_IDENTIFIER, startTransactionInfo, addOnIdentity);

        FutureRequestHandler<StartTransactionResponse, StartTransactionFutureResult> handler = new FutureRequestHandler<>(context.getMessageContext(), continuationTimeout);

        return handler.handle(future, new CallInitiator() {
                    @Override
                    public void initiateCall() {
                domainService.startTransaction(chargingStationId, new EvseId(parameters.getConnectorId()), new TextualToken(parameters.getIdTag()), future, addOnIdentity);
            }
        }, new StartTransactionResponseFactory(), new ResponseFactory<StartTransactionResponse>() {
            @Override
            public StartTransactionResponse createResponse() {
                LOG.error("Error while handling 'startTransaction' request");

                StartTransactionResponse response = new StartTransactionResponse();
                IdTagInfo tagInfo = new IdTagInfo();
                tagInfo.setStatus(AuthorizationStatus.INVALID);
                response.setIdTagInfo(tagInfo);

                return response;
            }
        });
    }

    @Override
    public StatusNotificationResponse statusNotification(StatusNotificationRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        EvseId evseId = new EvseId(request.getConnectorId());
        ComponentStatus componentStatus = getComponentStatusFromChargePointStatus(request.getStatus());
        String errorCode = request.getErrorCode() != null ? request.getErrorCode().value() : null;

        Date timestamp = new Date();
        AttributeMap<String, String> attributes = new AttributeMap<>();
        attributes.putIfValueNotNull(AttributeMapKeys.STATUS_NOTIFICATION_ERROR_CODE_KEY, errorCode);
        domainService.statusNotification(chargingStationId, evseId, new StatusNotification(componentStatus, timestamp, attributes), addOnIdentity);
        return new StatusNotificationResponse();
    }

    @Override
    public StopTransactionResponse stopTransaction(StopTransactionRequest request, String chargeBoxIdentity) {
        ChargingStationId chargingStationId = new ChargingStationId(chargeBoxIdentity);
        NumberedTransactionId transactionId = new NumberedTransactionId(chargingStationId, PROTOCOL_IDENTIFIER, request.getTransactionId());
        IdentifyingToken identifyingToken = new TextualToken(request.getIdTag());

        //OCPP 1.2 does not include the meter values in a stop transaction message
        List<MeterValue> meterValues = new ArrayList<>();

        domainService.stopTransaction(chargingStationId, transactionId, identifyingToken, request.getMeterStop(), request.getTimestamp(), meterValues, addOnIdentity);
        return new StopTransactionResponse();
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
     * Sets the add-on id. The add-on is hardcoded, the add-on id should be different for every instance (in a distributed configuration)
     * to be able to differentiate between add-on instances.
     *
     * @param id add-on id.
     */
    public void setAddOnId(String id) {
        addOnIdentity = new TypeBasedAddOnIdentity(ADD_ON_TYPE, id);
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
            tagInfo.setStatus(convert(futureResponse.getStatus()));
            response.setIdTagInfo(tagInfo);
            return response;
        }
    }

    /**
     * Response factory for start transaction, will create a StartTransactionResponse based on the StartTransactionFutureResult.
     */
    private static class StartTransactionResponseFactory implements FutureResponseFactory<StartTransactionResponse, StartTransactionFutureResult> {
        @Override
        public StartTransactionResponse createResponse(StartTransactionFutureResult startTransactionFutureResult) {

            IdTagInfo idTagInfo = new IdTagInfo();
            idTagInfo.setStatus(convert(startTransactionFutureResult.getAuthorizationResultStatus()));

            StartTransactionResponse response = new StartTransactionResponse();
            response.setIdTagInfo(idTagInfo);
            response.setTransactionId(startTransactionFutureResult.getTransactionId());

            return response;
        }
    }

    /**
     * Converts a {@code AuthorizationResultStatus} to a {@code AuthorizationStatus}. Throws an assertion error is the
     * status is unknown.
     *
     * @param status status to convert.
     * @return converted status.
     */
    private static AuthorizationStatus convert(AuthorizationResultStatus status) {
        AuthorizationStatus result;

        switch (status) {
            case ACCEPTED:
                result = AuthorizationStatus.ACCEPTED;
                break;
            case BLOCKED:
                result = AuthorizationStatus.BLOCKED;
                break;
            case EXPIRED:
                result = AuthorizationStatus.EXPIRED;
                break;
            case INVALID:
                result = AuthorizationStatus.INVALID;
                break;
            default:
                throw new AssertionError("AuthorizationResultStatus has unknown status: " + status);
        }

        return result;
    }
}
