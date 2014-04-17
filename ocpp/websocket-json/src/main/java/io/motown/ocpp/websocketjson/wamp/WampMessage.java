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
package io.motown.ocpp.websocketjson.wamp;

import com.google.gson.Gson;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WampMessage {

    public static final int CALL = 2;

    public static final int CALL_RESULT = 3;

    public static final int CALL_ERROR = 4;

    private int messageType;
    private String callId;
    private MessageProcUri procUri;
    private Object payload;

    private String errorCode;
    private String errorDescription;
    private String errorDetails;

    private static final int CALL_PROC_URI_INDEX = 2;
    private static final int CALL_PAYLOAD_INDEX = 3;
    private static final int CALL_RESULT_PAYLOAD_INDEX = 2;
    private static final int CALL_ERROR_ERROR_CODE_INDEX = 2;
    private static final int CALL_ERROR_ERROR_DESCRIPTION_INDEX = 3;
    private static final int CALL_ERROR_ERROR_DETAILS_INDEX = 4;

    public WampMessage(List<Object> list) {
        messageType = ((Double) list.get(0)).intValue();
        callId = list.get(1).toString();

        switch (messageType) {
            case CALL:
                procUri = MessageProcUri.fromValue(list.get(CALL_PROC_URI_INDEX).toString());
                payload = list.get(CALL_PAYLOAD_INDEX);
                break;
            case CALL_RESULT:
                payload = list.get(CALL_RESULT_PAYLOAD_INDEX).toString();
                break;
            case CALL_ERROR:
                errorCode = list.get(CALL_ERROR_ERROR_CODE_INDEX).toString();
                errorDescription = list.get(CALL_ERROR_ERROR_DESCRIPTION_INDEX).toString();
                errorDetails = list.get(CALL_ERROR_ERROR_DETAILS_INDEX).toString();
                break;
            default:
                break;
        }
    }

    public WampMessage(int messageType, String callId, MessageProcUri procUri, Object payload) {
        this.messageType = messageType;
        this.callId = callId;
        this.procUri = procUri;
        this.payload = payload;
    }

    public WampMessage(int messageType, String callId, Object payload) {
        this.messageType = messageType;
        this.callId = callId;
        this.payload = payload;
    }

    public WampMessage(int messageType, String callId, String errorCode, String errorDescription, String errorDetails) {
        this.messageType = messageType;
        this.callId = callId;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errorDetails = errorDetails;
    }

    public String toJson(Gson gson) {
        List<Object> target = new LinkedList<>();

        target.add(getMessageType());
        target.add(getCallId());

        switch (messageType) {
            case CALL:
                target.add(getProcUri());
                target.add(getPayload());
                break;
            case CALL_RESULT:
                target.add(getPayload());
                break;
            case CALL_ERROR:
                target.add(getErrorCode());
                target.add(getErrorDescription());
                target.add(getErrorDetails());
                break;
            default:
                break;
        }

        return gson.toJson(target);
    }

    public int getMessageType() {
        return messageType;
    }

    public String getCallId() {
        return callId;
    }

    public MessageProcUri getProcUri() {
        return procUri;
    }

    public Object getPayload() {
        return payload;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public String getPayloadAsString() {
        if (payload instanceof String) {
            return (String) payload;
        } else {
            return new Gson().toJson(payload);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, callId, procUri, payload, errorCode, errorDescription, errorDetails);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WampMessage other = (WampMessage) obj;
        return Objects.equals(this.messageType, other.messageType) && Objects.equals(this.callId, other.callId) && Objects.equals(this.procUri, other.procUri) && Objects.equals(this.payload, other.payload) && Objects.equals(this.errorCode, other.errorCode) && Objects.equals(this.errorDescription, other.errorDescription) && Objects.equals(this.errorDetails, other.errorDetails);
    }
}
