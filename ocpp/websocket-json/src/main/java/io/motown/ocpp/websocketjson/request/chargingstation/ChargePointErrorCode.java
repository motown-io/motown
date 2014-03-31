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
package io.motown.ocpp.websocketjson.request.chargingstation;

@Deprecated
public enum ChargePointErrorCode {

    CONNECTOR_LOCK_FAILURE("ConnectorLockFailure"),
    HIGH_TEMPERATURE("HighTemperature"),
    MODE_3_ERROR("Mode3Error"),
    NO_ERROR("NoError"),
    POWER_METER_FAILURE("PowerMeterFailure"),
    POWER_SWITCH_FAILURE("PowerSwitchFailure"),
    READER_FAILURE("ReaderFailure"),
    RESET_FAILURE("ResetFailure"),
    GROUND_FAILURE("GroundFailure"),
    OVER_CURRENT_FAILURE("OverCurrentFailure"),
    UNDER_VOLTAGE("UnderVoltage"),
    WEAK_SIGNAL("WeakSignal"),
    OTHER_ERROR("OtherError");

    private final String value;

    ChargePointErrorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ChargePointErrorCode fromValue(String v) {
        for (ChargePointErrorCode c: ChargePointErrorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
