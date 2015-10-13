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

/**
 * Interface that can be implemented in order to process Wamp messages
 */
public interface WampMessageHandler {

    /**
     * Handle @see io.motown.ocpp.websocketjson.wamp.CALL
     * @param chargingStationId identifier of the charging station that sent the call
     * @param wampMessageRaw the unparsed WAMP message sent by the charging station
     */
    public void handleWampCall(String chargingStationId, String wampMessageRaw);

    /**
     * Handle @see io.motown.ocpp.websocketjson.wamp.CALL_RESULT
     * @param chargingStationId identifier of the charging station that sent the call
     * @param wampMessageRaw the unparsed WAMP message sent by the charging station
     */
    public void handleWampCallResult(String chargingStationId, String wampMessageRaw);

    /**
     * Handle @see io.motown.ocpp.websocketjson.wamp.CALL_ERROR
     * @param chargingStationId identifier of the charging station that sent the call
     * @param wampMessageRaw the unparsed WAMP message sent by the charging station
     */
    public void handleWampCallError(String chargingStationId, String wampMessageRaw);

    /**
     * Handle a message that can't be parsed as a Wamp message
     * @param chargingStationId identifier of the charging station that sent the call
     * @param wampMessageRaw the unparsed message sent by the charging station when unable to parse as WAMP
     */
    public void handle(String chargingStationId, String message);
}
