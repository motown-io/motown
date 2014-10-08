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
import io.motown.domain.api.chargingstation.StatusNotification;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.domain.utils.AttributeMap;
import io.motown.domain.utils.AttributeMapKeys;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.Statusnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.StatusnotificationResponse;
import org.atmosphere.websocket.WebSocket;

import java.util.Date;

public class StatusNotificationRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public StatusNotificationRequestHandler(Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        this.gson = gson;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocket webSocket) {
        Statusnotification request = gson.fromJson(payload, Statusnotification.class);

        String errorCode = request.getErrorCode() != null ? request.getErrorCode().toString() : null;
        Date timestamp = request.getTimestamp();
        if(timestamp == null) {
            timestamp = new Date();
        }

        AttributeMap<String, String> attributes = new AttributeMap<String, String>().
                putIfValueNotNull(AttributeMapKeys.STATUS_NOTIFICATION_VENDOR_ERROR_CODE_KEY, errorCode).
                putIfValueNotNull(AttributeMapKeys.STATUS_NOTIFICATION_INFO_KEY, request.getInfo()).
                putIfValueNotNull(AttributeMapKeys.STATUS_NOTIFICATION_VENDOR_ID_KEY, request.getVendorId()).
                putIfValueNotNull(AttributeMapKeys.STATUS_NOTIFICATION_VENDOR_ERROR_CODE_KEY, request.getVendorErrorCode());

        domainService.statusNotification(chargingStationId,
                new StatusNotification(new EvseId(request.getConnectorId()), getComponentStatusFromChargePointStatus(request.getStatus()), timestamp, attributes),
                addOnIdentity);

        writeResponse(webSocket, new StatusnotificationResponse(), callId, gson);
    }

    /**
     * Gets the {@code ComponentStatus} from a given {@code ChargePointStatus}.
     *
     * @param status the {@code ChargePointStatus}.
     * @return the {@code ComponentStatus}.
     */
    private ComponentStatus getComponentStatusFromChargePointStatus(Statusnotification.Status status) {
        String value = status.toString();

        if (Statusnotification.Status.UNAVAILABLE.toString().equalsIgnoreCase(value)) {
            value = ComponentStatus.INOPERATIVE.value();
        }

        return ComponentStatus.fromValue(value);
    }

}
