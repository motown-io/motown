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
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import io.motown.ocpp.websocketjson.gson.*;
import io.motown.ocpp.websocketjson.request.chargingstation.ChargePointErrorCode;
import io.motown.ocpp.websocketjson.request.chargingstation.ChargePointStatus;
import io.motown.ocpp.websocketjson.request.chargingstation.DiagnosticsStatus;
import io.motown.ocpp.websocketjson.request.chargingstation.FirmwareStatus;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.websocket.WebSocket;

import java.lang.reflect.Type;
import java.util.Set;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OcppWebSocketJsonTestUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static Gson getGson() {
        GsonFactoryBean gsonFactoryBean = new GsonFactoryBean();
        gsonFactoryBean.setDateFormat(DATE_FORMAT);
        Set<TypeAdapterSerializer<?>> typeAdapterSerializers = ImmutableSet.<TypeAdapterSerializer<?>>builder()
                .add(new RegistrationStatusTypeAdapterSerializer())
                .add(new DataTransferStatusTypeAdapterSerializer())
                .add(new DiagnosticsStatusTypeAdapterSerializer())
                .add(new FirmwareStatusTypeAdapterSerializer())
                .add(new ChargePointErrorCodeTypeAdapterSerializer())
                .add(new ChargePointStatusTypeAdapterSerializer())
                .build();
        gsonFactoryBean.setTypeAdapterSerializers(typeAdapterSerializers);

        Set<TypeAdapterDeserializer<?>> typeAdapterDeserializers = ImmutableSet.<TypeAdapterDeserializer<?>>builder()
                .add(new DiagnosticsStatusTypeAdapterDeserializer())
                .add(new UnlockStatusTypeAdapterDeserializer())
                .add(new FirmwareStatusTypeAdapterDeserializer())
                .add(new ChargePointErrorCodeTypeAdapterDeserializer())
                .add(new ChargePointStatusTypeAdapterDeserializer())
                .build();
        gsonFactoryBean.setTypeAdapterDeserializers(typeAdapterDeserializers);

        return gsonFactoryBean.getObject();
    }

    public static WebSocket getMockWebSocket() {
        WebSocket webSocket = mock(WebSocket.class);

        AtmosphereRequest request = mock(AtmosphereRequest.class);
        when(request.getRequestURI()).thenReturn("/websockets/" + CHARGING_STATION_ID.getId());
        when(request.getServletPath()).thenReturn("/websockets");

        AtmosphereResource resource = mock(AtmosphereResource.class);
        when(resource.getRequest()).thenReturn(request);

        when(webSocket.resource()).thenReturn(resource);
        return webSocket;
    }

    /**
     * Only needed for testing
     */
    private static class DiagnosticsStatusTypeAdapterSerializer implements TypeAdapterSerializer<DiagnosticsStatus> {
        @Override
        public JsonElement serialize(DiagnosticsStatus diagnosticsStatus, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(diagnosticsStatus.value());
        }
        @Override
        public Class<?> getAdaptedType() {
            return DiagnosticsStatus.class;
        }
    }

    /**
     * Only needed for testing
     */
    private static class FirmwareStatusTypeAdapterSerializer implements TypeAdapterSerializer<FirmwareStatus> {
        @Override
        public JsonElement serialize(FirmwareStatus firmwareStatus, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(firmwareStatus.value());
        }
        @Override
        public Class<?> getAdaptedType() {
            return FirmwareStatus.class;
        }
    }

    /**
     * Only needed for testing
     */
    private static class ChargePointErrorCodeTypeAdapterSerializer implements TypeAdapterSerializer<ChargePointErrorCode> {
        @Override
        public JsonElement serialize(ChargePointErrorCode chargePointErrorCode, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(chargePointErrorCode.value());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ChargePointErrorCode.class;
        }
    }

    /**
     * Only needed for testing
     */
    private static class ChargePointStatusTypeAdapterSerializer implements TypeAdapterSerializer<ChargePointStatus> {
        @Override
        public JsonElement serialize(ChargePointStatus chargePointStatus, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(chargePointStatus.value());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ChargePointStatus.class;
        }
    }

}
