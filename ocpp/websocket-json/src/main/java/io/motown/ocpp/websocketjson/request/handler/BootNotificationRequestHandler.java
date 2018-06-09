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
import io.motown.domain.utils.AttributeMap;
import io.motown.domain.utils.AttributeMapKeys;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.OcppJsonService;
import io.motown.ocpp.websocketjson.WebSocketWrapper;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;
import io.motown.ocpp.websocketjson.schema.generated.v15.Bootnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.BootnotificationResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessageHandler;

public class BootNotificationRequestHandler extends RequestHandler {

    private Gson gson;

    private DomainService domainService;

    private AddOnIdentity addOnIdentity;

    public BootNotificationRequestHandler(Gson gson, DomainService domainService, AddOnIdentity addOnIdentity, WampMessageHandler wampMessageHandler) {
        super(wampMessageHandler);
        this.gson = gson;
        this.domainService = domainService;
        this.addOnIdentity = addOnIdentity;
    }

    @Override
    public void handleRequest(ChargingStationId chargingStationId, String callId, String payload, WebSocketWrapper webSocketWrapper) {
        Bootnotification request = gson.fromJson(payload, Bootnotification.class);

        AttributeMap<String, String> attributes = new AttributeMap<String, String>().
                putIfValueNotNull(AttributeMapKeys.VENDOR_ID, request.getChargePointVendor()).
                putIfValueNotNull(AttributeMapKeys.MODEL, request.getChargePointModel()).
                putIfValueNotNull(AttributeMapKeys.CHARGING_STATION_SERIAL_NUMBER, request.getChargePointSerialNumber()).
                putIfValueNotNull(AttributeMapKeys.CHARGE_BOX_SERIAL_NUMBER, request.getChargeBoxSerialNumber()).
                putIfValueNotNull(AttributeMapKeys.FIRMWARE_VERSION, request.getFirmwareVersion()).
                putIfValueNotNull(AttributeMapKeys.ICCID, request.getIccid()).
                putIfValueNotNull(AttributeMapKeys.IMSI, request.getImsi()).
                putIfValueNotNull(AttributeMapKeys.METER_TYPE, request.getMeterType()).
                putIfValueNotNull(AttributeMapKeys.METER_SERIAL_NUMBER, request.getMeterSerialNumber());

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(chargingStationId, OcppJsonService.PROTOCOL_IDENTIFIER, attributes, addOnIdentity);

        BootnotificationResponse response = new BootnotificationResponse();
        response.setStatus(bootChargingStationResult.isAccepted()? BootnotificationResponse.Status.ACCEPTED:BootnotificationResponse.Status.REJECTED);
        response.setCurrentTime(bootChargingStationResult.getTimeStamp());
        response.setHeartbeatInterval(bootChargingStationResult.getHeartbeatInterval());

        writeResponse(webSocketWrapper, response, callId, MessageProcUri.BOOT_NOTIFICATION);
    }
}
