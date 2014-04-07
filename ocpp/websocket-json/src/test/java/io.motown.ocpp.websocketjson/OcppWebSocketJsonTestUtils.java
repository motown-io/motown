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
import io.motown.ocpp.websocketjson.gson.GsonFactoryBean;
import io.motown.ocpp.websocketjson.gson.deserializer.*;
import io.motown.ocpp.websocketjson.gson.serializer.*;
import io.motown.ocpp.websocketjson.schema.generated.v15.*;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.websocket.WebSocket;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OcppWebSocketJsonTestUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static Gson getGson() {
        GsonFactoryBean gsonFactoryBean = new GsonFactoryBean();
        gsonFactoryBean.setDateFormat(DATE_FORMAT);
        Set<TypeAdapterSerializer<?>> typeAdapterSerializers = ImmutableSet.<TypeAdapterSerializer<?>>builder()
                .add(new AuthorizationListIdTagStatusTypeAdapterSerializer())
                .add(new BootnotificationResponseStatusTypeAdapterSerializer())
                .add(new DataTransferResponseStatusTypeAdapterSerializer())
                .add(new ResetTypeAdapterSerializer())
                .add(new SendLocalListRequestUpdateTypeAdapterSerializer())
                .add(new StartTransactionIdTagStatusTypeAdapterSerializer())
                .add(new ChangeAvailabilityTypeAdapterSerializer())
                //The serializers below are only needed during testing, as they are used to verify the result
                .add(new ChangeAvailabilityStatusTypeAdapterSerializer())
                .add(new CancelReservationStatusTypeAdapterSerializer())
                .add(new ChangeConfigurationStatusTypeAdapterSerializer())
                .add(new ClearCacheStatusTypeAdapterSerializer())
                .add(new DataTransferResponseStatusTypeAdapterSerializer())
                .add(new RemoteStartTransactionStatusTypeAdapterSerializer())
                .add(new RemoteStopTransactionStatusTypeAdapterSerializer())
                .add(new ReserveNowStatusTypeAdapterSerializer())
                .build();
        gsonFactoryBean.setTypeAdapterSerializers(typeAdapterSerializers);

        Set<TypeAdapterDeserializer<?>> typeAdapterDeserializers = ImmutableSet.<TypeAdapterDeserializer<?>>builder()
                .add(new AuthorizationIdTagStatusAdapterDeserializer())
                .add(new CancelReservationResponseStatusTypeAdapterDeserializer())
                .add(new ChangeAvailabilityTypeAdapterDeserializer())
                .add(new ChangeConfigurationResponseStatusTypeAdapterDeserializer())
                .add(new ChargePointErrorCodeTypeAdapterDeserializer())
                .add(new ChargePointStatusTypeAdapterDeserializer())
                .add(new ClearCacheResponseStatusTypeAdapterDeserializer())
                .add(new DataTransferResponseStatusTypeAdapterDeserializer())
                .add(new DiagnosticsStatusTypeAdapterDeserializer())
                .add(new FirmwareStatusTypeAdapterDeserializer())
                .add(new RemoteStartTransactionResponseTypeAdapterDeserializer())
                .add(new RemoteStopTransactionResponseTypeAdapterDeserializer())
                .add(new ReserveNowResponseStatusTypeAdapterDeserializer())
                .add(new ResetResponseStatusAdapterDeserializer())
                .add(new SendLocalListResponseStatusTypeAdapterDeserializer())
                .add(new StopTransactionIdTagStatusAdapterDeserializer())
                .add(new UnlockConnectorResponseStatusTypeAdapterDeserializer())
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

    public static String createAcceptedCallResult(String callId) {
        return String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, callId, "Accepted");
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ChangeAvailabilityStatusTypeAdapterSerializer implements TypeAdapterSerializer<ChangeavailabilityResponse.Status> {
        @Override
        public JsonElement serialize(ChangeavailabilityResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ChangeavailabilityResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class CancelReservationStatusTypeAdapterSerializer implements TypeAdapterSerializer<CancelreservationResponse.Status> {
        @Override
        public JsonElement serialize(CancelreservationResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return CancelreservationResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ChangeConfigurationStatusTypeAdapterSerializer implements TypeAdapterSerializer<ChangeconfigurationResponse.Status> {
        @Override
        public JsonElement serialize(ChangeconfigurationResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ChangeconfigurationResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ClearCacheStatusTypeAdapterSerializer implements TypeAdapterSerializer<ClearcacheResponse.Status> {
        @Override
        public JsonElement serialize(ClearcacheResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ClearcacheResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class RemoteStartTransactionStatusTypeAdapterSerializer implements TypeAdapterSerializer<RemotestarttransactionResponse.Status> {
        @Override
        public JsonElement serialize(RemotestarttransactionResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return RemotestarttransactionResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class RemoteStopTransactionStatusTypeAdapterSerializer implements TypeAdapterSerializer<RemotestoptransactionResponse.Status> {
        @Override
        public JsonElement serialize(RemotestoptransactionResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return RemotestoptransactionResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ReserveNowStatusTypeAdapterSerializer implements TypeAdapterSerializer<ReservenowResponse.Status> {
        @Override
        public JsonElement serialize(ReservenowResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ReservenowResponse.Status.class;
        }
    }

}
