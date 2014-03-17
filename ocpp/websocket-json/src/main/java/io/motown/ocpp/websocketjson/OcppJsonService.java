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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.EvseId;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.*;
import io.motown.ocpp.websocketjson.request.handler.DiagnosticsStatusNotificationRequestHandler;
import io.motown.ocpp.websocketjson.response.centralsystem.*;
import io.motown.ocpp.websocketjson.request.handler.BootNotificationRequestHandler;
import io.motown.ocpp.websocketjson.request.handler.DataTransferRequestHandler;
import io.motown.ocpp.websocketjson.response.handler.ResponseHandler;
import io.motown.ocpp.websocketjson.response.handler.UnlockConnectorResponseHandler;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
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

    private WampMessageParser wampMessageParser;

    private SchemaValidator schemaValidator;

    private DomainService domainService;

    private Gson gson;

    public static final String PROTOCOL_IDENTIFIER = OcppWebSocketRequestHandler.PROTOCOL_IDENTIFIER;

    /**
     * Map of sockets, key is charging station identifier.
     */
    private Map<String, WebSocket> sockets = new HashMap<>();

    /**
     * Map of response handlers, key is call id.
     */
    private Map<String, ResponseHandler> responseHandlers = new HashMap<>();

    public String handleMessage(ChargingStationId chargingStationId, Reader reader) {
        WampMessage wampMessage = wampMessageParser.parseMessage(reader);

        LOG.info("Received call from [{}]: {}", chargingStationId.getId(), wampMessage.getPayloadAsString());

        String result = null;
        if (WampMessage.CALL == wampMessage.getMessageType()) {
            if (!schemaValidator.isValidRequest(wampMessage.getPayloadAsString(), wampMessage.getProcUri())) {
                LOG.error("Cannot continue processing invalid request for [{}].", chargingStationId.getId());
                return null;
            }

            WampMessage processedMessage = processWampMessage(chargingStationId, wampMessage);

            result = processedMessage != null ? processedMessage.toJson(gson) : null;
        } else if (WampMessage.CALL_RESULT == wampMessage.getMessageType()) {
            ResponseHandler responseHandler = responseHandlers.get(wampMessage.getCallId());
            if (responseHandler != null) {
                responseHandler.handle(chargingStationId, wampMessage, gson, domainService);

                // handled so we remove the handler
                responseHandlers.remove(wampMessage.getCallId());
            } else {
                LOG.warn("No response handler found for callId [{}]", wampMessage.getCallId());
            }

            // no response is needed when handling a call result
            result = null;
        }

        return result;
    }

    public void getConfiguration(ChargingStationId chargingStationId) {
        //TODO implement
    }

    public void unlockEvse(ChargingStationId chargingStationId, EvseId evseId, CorrelationToken statusCorrelationToken) {
        UnlockConnectorRequest unlockConnectorRequest = new UnlockConnectorRequest(evseId.getNumberedId());

        responseHandlers.put(statusCorrelationToken.getToken(), new UnlockConnectorResponseHandler(statusCorrelationToken));

        WampMessage wampMessage = new WampMessage(WampMessage.CALL, statusCorrelationToken.getToken(), "UnlockConnector", unlockConnectorRequest);

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

    private WampMessage processWampMessage(ChargingStationId chargingStationId, WampMessage wampMessage) {
        CentralSystemResponse result;

        switch (wampMessage.getProcUri().toLowerCase()) {
            case "bootnotification":
                result = new BootNotificationRequestHandler(gson, domainService).handleRequest(chargingStationId, wampMessage.getPayloadAsString());
                break;
            case "datatransfer":
                result = new DataTransferRequestHandler(gson, domainService).handleRequest(chargingStationId, wampMessage.getPayloadAsString());
                break;
            case "diagnosticsstatusnotification":
                result = new DiagnosticsStatusNotificationRequestHandler(gson, domainService).handleRequest(chargingStationId, wampMessage.getPayloadAsString());
                break;
            default:
                LOG.error("Unknown ProcUri: " + wampMessage.getProcUri());
                return null;
        }

        return new WampMessage(WampMessage.CALL_RESULT, wampMessage.getCallId(), result);
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
}
