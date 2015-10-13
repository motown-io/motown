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

import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.ocpp.websocketjson.schema.MessageProcUri;

import java.io.IOException;
import java.io.Reader;

/**
 * Wamp specific message parser
 */
public class WampMessageParser {

    private WampMessageHandler wampMessageHandler;

    public WampMessageParser() {
    }

    /**
     * Parses a CALL, RESULT, or ERROR message and constructs a WampMessage
     * @param chargingStationId sending the message
     * @param reader containing the message
     * @return WampMessage
     * @throws IOException in case the message could not be read
     * @throws IllegalArgumentException in case an unknown wamp messageType is encountered
     */
    public WampMessage parseMessage(ChargingStationId chargingStationId, Reader reader) throws IOException {
        String rawMessage = this.convertToString(reader);
        String trimmedMessage = this.removeBrackets(rawMessage);

        //In case a payload is present, it always is the last part of the message
        int payloadStart = trimmedMessage.indexOf("{");
        String payload = payloadStart > 0 ? trimmedMessage.substring(payloadStart) : null;
        String metaData = payloadStart > 0 ? trimmedMessage.substring(0, payloadStart) : trimmedMessage;
        String[] metaDataParts = metaData.split(",");

        int messageType = Integer.parseInt(removeQuotesAndTrim(metaDataParts[0]));
        String callId = removeQuotes(removeQuotesAndTrim(metaDataParts[1]));

        WampMessage wampMessage;
        switch (messageType) {
            case WampMessage.CALL:
                MessageProcUri procUri = MessageProcUri.fromValue(removeQuotesAndTrim(metaDataParts[2]));
                wampMessage = new WampMessage(messageType, callId, procUri, payload);
                if (wampMessageHandler != null) {
                    wampMessageHandler.handleWampCall(chargingStationId.getId(), rawMessage);
                }
                break;
            case WampMessage.CALL_RESULT:
                wampMessage = new WampMessage(messageType, callId, payload);
                if (wampMessageHandler != null) {
                    wampMessageHandler.handleWampCallResult(chargingStationId.getId(), rawMessage);
                }
                break;
            case WampMessage.CALL_ERROR:
                String errorCode = removeQuotes(metaDataParts[2]);
                String errorDescription = removeQuotes(metaDataParts[3]);
                String errorDetails = removeQuotes(metaDataParts[4]);
                wampMessage = new WampMessage(messageType, callId, errorCode, errorDescription, errorDetails);
                if (wampMessageHandler != null) {
                    wampMessageHandler.handleWampCallError(chargingStationId.getId(), rawMessage);
                }
                break;
            default:
                if (wampMessageHandler != null) {
                    wampMessageHandler.handle(chargingStationId.getId(), rawMessage);
                }
                throw new IllegalArgumentException(String.format("Unknown WAMP messageType: %s", messageType));
        }
        return wampMessage;
    }

    /**
     * Constructs a String by reading the characters from the Reader
     * @param reader Reader to read from
     * @return String containing the message
     * @throws IOException in case of read failure
     */
    private String convertToString(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int numChars;
        char[] chars = new char[50];
        do {
            numChars = reader.read(chars, 0, chars.length);
            if (numChars > 0) {
                stringBuilder.append(chars, 0, numChars);
            }
        } while (numChars != -1);

        return stringBuilder.toString();
    }

    private String removeBrackets(String wampString) {
        //Remove any leading or trailing spaces
        String result = wampString.trim();
        //Strip off the leading '[' and trailing ']' characters
        return result.substring(1, result.length() - 1);
    }

    /**
     * Removes all single and double quotes from the given String.
     * @param toReplace String to replace the quotes from
     * @return String without quotes
     */
    private String removeQuotes(String toReplace) {
        String noQuotes = toReplace.replaceAll("\"", "");
        noQuotes = noQuotes.replaceAll("'", "");
        return noQuotes;
    }

    /**
     * Removes leading and trailing spaces next to the removal of quotes {@link #removeQuotes(String)}
     * @param message the message to trim.
     * @return String free of quotes and leading/trailing spaces
     */
    private String removeQuotesAndTrim(String message) {
        return removeQuotes(message).trim();
    }

    public void setWampMessageHandler(WampMessageHandler wampMessageHandler) {
        this.wampMessageHandler = wampMessageHandler;
    }
}
