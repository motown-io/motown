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
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.request.BootNotificationRequest;
import io.motown.ocpp.websocketjson.request.DataTransferRequest;
import io.motown.ocpp.websocketjson.response.BootNotificationResponse;
import io.motown.ocpp.websocketjson.response.DataTransferResponse;
import io.motown.ocpp.websocketjson.response.DataTransferStatus;
import io.motown.ocpp.websocketjson.response.RegistrationStatus;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

public class OcppJsonService {

    private static final Logger LOG = LoggerFactory.getLogger(OcppJsonService.class);

    private WampMessageParser wampMessageParser;

    private SchemaValidator schemaValidator;

    private DomainService domainService;

    private Gson gson;

    // TODO ocppj 1.2?
    public static final String PROTOCOL_IDENTIFIER = "OCPPJ15";

    public String handleMessage(ChargingStationId chargingStationId, Reader reader) {
        WampMessage wampMessage = wampMessageParser.parseMessage(reader);

        LOG.info("Received call from [{}]: {}", chargingStationId.getId(), wampMessage.getPayloadAsString());

        if (!schemaValidator.isValidRequest(wampMessage.getPayloadAsString(), wampMessage.getProcUri())) {
            LOG.error("Cannot continue processing invalid request for [{}].", chargingStationId.getId());
            return null;
        }

        WampMessage processedMessage = processWampMessage(chargingStationId, wampMessage);

        return processedMessage != null ? processedMessage.toJson(gson) : null;
    }

    private WampMessage processWampMessage(ChargingStationId chargingStationId, WampMessage wampMessage) {
        Object result;

        switch (wampMessage.getProcUri().toLowerCase()) {
            case "bootnotification":
                result = processBootNotification(chargingStationId, wampMessage.getPayloadAsString());
                break;
            case "datatransfer":
                result = processDataTransfer(chargingStationId, wampMessage.getPayloadAsString());
                break;
            default:
                LOG.error("Unknown ProcUri: " + wampMessage.getProcUri());
                return null;
        }

        return new WampMessage(WampMessage.CALL_RESULT, wampMessage.getCallId(), result);
    }

    private BootNotificationResponse processBootNotification(ChargingStationId chargingStationId, String payload) {
        BootNotificationRequest request = gson.fromJson(payload, BootNotificationRequest.class);

        BootChargingStationResult bootChargingStationResult = domainService.bootChargingStation(chargingStationId, null, request.getChargePointVendor(),
                request.getChargePointModel(), PROTOCOL_IDENTIFIER, request.getChargePointSerialNumber(), request.getChargeBoxSerialNumber(),
                request.getFirmwareVersion(), request.getIccid(), request.getImsi(), request.getMeterType(),
                request.getMeterSerialNumber());

        return new BootNotificationResponse(bootChargingStationResult.isAccepted()?RegistrationStatus.ACCEPTED:RegistrationStatus.REJECTED, bootChargingStationResult.getTimeStamp(), bootChargingStationResult.getHeartbeatInterval());
    }

    private DataTransferResponse processDataTransfer(ChargingStationId chargingStationId, String payload) {
        DataTransferRequest request = gson.fromJson(payload, DataTransferRequest.class);

        domainService.dataTransfer(chargingStationId, request.getData(), request.getVendorId(), request.getMessageId());

        return new DataTransferResponse(DataTransferStatus.ACCEPTED, null);
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
}
