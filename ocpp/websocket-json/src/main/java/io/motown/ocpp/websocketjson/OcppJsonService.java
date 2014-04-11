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
package io.motown.ocpp.websocketjson;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.api.security.TypeBasedAddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.handler.*;
import io.motown.ocpp.websocketjson.response.handler.*;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.schema.generated.v15.*;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.atmosphere.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OcppJsonService {

    private static final Logger LOG = LoggerFactory.getLogger(OcppJsonService.class);

    private static final String ADD_ON_TYPE = "OCPPJ15";

    private WampMessageParser wampMessageParser;

    private SchemaValidator schemaValidator;

    private DomainService domainService;

    private Gson gson;

    private AddOnIdentity addOnIdentity;

    public static final String PROTOCOL_IDENTIFIER = OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER;

    /**
     * Map of requestHandlers, key is lowercase procUri.
     */
    private Map<MessageProcUri, RequestHandler> requestHandlers = new HashMap<>();

    /**
     * Map of sockets, key is charging station identifier.
     */
    private Map<String, WebSocket> sockets = new HashMap<>();

    /**
     * Map of response handlers, key is call id.
     */
    private Map<String, ResponseHandler> responseHandlers = new HashMap<>();

    public void handleMessage(ChargingStationId chargingStationId, Reader reader) {
        try {
            WampMessage wampMessage = wampMessageParser.parseMessage(reader);

            LOG.info("Received call from [{}]: {}", chargingStationId.getId(), wampMessage.getPayloadAsString());

            if (WampMessage.CALL == wampMessage.getMessageType()) {
                if (!schemaValidator.isValidRequest(wampMessage.getPayloadAsString(), wampMessage.getProcUri())) {
                    LOG.error("Cannot continue processing invalid request for [{}].", chargingStationId.getId());
                    //TODO send back wamp error?
                    return;
                }

                processWampMessage(chargingStationId, wampMessage);
            } else if (WampMessage.CALL_RESULT == wampMessage.getMessageType()) {
                ResponseHandler responseHandler = responseHandlers.get(wampMessage.getCallId());
                if (responseHandler != null) {
                    responseHandler.handle(chargingStationId, wampMessage, gson, domainService, addOnIdentity);

                    // handled so we remove the handler
                    responseHandlers.remove(wampMessage.getCallId());
                } else {
                    LOG.warn("No response handler found for callId [{}]", wampMessage.getCallId());
                }
            }
        } catch (IllegalArgumentException iae) {
            LOG.error("Unable to handle message", iae);
            //TODO send back wamp error?
        }
    }

    public void changeConfiguration(ChargingStationId chargingStationId, String key, String value, CorrelationToken statusCorrelationToken) {

        Changeconfiguration changeConfigurationRequest = new Changeconfiguration();
        changeConfigurationRequest.setKey(key);
        changeConfigurationRequest.setValue(value);

        responseHandlers.put(statusCorrelationToken.getToken(), new ChangeConfigurationResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.CHANGE_CONFIGURATION, changeConfigurationRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void getConfiguration(ChargingStationId chargingStationId) {

        CorrelationToken statusCorrelationToken = new CorrelationToken();
        Getconfiguration getConfigurationRequest = new Getconfiguration();

        responseHandlers.put(statusCorrelationToken.getToken(), new GetConfigurationResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.GET_CONFIGURATION, getConfigurationRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void getDiagnostics(ChargingStationId chargingStationId, int numRetries, int retryInterval, Date start, Date stop, String uploadLocation, CorrelationToken statusCorrelationToken) {
        Getdiagnostics getDiagnosticsRequest = new Getdiagnostics();
        try {
            getDiagnosticsRequest.setLocation(new URI(uploadLocation));
            getDiagnosticsRequest.setRetries(numRetries);
            getDiagnosticsRequest.setRetryInterval(retryInterval);
            getDiagnosticsRequest.setStartTime(start);
            getDiagnosticsRequest.setStopTime(stop);

            responseHandlers.put(statusCorrelationToken.getToken(), new GetDiagnosticsResponseHandler(statusCorrelationToken));

            WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.GET_DIAGNOSTICS, getDiagnosticsRequest);

            sendWampMessage(wampMessage, chargingStationId);
        } catch (URISyntaxException e) {
            LOG.error("Unable to perform get diagnostics request due to an invalid upload URI.", e);
        }
    }

    public void updateFirmware(ChargingStationId chargingStationId, Date retrieveDate, Map<String, String> attributes, String updateLocation) {
        CorrelationToken statusCorrelationToken = new CorrelationToken();

        try {
            Updatefirmware updateFirmwareRequest = new Updatefirmware();
            updateFirmwareRequest.setRetrieveDate(retrieveDate);
            updateFirmwareRequest.setLocation(new URI(updateLocation));

            String numRetries = attributes.get(FirmwareUpdateAttributeKey.NUM_RETRIES);
            if (numRetries != null) {
                updateFirmwareRequest.setRetries(Double.parseDouble(numRetries));
            }
            String retryInterval = attributes.get(FirmwareUpdateAttributeKey.RETRY_INTERVAL);
            if (retryInterval != null) {
                updateFirmwareRequest.setRetryInterval(Double.parseDouble(retryInterval));
            }

            //No response handler is necessary. No data comes back from the firmwareupdaterequest, so there is nothing to communicate to the caller. Besides that the correlationtoken is not known to the caller.
            WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.UPDATE_FIRMWARE, updateFirmwareRequest);

            sendWampMessage(wampMessage, chargingStationId);
        } catch (URISyntaxException e) {
            LOG.error("Unable to perform update firmware request due to an invalid upload URI.", e);
        }
    }

    public void sendLocalList(ChargingStationId chargingStationId, AuthorizationListUpdateType updateType, List<IdentifyingToken> authorizationList, int authorizationListVersion, String authorizationListHash, CorrelationToken statusCorrelationToken) {

        List<LocalAuthorisationList> localList = Lists.newArrayList();
        for (IdentifyingToken token : authorizationList) {
            LocalAuthorisationList localListEntry = new LocalAuthorisationList();
            localListEntry.setIdTag(token.getToken());

            IdTagInfo_ idTagInfo = new IdTagInfo_();
            idTagInfo.setStatus(convertAuthenticationStatus(token.getAuthenticationStatus()));
            localListEntry.setIdTagInfo(idTagInfo);

            localList.add(localListEntry);
        }

        Sendlocallist sendLocalListRequest = new Sendlocallist();
        sendLocalListRequest.setLocalAuthorisationList(localList);
        sendLocalListRequest.setUpdateType(AuthorizationListUpdateType.FULL.equals(updateType) ? Sendlocallist.UpdateType.FULL : Sendlocallist.UpdateType.DIFFERENTIAL);
        sendLocalListRequest.setListVersion(authorizationListVersion);
        sendLocalListRequest.setHash(authorizationListHash);

        responseHandlers.put(statusCorrelationToken.getToken(), new SendLocalListResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.SEND_LOCALLIST, sendLocalListRequest);

        sendWampMessage(wampMessage, chargingStationId);
    }

    public void reserveNow(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, IdentifyingToken parentIdentifyingToken, Date expiryDate, CorrelationToken statusCorrelationToken) {
        NumberedReservationId reservationId = domainService.generateReservationIdentifier(chargingStationId, PROTOCOL_IDENTIFIER);

        Reservenow reserveNowRequest = new Reservenow();
        reserveNowRequest.setConnectorId(evseId.getNumberedId());
        reserveNowRequest.setIdTag(identifyingToken.getToken());
        if (parentIdentifyingToken != null) {
            reserveNowRequest.setParentIdTag(parentIdentifyingToken.getToken());
        }
        reserveNowRequest.setExpiryDate(expiryDate);
        reserveNowRequest.setReservationId(reservationId.getNumber());

        responseHandlers.put(statusCorrelationToken.getToken(), new ReserveNowResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.RESERVE_NOW, reserveNowRequest);

        sendWampMessage(wampMessage, chargingStationId);
    }

    public void cancelReservation(ChargingStationId chargingStationId, NumberedReservationId reservationId, CorrelationToken statusCorrelationToken) {
        Cancelreservation cancelReservationRequest = new Cancelreservation();
        cancelReservationRequest.setReservationId(reservationId.getNumber());

        responseHandlers.put(statusCorrelationToken.getToken(), new CancelReservationResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.CANCEL_RESERVATION, cancelReservationRequest);

        sendWampMessage(wampMessage, chargingStationId);
    }

    /**
     * Converts the AuthenticationStatus into an OCPPJ specific status
     *
     * @param status
     * @return
     */
    private IdTagInfo_.Status convertAuthenticationStatus(IdentifyingToken.AuthenticationStatus status) {
        switch (status) {
            case ACCEPTED:
                return IdTagInfo_.Status.ACCEPTED;
            case EXPIRED:
                return IdTagInfo_.Status.EXPIRED;
            case DELETED:
                return IdTagInfo_.Status.EXPIRED;
            case CONCURRENT_TX:
                return IdTagInfo_.Status.CONCURRENT_TX;
            case BLOCKED:
                return IdTagInfo_.Status.BLOCKED;
            default:
                return IdTagInfo_.Status.INVALID;
        }
    }

    public void softReset(ChargingStationId chargingStationId, CorrelationToken statusCorrelationToken) {
        Reset softResetRequest = new Reset();
        softResetRequest.setType(Reset.Type.SOFT);

        responseHandlers.put(statusCorrelationToken.getToken(), new ResetResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.RESET, softResetRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void hardReset(ChargingStationId chargingStationId, CorrelationToken statusCorrelationToken) {
        Reset hardResetRequest = new Reset();
        hardResetRequest.setType(Reset.Type.HARD);

        responseHandlers.put(statusCorrelationToken.getToken(), new ResetResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.RESET, hardResetRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void remoteStartTransaction(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, CorrelationToken statusCorrelationToken) {
        Remotestarttransaction remoteStartTransactionRequest = new Remotestarttransaction();
        remoteStartTransactionRequest.setConnectorId(evseId.getNumberedId());
        remoteStartTransactionRequest.setIdTag(identifyingToken.getToken());

        responseHandlers.put(statusCorrelationToken.getToken(), new RemoteStartTransactionResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.REMOTE_START_TRANSACTION, remoteStartTransactionRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void remoteStopTransaction(ChargingStationId chargingStationId, TransactionId transactionId, CorrelationToken statusCorrelationToken) {
        NumberedTransactionId transactionIdNumber = (NumberedTransactionId) transactionId;
        Remotestoptransaction remoteStopTransactionRequest = new Remotestoptransaction();
        remoteStopTransactionRequest.setTransactionId(transactionIdNumber.getNumber());

        responseHandlers.put(statusCorrelationToken.getToken(), new RemoteStopTransactionResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.REMOTE_STOP_TRANSACTION, remoteStopTransactionRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void unlockEvse(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken statusCorrelationToken) {
        Unlockconnector unlockConnectorRequest = new Unlockconnector();
        unlockConnectorRequest.setConnectorId(evseId.getNumberedId());

        responseHandlers.put(statusCorrelationToken.getToken(), new UnlockConnectorResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.UNLOCK_CONNECTOR, unlockConnectorRequest);

        sendWampMessage(wampMessage, chargingStationId);
    }

    public void getLocalListVersion(ChargingStationId chargingStationId, CorrelationToken statusCorrelationToken) {
        Getlocallistversion getlocallistversionRequest = new Getlocallistversion();

        responseHandlers.put(statusCorrelationToken.getToken(), new GetLocalListVersionResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.GET_LOCALLIST_VERSION, getlocallistversionRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void clearCache(ChargingStationId chargingStationId, CorrelationToken statusCorrelationToken) {
        Clearcache clearCacheRequest = new Clearcache();

        responseHandlers.put(statusCorrelationToken.getToken(), new ClearCacheResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.CLEAR_CACHE, clearCacheRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void changeAvailability(ChargingStationId chargingStationId, int connectorId, Changeavailability.Type availabilityType, CorrelationToken statusCorrelationToken) {
        Changeavailability changeAvailabilityRequest = new Changeavailability();
        changeAvailabilityRequest.setConnectorId(connectorId);
        changeAvailabilityRequest.setType(availabilityType);
        responseHandlers.put(statusCorrelationToken.getToken(), new ChangeAvailabilityResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.CHANGE_AVAILABILITY, changeAvailabilityRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void dataTransfer(ChargingStationId chargingStationId, String vendorId, String messageId, String data, CorrelationToken statusCorrelationToken) {
        Datatransfer dataTransferRequest = new Datatransfer();
        dataTransferRequest.setVendorId(vendorId);
        dataTransferRequest.setMessageId(messageId);
        dataTransferRequest.setData(data);

        responseHandlers.put(statusCorrelationToken.getToken(), new DataTransferResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), MessageProcUri.DATA_TRANSFER, dataTransferRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    private void sendWampMessage(WampMessage wampMessage, ChargingStationId chargingStationId) {
        WebSocket webSocket = sockets.get(chargingStationId.getId());
        if (webSocket != null) {
            try {
                webSocket.write(wampMessage.toJson(gson));
            } catch (IOException e) {
                LOG.error("IOException while writing to web socket", e);
            }
        } else {
            LOG.error("No web socket found for charging station id [{}]", chargingStationId.getId());
        }
    }

    private void processWampMessage(ChargingStationId chargingStationId, WampMessage wampMessage) {
        MessageProcUri procUri = wampMessage.getProcUri();
        RequestHandler requestHandler = requestHandlers.get(procUri);

        if (requestHandler == null) {
            switch (procUri) {
                case AUTHORIZE:
                    requestHandler = new AuthorizeRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case BOOT_NOTIFICATION:
                    requestHandler = new BootNotificationRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case DATA_TRANSFER:
                    requestHandler = new DataTransferRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case DIAGNOSTICSS_STATUS_NOTIFICATION:
                    requestHandler = new DiagnosticsStatusNotificationRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case FIRMWARE_STATUS_NOTIFICATION:
                    requestHandler = new FirmwareStatusNotificationRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case HEARTBEAT:
                    requestHandler = new HeartbeatRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case METERVALUES:
                    requestHandler = new MeterValuesRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER, addOnIdentity);
                    break;
                case START_TRANSACTION:
                    requestHandler = new StartTransactionRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER, addOnIdentity);
                    break;
                case STATUS_NOTIFICATION:
                    requestHandler = new StatusNotificationRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case STOP_TRANSACTION:
                    requestHandler = new StopTransactionRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER, addOnIdentity);
                    break;
                default:
                    LOG.error("Unknown ProcUri: " + wampMessage.getProcUri());
                    return;
            }

            requestHandlers.put(procUri, requestHandler);
        }

        requestHandler.handleRequest(chargingStationId, wampMessage.getCallId(), wampMessage.getPayloadAsString(), sockets.get(chargingStationId.getId()));
    }

    public void setWampMessageParser(WampMessageParser wampMessageParser) {
        this.wampMessageParser = wampMessageParser;
    }

    public void setSchemaValidator(SchemaValidator schemaValidator) {
        this.schemaValidator = schemaValidator;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void addWebSocket(String chargingStationIdentifier, WebSocket webSocket) {
        sockets.put(chargingStationIdentifier, webSocket);
    }

    public void addResponseHandler(String callId, ResponseHandler responseHandler) {
        responseHandlers.put(callId, responseHandler);
    }

    public void addRequestHandler(MessageProcUri procUri, RequestHandler requestHandler) {
        requestHandlers.put(procUri, requestHandler);
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
}
