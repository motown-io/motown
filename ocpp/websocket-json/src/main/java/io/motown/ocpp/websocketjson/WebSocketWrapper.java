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
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageHandler;
import org.atmosphere.websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Provides a central access point to the web socket. Checking whether the web socket is open, closing the web socket,
 * sending messages, all those actions go through this wrapper.
 */
public class WebSocketWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketWrapper.class);

    private final ChargingStationId chargingStationId;
    private final WebSocket webSocket;
    private final WampMessageHandler wampMessageHandler;
    private final Gson gson;

    public WebSocketWrapper(ChargingStationId chargingStationId, WebSocket webSocket, WampMessageHandler wampMessageHandler,
                            Gson gson) {
        this.chargingStationId = chargingStationId;
        this.webSocket = webSocket;
        this.wampMessageHandler = wampMessageHandler;
        this.gson = gson;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public boolean isOpen() {
        return webSocket != null && webSocket.isOpen();
    }

    public void close() {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.close();
        }
    }

    public void sendMessage(WampMessage wampMessage) throws ChargePointCommunicationException {
        String wampMessageRaw = wampMessage.toJson(gson);

        try {
            writeToSocket(wampMessageRaw);

            if (wampMessageHandler != null) {
                wampMessageHandler.handleWampCall(
                        chargingStationId.getId(),
                        wampMessageRaw,
                        wampMessage.getCallId()
                );
            }
        } catch (IOException e) {
            LOG.error("IOException while writing to web socket [{}] message [{}]. Closing websocket", chargingStationId, wampMessageRaw, e);
            throw new ChargePointCommunicationException("Error during writing to web socket [" + chargingStationId + "] message [" + wampMessageRaw + "]", e, webSocket);
        }
    }

    public void sendResultMessage(WampMessage wampMessage) {
        String wampMessageRaw = wampMessage.toJson(gson);

        try {
            writeToSocket(wampMessageRaw);

            if (wampMessageHandler != null) {
                wampMessageHandler.handleWampCallResult(
                        chargingStationId.getId(),
                        wampMessageRaw,
                        wampMessage.getCallId()
                );
            }
        } catch (IOException e) {
            LOG.error("IOException while writing to web socket [{}]: {}.", chargingStationId, wampMessageRaw, e);
        }
    }

    private void writeToSocket(String message) throws IOException {
        LOG.info("Writing to charging station [{}]: {}", chargingStationId.getId(), message);

        webSocket.write(message);
    }

    private static String getMessageType(WampMessage wampMessage) {
        return wampMessage.getProcUri() != null ? wampMessage.getProcUri().toString() : null;
    }

}
