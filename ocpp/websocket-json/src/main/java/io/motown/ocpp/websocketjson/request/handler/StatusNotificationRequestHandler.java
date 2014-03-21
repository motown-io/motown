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
import io.motown.domain.api.chargingstation.ComponentStatus;
import io.motown.domain.api.chargingstation.EvseId;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.ChargePointStatus;
import io.motown.ocpp.websocketjson.request.chargingstation.StatusNotificationRequest;
import io.motown.ocpp.websocketjson.response.centralsystem.StatusNotificationResponse;
import org.atmosphere.websocket.WebSocket;

import java.util.Date;

public class StatusNotificationRequestHandler extends RequestHandler {

    public static final String PROC_URI = "statusnotification";

    private Gson gson;

    private DomainService domainService;

    public StatusNotificationRequestHandler(Gson gson, DomainService domainService) {
        this.gson = gson;
        this.domainService = domainService;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        StatusNotificationRequest request = gson.fromJson(payload, StatusNotificationRequest.class);

        String errorCode = request.getErrorCode() != null ? request.getErrorCode().value() : null;
        Date timestamp = request.getTimestamp();
        if(timestamp == null) {
            timestamp = new Date();
        }

        domainService.statusNotification(chargingStationId, new EvseId(request.getConnectorId()), errorCode, getComponentStatusFromChargePointStatus(request.getStatus()), request.getInfo(), timestamp, request.getVendorId(), request.getVendorErrorCode());

        writeResponse(webSocket, new StatusNotificationResponse(), callId, gson);
    }

    /**
     * Gets the {@code ComponentStatus} from a given {@code ChargePointStatus}.
     *
     * @param status the {@code ChargePointStatus}.
     * @return the {@code ComponentStatus}.
     */
    private ComponentStatus getComponentStatusFromChargePointStatus(ChargePointStatus status) {
        String value = status.value();

        if (ChargePointStatus.UNAVAILABLE.value().equalsIgnoreCase(value)) {
            value = ComponentStatus.INOPERATIVE.value();
        }

        return ComponentStatus.fromValue(value);
    }

}
