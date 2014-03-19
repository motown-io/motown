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
import io.motown.domain.api.chargingstation.*;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.chargingstation.ChargePointStatus;
import io.motown.ocpp.websocketjson.request.chargingstation.StatusNotificationRequest;
import io.motown.ocpp.websocketjson.response.centralsystem.StatusNotificationResponse;

public class StatusNotificationRequestHandler implements RequestHandler {

    private Gson gson;

    private DomainService domainService;

    public StatusNotificationRequestHandler(Gson gson, DomainService domainService) {
        this.gson = gson;
        this.domainService = domainService;
    }

    @Override
    public StatusNotificationResponse handleRequest(ChargingStationId chargingStationId, String payload) {
        StatusNotificationRequest request = gson.fromJson(payload, StatusNotificationRequest.class);

        String errorCode = request.getErrorCode() != null ? request.getErrorCode().value() : null;
        domainService.statusNotification(chargingStationId, new EvseId(request.getConnectorId()), errorCode, getComponentStatusFromChargePointStatus(request.getStatus()), request.getInfo(), request.getTimestamp(), request.getVendorId(), request.getVendorErrorCode());

        return new StatusNotificationResponse();
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
