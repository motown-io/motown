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
import io.motown.ocpp.websocketjson.schema.generated.v15.Firmwarestatusnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.FirmwarestatusnotificationResponse;
import org.atmosphere.websocket.WebSocket;

public class FirmwareStatusNotificationRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public FirmwareStatusNotificationRequestHandler(Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Firmwarestatusnotification request = gson.fromJson(payload, Firmwarestatusnotification.class);

        Firmwarestatusnotification.Status status = request.getStatus();

        io.motown.domain.api.chargingstation.FirmwareStatus firmwareStatus;

        if (Firmwarestatusnotification.Status.INSTALLED.equals(status)) {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.INSTALLED;
        } else if (Firmwarestatusnotification.Status.DOWNLOADED.equals(status)) {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.DOWNLOADED;
        } else if (Firmwarestatusnotification.Status.INSTALLATION_FAILED.equals(status)) {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.INSTALLATION_FAILED;
        } else {
            firmwareStatus = io.motown.domain.api.chargingstation.FirmwareStatus.DOWNLOAD_FAILED;
        }

        domainService.firmwareStatusUpdate(chargingStationId, firmwareStatus, addOnIdentity);

        writeResponse(webSocket, new FirmwarestatusnotificationResponse(), callId, gson);
    }
}
