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

import java.util.LinkedList;
import java.util.List;

public class WampMessage {

    public static final int CALL = 2;

    public static final int CALL_RESULT = 3;

    public static final int CALL_ERROR = 4;

    private int messageType;
    private String callId;
    private String procUri;
    private Object payload;

    private String errorCode;
    private String errorDescription;
    private String errorDetails;

    public WampMessage(List<Object> list) {
        messageType = ((Double) list.get(0)).intValue();
        callId = list.get(1).toString();

        switch (messageType) {
            case CALL:
                procUri = list.get(2).toString();
                payload = list.get(3);
                break;
            case CALL_RESULT:
                payload = list.get(2).toString();
                break;
            case CALL_ERROR:
                errorCode = list.get(2).toString();
                errorDescription = list.get(3).toString();
                errorDetails = list.get(4).toString();
                break;
        }
    }

    public WampMessage(int messageType, String callId, String procUri, Object payload) {
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
        }

        return gson.toJson(target);
    }

    public int getMessageType() {
        return messageType;
    }

    public String getCallId() {
        return callId;
    }

    public String getProcUri() {
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
        return new Gson().toJson(payload);
    }

}
