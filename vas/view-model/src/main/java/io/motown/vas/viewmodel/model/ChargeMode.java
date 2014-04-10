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
package io.motown.vas.viewmodel.model;

import io.motown.domain.api.chargingstation.ChargingProtocol;

public enum ChargeMode {

    UNSPECIFIED("Unspecified"),
    IEC_61851_MODE_1("Iec61851Mode1"),
    IEC_61851_MODE_2("Iec61851Mode2"),
    IEC_61851_MODE_3("Iec61851Mode3"),
    IEC_61851_MODE_4("Iec61851Mode4"),
    CHA_DE_MO("ChaDeMo");

    private final String value;

    ChargeMode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ChargeMode fromValue(String v) {
        for (ChargeMode c: ChargeMode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

    public static ChargeMode fromChargingProtocol(ChargingProtocol protocol) {
        ChargeMode chargeMode;

        switch(protocol) {
            case MODE1:
                chargeMode = ChargeMode.IEC_61851_MODE_1;
                break;
            case MODE2:
                chargeMode = ChargeMode.IEC_61851_MODE_2;
                break;
            case MODE3:
                chargeMode = ChargeMode.IEC_61851_MODE_3;
                break;
            case MODE4:
                chargeMode = ChargeMode.IEC_61851_MODE_4;
                break;
            case CHA_DE_MO:
                chargeMode = ChargeMode.CHA_DE_MO;
                break;
            case UNSPECIFIED:
                chargeMode = ChargeMode.UNSPECIFIED;
                break;
            default:
                chargeMode = ChargeMode.UNSPECIFIED;
                break;
        }

        return chargeMode;
    }


}