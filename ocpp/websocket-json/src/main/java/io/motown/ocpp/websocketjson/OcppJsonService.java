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

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.AddOnIdentity;
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
import java.util.HashMap;
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
                responseHandler.handle(chargingStationId, wampMessage, gson, domainService);

                // handled so we remove the handler
                responseHandlers.remove(wampMessage.getCallId());
            } else {
                LOG.warn("No response handler found for callId [{}]", wampMessage.getCallId());
            }
        }
    }

    /**
     * Performs the synchronous call to retrieve the charging station configuration
     * @param chargingStationId
     */
    public void getConfiguration(ChargingStationId chargingStationId) {

        CorrelationToken statusCorrelationToken = new CorrelationToken();
        Getconfiguration getConfigurationRequest = new Getconfiguration();

        responseHandlers.put(statusCorrelationToken.getToken(), new GetConfigurationResponseHandler(statusCorrelationToken));

        //Map<String, String> configurationItems = chargingStationOcpp15Client.getConfiguration(event.getChargingStationId());

        //domainService.configureChargingStation(event.getChargingStationId(), configurationItems);

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "GetConfiguration", getConfigurationRequest);
        sendWampMessage(wampMessage, chargingStationId);
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

    public void startTransaction(ChargingStationId chargingStationId, EvseId evseId, IdentifyingToken identifyingToken, CorrelationToken statusCorrelationToken) {
        Remotestarttransaction remoteStartTransactionRequest = new Remotestarttransaction();
        remoteStartTransactionRequest.setConnectorId((double) evseId.getNumberedId());
        remoteStartTransactionRequest.setIdTag(identifyingToken.getToken());

        responseHandlers.put(statusCorrelationToken.getToken(), new RemoteStartTransactionResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "RemoteStartTransaction", remoteStartTransactionRequest);
        sendWampMessage(wampMessage, chargingStationId);
    }

    public void stopTransaction(ChargingStationId chargingStationId, TransactionId transactionId, CorrelationToken statusCorrelationToken) {
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
                    requestHandler = new AuthorizeRequestHandler(gson, domainService);
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
                    requestHandler = new MeterValuesRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER);
                    break;
                case StartTransactionRequestHandler.PROC_URI:
                    requestHandler = new StartTransactionRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER);
                    break;
                case StatusNotificationRequestHandler.PROC_URI:
                    requestHandler = new StatusNotificationRequestHandler(gson, domainService);
                    break;
                case StopTransactionRequestHandler.PROC_URI:
                    requestHandler = new StopTransactionRequestHandler(gson, domainService, PROTOCOL_IDENTIFIER);
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
}
