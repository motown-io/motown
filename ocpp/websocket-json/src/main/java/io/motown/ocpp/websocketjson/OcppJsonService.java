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
    private Map<String, RequestHandler> requestHandlers = new HashMap<>();

    /**
     * Map of sockets, key is charging station identifier.
     */
    private Map<String, WebSocket> sockets = new HashMap<>();

    /**
     * Map of response handlers, key is call id.
     */
    private Map<String, ResponseHandler> responseHandlers = new HashMap<>();

    public void handleMessage(ChargingStationId chargingStationId, Reader reader) {
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
    }

    public void changeConfiguration(ChargingStationId chargingStationId, String key, String value, CorrelationToken statusCorrelationToken) {

        Changeconfiguration changeConfigurationRequest = new Changeconfiguration();
        changeConfigurationRequest.setKey(key);
        changeConfigurationRequest.setValue(value);

        responseHandlers.put(statusCorrelationToken.getToken(), new ChangeConfigurationResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "ChangeConfiguration", changeConfigurationRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void getConfiguration(ChargingStationId chargingStationId) {

        CorrelationToken statusCorrelationToken = new CorrelationToken();
        Getconfiguration getConfigurationRequest = new Getconfiguration();

        responseHandlers.put(statusCorrelationToken.getToken(), new GetConfigurationResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "GetConfiguration", getConfigurationRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void getDiagnostics(ChargingStationId chargingStationId, int numRetries, int retryInterval, Date start, Date stop, String uploadLocation, CorrelationToken statusCorrelationToken) {
        Getdiagnostics getDiagnosticsRequest = new Getdiagnostics();
        try {
            getDiagnosticsRequest.setLocation(new URI(uploadLocation));
            getDiagnosticsRequest.setRetries((double) numRetries);
            getDiagnosticsRequest.setRetryInterval((double) retryInterval);
            getDiagnosticsRequest.setStartTime(start);
            getDiagnosticsRequest.setStopTime(stop);

            responseHandlers.put(statusCorrelationToken.getToken(), new GetDiagnosticsResponseHandler(statusCorrelationToken));

            WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "GetDiagnostics", getDiagnosticsRequest);

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
            if(numRetries != null) {
                updateFirmwareRequest.setRetries(Double.parseDouble(numRetries));
            }
            String retryInterval = attributes.get(FirmwareUpdateAttributeKey.RETRY_INTERVAL);
            if(retryInterval != null) {
                updateFirmwareRequest.setRetryInterval(Double.parseDouble(retryInterval));
            }

            //No response handler is necessary. No data comes back from the firmwareupdaterequest, so there is nothing to communicate to the caller. Besides that the correlationtoken is not known to the caller.
            WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "UpdateFirmware", updateFirmwareRequest);

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
        sendLocalListRequest.setListVersion((double) authorizationListVersion);
        sendLocalListRequest.setHash(authorizationListHash);

        responseHandlers.put(statusCorrelationToken.getToken(), new SendLocalListResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "SendLocalList", sendLocalListRequest);

        sendWampMessage(wampMessage, chargingStationId);
    }

    /**
     * Converts the AuthenticationStatus into an OCPPJ specific status
     * @param status
     * @return
     */
    private IdTagInfo_.Status convertAuthenticationStatus(IdentifyingToken.AuthenticationStatus status) {
        switch(status){
            case ACCEPTED: return IdTagInfo_.Status.ACCEPTED;
            case EXPIRED: return IdTagInfo_.Status.EXPIRED;
            case DELETED: return IdTagInfo_.Status.EXPIRED;
            case CONCURRENT_TX: return IdTagInfo_.Status.CONCURRENT_TX;
            case BLOCKED: return IdTagInfo_.Status.BLOCKED;
            default: return IdTagInfo_.Status.INVALID;
        }
    }

    public void softReset(ChargingStationId chargingStationId, CorrelationToken statusCorrelationToken) {
        Reset softResetRequest = new Reset();
        softResetRequest.setType(Reset.Type.SOFT);

        responseHandlers.put(statusCorrelationToken.getToken(), new ResetResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "Reset", softResetRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void hardReset(ChargingStationId chargingStationId, CorrelationToken statusCorrelationToken) {
        Reset hardResetRequest = new Reset();
        hardResetRequest.setType(Reset.Type.HARD);

        responseHandlers.put(statusCorrelationToken.getToken(), new ResetResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "Reset", hardResetRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void remoteStartTransaction(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, CorrelationToken statusCorrelationToken) {
        Remotestarttransaction remoteStartTransactionRequest = new Remotestarttransaction();
        remoteStartTransactionRequest.setConnectorId((double) evseId.getNumberedId());
        remoteStartTransactionRequest.setIdTag(identifyingToken.getToken());

        responseHandlers.put(statusCorrelationToken.getToken(), new RemoteStartTransactionResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "RemoteStartTransaction", remoteStartTransactionRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void remoteStopTransaction(ChargingStationId chargingStationId, TransactionId transactionId, CorrelationToken statusCorrelationToken) {
        NumberedTransactionId transactionIdNumber = (NumberedTransactionId) transactionId;
        Remotestoptransaction remoteStopTransactionRequest = new Remotestoptransaction();
        remoteStopTransactionRequest.setTransactionId((double) transactionIdNumber.getNumber());

        responseHandlers.put(statusCorrelationToken.getToken(), new RemoteStopTransactionResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "RemoteStopTransaction", remoteStopTransactionRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void unlockEvse(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken statusCorrelationToken) {
        Unlockconnector unlockConnectorRequest = new Unlockconnector();
        unlockConnectorRequest.setConnectorId((double) evseId.getNumberedId());

        responseHandlers.put(statusCorrelationToken.getToken(), new UnlockConnectorResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "UnlockConnector", unlockConnectorRequest);

        sendWampMessage(wampMessage, chargingStationId);
    }

    //TODO: Add the rest of the outgoing calls towards the charging station - Ingo Pak, 02 Apr 2014

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
        String procUri = wampMessage.getProcUri().toLowerCase();

        RequestHandler requestHandler = requestHandlers.get(procUri);

        if(requestHandler == null) {
            switch (procUri) {
                case AuthorizeRequestHandler.PROC_URI:
                    requestHandler = new AuthorizeRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case BootNotificationRequestHandler.PROC_URI:
                    requestHandler = new BootNotificationRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case DataTransferRequestHandler.PROC_URI:
                    requestHandler = new DataTransferRequestHandler(gson, domainService);
                    break;
                case DiagnosticsStatusNotificationRequestHandler.PROC_URI:
                    requestHandler = new DiagnosticsStatusNotificationRequestHandler(gson, domainService);
                    break;
                case FirmwareStatusNotificationRequestHandler.PROC_URI:
                    requestHandler = new FirmwareStatusNotificationRequestHandler(gson, domainService);
                    break;
                case HeartbeatRequestHandler.PROC_URI:
                    requestHandler = new HeartbeatRequestHandler(gson, domainService, addOnIdentity);
                    break;
                case MeterValuesRequestHandler.PROC_URI:
                    requestHandler = new MeterValuesRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER, addOnIdentity);
                    break;
                case StartTransactionRequestHandler.PROC_URI:
                    requestHandler = new StartTransactionRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER, addOnIdentity);
                    break;
                case StatusNotificationRequestHandler.PROC_URI:
                    requestHandler = new StatusNotificationRequestHandler(gson, domainService);
                    break;
                case StopTransactionRequestHandler.PROC_URI:
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

    public void addRequestHandler(String procUri, RequestHandler requestHandler) {
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
