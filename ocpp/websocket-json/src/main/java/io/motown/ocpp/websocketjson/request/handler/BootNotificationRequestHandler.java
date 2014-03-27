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
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.OcppJsonService;
import io.motown.ocpp.websocketjson.request.chargingstation.BootNotificationRequest;
import io.motown.ocpp.websocketjson.response.centralsystem.BootNotificationResponse;
import io.motown.ocpp.websocketjson.response.centralsystem.RegistrationStatus;
import org.atmosphere.websocket.WebSocket;

public class BootNotificationRequestHandler extends RequestHandler {

    public static final String PROC_URI = "bootnotification";

    private Gson gson;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public BootNotificationRequestHandler(Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        BootNotificationRequest request = gson.fromJson(payload, BootNotificationRequest.class);

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(chargingStationId, null, request.getChargePointVendor(),
                request.getChargePointModel(), OcppJsonService.PROTOCOL_IDENTIFIER, request.getChargePointSerialNumber(), request.getChargeBoxSerialNumber(),
                request.getFirmwareVersion(), request.getIccid(), request.getImsi(), request.getMeterType(),
                request.getMeterSerialNumber(), addOnIdentity);

        BootNotificationResponse response = new BootNotificationResponse(bootChargingStationResult.isAccepted()? RegistrationStatus.ACCEPTED:RegistrationStatus.REJECTED, bootChargingStationResult.getTimeStamp(), bootChargingStationResult.getHeartbeatInterval());

        writeResponse(webSocket, response, callId, gson);
    }
}
