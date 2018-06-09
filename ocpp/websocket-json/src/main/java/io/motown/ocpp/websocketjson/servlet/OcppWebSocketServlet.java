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
package io.motown.ocpp.websocketjson.servlet;

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.websocketjson.OcppJsonService;
import org.atmosphere.config.service.WebSocketHandlerService;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.websocket.WebSocket;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;
import org.atmosphere.websocket.WebSocketStreamingHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.Reader;

@WebSocketHandlerService
public class OcppWebSocketServlet extends WebSocketStreamingHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(OcppWebSocketServlet.class);

    private OcppJsonService ocppJsonService;

    public OcppWebSocketServlet() {
        // TODO refactor this so application context is not needed - Mark van den Bergh, March 12th 2014
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();

        this.ocppJsonService = context.getBean(OcppJsonService.class);
    }

    public OcppWebSocketServlet(OcppJsonService ocppJsonService) {
        this.ocppJsonService = ocppJsonService;
    }

    @Override
    public void onOpen(final WebSocket webSocket) throws IOException {
        final String chargingStationIdentifier = determineIdentifier(webSocket);

        ocppJsonService.addWebSocket(chargingStationIdentifier, webSocket);

        webSocket.resource().addEventListener(new WebSocketEventListenerAdapter() {
            @Override
            public void onDisconnect(AtmosphereResourceEvent event) {
                LOG.info("Client [{}] disconnected", chargingStationIdentifier);
                ocppJsonService.closeAndRemoveWebSocket(chargingStationIdentifier, webSocket);
            }
        });
    }

    @Override
    public void onTextStream(WebSocket webSocket, Reader reader) throws IOException {
        String chargingStationId = determineIdentifier(webSocket);

        ocppJsonService.handleMessage(new ChargingStationId(chargingStationId), reader);
    }

    public void setOcppJsonService(OcppJsonService ocppJsonService) {
        this.ocppJsonService = ocppJsonService;
    }

    private String determineIdentifier(WebSocket webSocket) {
        return determineIdentifierFromRequest(webSocket.resource().getRequest());
    }

    private String determineIdentifierFromRequest(AtmosphereRequest request) {
        // request.getPathInfo() is said to be unreliable in several containers
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath() + "/";

        return request.getRequestURI().substring((contextPath + servletPath).length());
    }

}
