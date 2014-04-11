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
package io.motown.ocpp.websocketjson.request.handler;

import com.google.gson.Gson;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.MessageProcUri;
import io.motown.ocpp.websocketjson.schema.generated.v15.Diagnosticsstatusnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.DiagnosticsstatusnotificationResponse;
import org.atmosphere.websocket.WebSocket;

public class DiagnosticsStatusNotificationRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public DiagnosticsStatusNotificationRequestHandler(Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Diagnosticsstatusnotification request = gson.fromJson(payload, Diagnosticsstatusnotification.class);

        domainService.diagnosticsUploadStatusUpdate(chargingStationId, request.getStatus().equals(Diagnosticsstatusnotification.Status.UPLOADED), addOnIdentity);

        writeResponse(webSocket, new DiagnosticsstatusnotificationResponse(), callId, gson);
    }
}
