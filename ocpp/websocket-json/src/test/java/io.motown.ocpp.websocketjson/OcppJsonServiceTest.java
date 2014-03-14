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

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import io.motown.ocpp.viewmodel.domain.BootChargingStationResult;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.gson.*;
import io.motown.ocpp.websocketjson.request.BootNotificationRequest;
import io.motown.ocpp.websocketjson.request.DataTransferRequest;
import io.motown.ocpp.websocketjson.request.DiagnosticsStatus;
import io.motown.ocpp.websocketjson.response.DataTransferStatus;
import io.motown.ocpp.websocketjson.response.RegistrationStatus;
import io.motown.ocpp.websocketjson.schema.SchemaValidator;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import io.motown.ocpp.websocketjson.wamp.WampMessageParser;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OcppJsonServiceTest {

    private OcppJsonService service;

    private SchemaValidator schemaValidator;

    private DomainService domainService;

    private Gson gson;

    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Before
    public void setup() {
        schemaValidator = mock(SchemaValidator.class);
        // for this tests all requests are valid
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(true);

        GsonFactoryBean gsonFactoryBean = new GsonFactoryBean();
        gsonFactoryBean.setDateFormat(dateFormat);
        Set<TypeAdapterSerializer<?>> typeAdapterSerializers = ImmutableSet.<TypeAdapterSerializer<?>>builder()
                .add(new RegistrationStatusTypeAdapterSerializer())
                .add(new DataTransferStatusTypeAdapterSerializer())
                .build();
        gsonFactoryBean.setTypeAdapterSerializers(typeAdapterSerializers);

        Set<TypeAdapterDeserializer<?>> typeAdapterDeserializers = ImmutableSet.<TypeAdapterDeserializer<?>>builder()
                .add(new DiagnosticsStatusTypeAdapterDeserializer())
                .build();
        gsonFactoryBean.setTypeAdapterDeserializers(typeAdapterDeserializers);

        gson = gsonFactoryBean.getObject();

        domainService = mock(DomainService.class);

        service = new OcppJsonService();
        service.setDomainService(domainService);
        service.setGson(gson);
        service.setSchemaValidator(schemaValidator);
        service.setWampMessageParser(new WampMessageParser());
    }

    @Test
    public void handleUnknownProcUri() {
        String request = String.format("[%d,%s,%s,%s]", WampMessage.CALL, UUID.randomUUID().toString(), "UnknownProcUri", "{\"request\":\"invalid\"}");

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertNull(response);
    }

    @Test
    public void handleBootNotification() {
        BootNotificationRequest request = new BootNotificationRequest(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER);
        Date now = new Date();
        int heartbeatInterval = 900;
        BootChargingStationResult bootResult = new BootChargingStationResult(true, heartbeatInterval, now);
        when(domainService.bootChargingStation(CHARGING_STATION_ID, null, CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, OcppJsonService.PROTOCOL_IDENTIFIER, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER))
                .thenReturn(bootResult);
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, "BootNotification", request);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertEquals(String.format("[%d,\"%s\",{\"status\":\"%s\",\"currentTime\":\"%s\",\"heartbeatInterval\":%d}]", WampMessage.CALL_RESULT, callId, RegistrationStatus.ACCEPTED.value(), new SimpleDateFormat(dateFormat).format(now), heartbeatInterval), response);
    }

    @Test
    public void handleInvalidBootNotification() {
        BootNotificationRequest request = new BootNotificationRequest(CHARGING_STATION_VENDOR, CHARGING_STATION_MODEL, CHARGING_STATION_SERIAL_NUMBER, CHARGE_BOX_SERIAL_NUMBER, FIRMWARE_VERSION, ICCID, IMSI, METER_TYPE, METER_SERIAL_NUMBER);
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), "BootNotification", request);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertNull(response);
    }

    @Test
    public void handleDataTransfer() {
        String messageId = "GetChargeInstruction";
        String data = "";
        DataTransferRequest request = new DataTransferRequest(CHARGING_STATION_VENDOR, messageId, data);
        String callId = UUID.randomUUID().toString();
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, callId, "DataTransfer", request);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertEquals(String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, callId, DataTransferStatus.ACCEPTED.value()), response);
    }

    @Test
    public void handleInvalidDataTransfer() {
        DataTransferRequest request = new DataTransferRequest(CHARGING_STATION_VENDOR, "GetChargeInstruction", "");
        WampMessage wampMessage = new WampMessage(WampMessage.CALL, UUID.randomUUID().toString(), "DataTransfer", request);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(wampMessage.toJson(gson)));

        assertNull(response);
    }

    @Test
    public void handleDiagnosticsStatusNotification() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"DiagnosticsStatusNotification\",{\"status\":\"%s\"}]", WampMessage.CALL, callId, DiagnosticsStatus.UPLOADED.value());

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertEquals(String.format("[%d,\"%s\",{}]", WampMessage.CALL_RESULT, callId), response);
    }

    @Test
    public void handleInvalidDiagnosticsStatusNotification() {
        String callId = UUID.randomUUID().toString();
        String request = String.format("[%d,\"%s\",\"DiagnosticsStatusNotification\",{\"status\":\"%s\"}]", WampMessage.CALL, callId, DiagnosticsStatus.UPLOADED);
        when(schemaValidator.isValidRequest(anyString(), anyString())).thenReturn(false);

        String response = service.handleMessage(CHARGING_STATION_ID, new StringReader(request));

        assertNull(response);
    }

}
